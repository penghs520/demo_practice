package 分布式多线程编程;

/**
 * Created by penghs at 2023/12/20 23:16
 */
public class GoodService {
    public Good dealGood(Order order) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new Good("good001", "商品001");
    }
}
