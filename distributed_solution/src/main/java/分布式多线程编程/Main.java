package 分布式多线程编程;

import com.alibaba.fastjson2.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by penghs at 2023/12/20 23:14
 */
public class Main {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        OrderService orderService = new OrderService();
        orderService.createOrderParallel();
        orderService.createOrderParallel2();

        long start = System.currentTimeMillis();
        orderService.startSchedule();
        for (int i = 0; i < 100; i++) {
            System.out.println("创建订单：" + i);
            Order order = orderService.createOrderBatch("order" + i);
            System.out.println(order.getDelivery().getDeliveryId());
        }
        System.out.println("批量创建100个订单耗时：" + (System.currentTimeMillis() - start));
    }
}
