package 分布式多线程编程;

/**
 * Created by penghs at 2023/12/20 23:15
 */
public class Order {

    private final String orderId;
    private Good good;
    private Delivery delivery;

    public Order(String orderId) {
        this.orderId = orderId;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public Good getGood() {
        return good;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void setGood(Good good) {
        this.good = good;
    }

}
