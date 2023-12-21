package 分布式多线程编程;

import java.util.List;

/**
 * Created by penghs at 2023/12/20 23:16
 */
public class DeliveryService {


    public Delivery dealDelivery(Order order) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new Delivery("delivery001");
    }


    public List<Delivery> dealDeliveryBatch(List<Order> orders) {
        try {
            Thread.sleep(10L * orders.size());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Delivery> deliveries = new java.util.ArrayList<>();
        for (Order order : orders) {
            deliveries.add(new Delivery(order.getOrderId()));
        }
        return deliveries;
    }
}
