package 分布式多线程编程;

/**
 * Created by penghs at 2023/12/20 23:16
 */
public class Delivery {
    private final String deliveryId;

    public Delivery(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getDeliveryId() {
        return deliveryId;
    }
}
