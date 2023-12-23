package 分布式锁.redis;

import java.util.concurrent.CountDownLatch;

/**
 * Created by penghs at 2023/12/21 16:45
 */
public class Main {
    public static void main(String[] args) {
        RedisDistLock lock = new RedisDistLock("lock");
        CountDownLatch latch = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    lock.lock();
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                    latch.countDown();
                }
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
