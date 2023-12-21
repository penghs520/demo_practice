package 分布式多线程编程;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by penghs at 2023/12/20 23:14
 */
public class OrderService {

    private final GoodService goodService = new GoodService();
    private final DeliveryService deliveryService = new DeliveryService();

    public Order createOrder() {
        long start = System.currentTimeMillis();
        Order order = new Order("order001");
        Good good = goodService.dealGood(order);//模拟远程调用
        Delivery delivery = deliveryService.dealDelivery(order);//模拟远程调用
        order.setGood(good);
        order.setDelivery(delivery);
        System.out.println("串行模式创建订单耗时：" + (System.currentTimeMillis() - start));
        return order;
    }

    public Order createOrderParallel() {
        long start = System.currentTimeMillis();
        Order order = new Order("order001");
        Thread goodThread = new Thread(() -> {
            Good good = goodService.dealGood(order);
            order.setGood(good);
        });
        Thread deliveryThread = new Thread(() -> {
            Delivery delivery = deliveryService.dealDelivery(order);
            order.setDelivery(delivery);
        });
        goodThread.start();
        deliveryThread.start();
        try {
            goodThread.join();
            deliveryThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("并行模式创建订单耗时：" + (System.currentTimeMillis() - start));
        return order;
    }

    public Order createOrderParallel2() {
        long start = System.currentTimeMillis();
        Order order = new Order("order001");
        FutureTask<Good> dealGoodTask = new FutureTask<>(() -> goodService.dealGood(order));
        FutureTask<Delivery> dealDeliveryTask = new FutureTask<>(() -> deliveryService.dealDelivery(order));
        new Thread(dealGoodTask).start();
        new Thread(dealDeliveryTask).start();
        try {
            order.setGood(dealGoodTask.get());
            order.setDelivery(dealDeliveryTask.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("并行模式创建订单耗时：" + (System.currentTimeMillis() - start));
        return order;
    }

    /**
     * 为了支撑高并发，当生成订单后，我们写MQ，定时任务每20秒扫描一次MQ，如果有订单，就批量去调用商品服务和物流服务，得到批量结果，然后进行数据的分发以更新订单状态
     */
    public Order createOrderBatch(String orderId) throws ExecutionException, InterruptedException {
        Order order = new Order(orderId);
        DealDeliveryRequest dealDeliveryRequest = new DealDeliveryRequest();
        dealDeliveryRequest.order = order;
        dealDeliveryRequest.future = new CompletableFuture<>();
        dealDeliveryMQ.add(dealDeliveryRequest);
        order.setDelivery(dealDeliveryRequest.future.get());
        return order;
    }

    public void startSchedule() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            int size = dealDeliveryMQ.size();
            if (size == 0) {
                return;
            }
            if (size > 1000) {
                size = 1000;//批次最大1000
            }
            List<DealDeliveryRequest> dealDeliveryRequests = new ArrayList<>();
            List<Order> orders = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                DealDeliveryRequest dealDeliveryRequest = dealDeliveryMQ.poll();
                orders.add(dealDeliveryRequest.getOrder());
                dealDeliveryRequests.add(dealDeliveryRequest);
            }
            List<Delivery> deliveries = deliveryService.dealDeliveryBatch(orders);
            for (int i = 0; i < deliveries.size(); i++) {
                DealDeliveryRequest dealDeliveryRequest = dealDeliveryRequests.get(i);
                dealDeliveryRequest.future.complete(deliveries.get(i));
            }
        }, 0, 50, TimeUnit.MICROSECONDS);
    }

    class DealDeliveryRequest {
        Order order;
        CompletableFuture<Delivery> future;

        public Order getOrder() {
            return order;
        }
    }

    LinkedBlockingDeque<DealDeliveryRequest> dealDeliveryMQ = new LinkedBlockingDeque<>();
}
