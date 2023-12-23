package 分布式锁.redis;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by penghs at 2023/12/21 16:40
 */
public class DelayedItem<T> implements Delayed {

    private final T item;

    private final long expireTime;

    public DelayedItem(T item, long expireTime) {
        this.item = item;
        this.expireTime = expireTime;
    }

    /**
     * 返回元素到激活时刻的剩余时长
     */
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expireTime
                - System.currentTimeMillis(), unit);
    }

    /**按剩余时长排序*/
    public int compareTo(Delayed o) {
        return (int) (getDelay(TimeUnit.MILLISECONDS)
                - o.getDelay(TimeUnit.MILLISECONDS));
    }

    public T getItem() {
        return item;
    }

    public static void main(String[] args) throws InterruptedException {
        final DelayQueue<DelayedItem<LockItem>> delayedItems = new DelayQueue<>();
        for (int i = 0; i < 100; i++) {
            delayedItems.add(new DelayedItem<>(new LockItem(i + "", i + ""), 1000));
        }
        DelayedItem<LockItem> a1 = delayedItems.take();
        System.out.println(a1.getItem().getValue());
        DelayedItem<LockItem> a2 = delayedItems.take();
        System.out.println(a2.getItem().getValue());
    }
}
