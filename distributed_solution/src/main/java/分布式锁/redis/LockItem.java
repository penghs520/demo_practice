package 分布式锁.redis;

/**
 * Created by penghs at 2023/12/21 16:43
 */
public class LockItem {
    private final String key;
    private final String value;

    public LockItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
