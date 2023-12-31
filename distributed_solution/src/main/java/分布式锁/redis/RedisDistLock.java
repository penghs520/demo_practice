package 分布式锁.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by penghs at 2023/12/21 11:29
 */
public class RedisDistLock implements Lock {

    private final String lockName;

    private static final JedisPool jedisPool;

    static {
        jedisPool = new JedisPool("172.25.51.88", 6379, null, "kanban");
    }

    private static final int LOCK_TIME = 2 * 1000;//锁的过期时间，防止死锁
    private final static String LOCK_TIME_STR = String.valueOf(LOCK_TIME);
    private final static String RELEASE_LOCK_LUA =
            """
                    if redis.call('get',KEYS[1])==ARGV[1] then
                            return redis.call('del', KEYS[1])
                        else return 0 end
                    """;

    private Thread ownerThread;//当前持有锁的线程，用于判支持可重入
    private final ThreadLocal<String> lockerId = new ThreadLocal<>();//每个线程持有一个唯一标识，用于解锁

    public RedisDistLock(String lockName) {
        this.lockName = lockName;
    }

    @Override
    public void lock() {
        while (!tryLock()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void unlock() {
        if (ownerThread != Thread.currentThread()) {
            throw new RuntimeException("试图释放无所有权的锁！");
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long result = (Long) jedis.eval(RELEASE_LOCK_LUA,
                    Collections.singletonList(lockName),
                    Collections.singletonList(lockerId.get()));
            if (result != 0L) {
                System.out.println("Redis上的锁已释放！");
            } else {
                System.out.println("Redis上的锁释放失败！");
            }
        } catch (Exception e) {
            throw new RuntimeException("释放锁失败！", e);
        } finally {
            if (jedis != null) jedis.close();
            lockerId.remove();
            ownerThread = null;
            System.out.println("本地锁所有权已释放！");
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException("不支持可中断获取锁！");
    }

    @Override
    public boolean tryLock() {
        Thread t = Thread.currentThread();
        if (t == ownerThread) {//如果当前线程为锁的持有者，可重入
            return true;
        } /*else if (ownerThread != null) {//如果锁已经被其他线程持有，返回false  TODO 但如果忘记释放锁，会导致死锁
            return false;
        }*/
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource(); //从jedispool的连接池中拿到一个连接
            String id = UUID.randomUUID().toString();//使用一个唯一标识，实际环境中可以使用我们的业务id，如订单id
            SetParams params = new SetParams();
            params.px(LOCK_TIME);//过期时间
            params.nx();//只有key不存在时才设置key
            synchronized (this) {//避免发生抢锁
                if ("OK".equals(jedis.set(lockName, id, params))) {//如果redis返回OK，表示获取到锁
                    ownerThread = t;//设置当前持有锁的线程,用于判支持可重入
                    lockerId.set(id);//用于解锁
                    if (watchDogTask == null) {//看门狗线程启动
                        watchDogTask = new Thread(new WatchDog(), "expireThread");
                        watchDogTask.setDaemon(true);
                        watchDogTask.start();
                    }
                    //往延迟阻塞队列中加入元素（让看门口可以在过期之前一点点的时间去做锁的续期）
                    delayedItems.add(new DelayedItem<>(new LockItem(lockName, id), LOCK_TIME));
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("加锁失败", e);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long start = System.currentTimeMillis();
        while (!tryLock()) {
            if (System.currentTimeMillis() - start > unit.toMillis(time)) {
                return false;
            }
            Thread.sleep(100);
        }
        return true;
    }


    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("不支持等待通知操作！");
    }


    //通过延迟队列避免无畏的轮询
    private static final DelayQueue<DelayedItem<LockItem>> delayedItems = new DelayQueue<>();
    private static final String DELAY_LOCK_LUA = """
                if redis.call('get',KEYS[1]) == ARGV[1]
                   then return redis.call('pexpire',KEYS[1],ARGV[2])
                   else return 0 end
            """;

    /*看门狗线程*/
    private Thread watchDogTask;

    /**
     * Created by penghs at 2023/12/21 16:44
     */
    public static class WatchDog implements Runnable {

        @Override
        public void run() {
            System.out.println("看门狗线程已启动----");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    LockItem lockItem = delayedItems.take().getItem();
                    Jedis jedis = null;
                    try {
                        jedis = jedisPool.getResource();
                        Long result = (Long) jedis.eval(DELAY_LOCK_LUA, Collections.singletonList(lockItem.getKey()), Arrays.asList(lockItem.getValue(), LOCK_TIME_STR));
                        if (result == 0L) {
                            System.out.println("Redis上的锁已释放，无需续期");
                        } else {
                            System.out.println("Redis上的锁已续期，继续加入队列");
                            delayedItems.add(new DelayedItem<>(new LockItem(lockItem.getKey(), lockItem.getValue()), LOCK_TIME));
                        }
                    } finally {
                        if (jedis != null)
                            jedis.close();
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException("看门狗线程被中断", e);
                }
            }
        }
    }
}
