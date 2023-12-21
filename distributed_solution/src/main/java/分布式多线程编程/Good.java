package 分布式多线程编程;

/**
 * Created by penghs at 2023/12/20 23:15
 */
public class Good {

    private final String goodId;
    private final String goodName;

    public Good(String goodId, String goodName) {
        this.goodId = goodId;
        this.goodName = goodName;
    }

    public String getGoodId() {
        return goodId;
    }

    public String getGoodName() {
        return goodName;
    }
}
