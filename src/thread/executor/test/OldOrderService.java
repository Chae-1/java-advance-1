package thread.executor.test;

import static thread.start.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class OldOrderService {
    public void order(String orderNo) {

        ExecutorService es = Executors.newFixedThreadPool(3);

        InventoryWork inventoryWork = new InventoryWork(orderNo);
        ShippingWork shippingWork = new ShippingWork(orderNo);
        AccountingWork accountingWork = new AccountingWork(orderNo);

        try {
            List<Future<Boolean>> futures = es.invokeAll(List.of(inventoryWork, shippingWork, accountingWork));
            for (Future<Boolean> future : futures) {
                Boolean workResult = future.get();
                if (!workResult) {
                    log("일부 작업이 실패했습니다.");
                    return ;
                }
            }
            log("모든 주문 처리가 성공적으로 완료되었습니다.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    public void orderV1(String orderNo) {

        ExecutorService es = Executors.newFixedThreadPool(3);

        InventoryWork inventoryWork = new InventoryWork(orderNo);
        ShippingWork shippingWork = new ShippingWork(orderNo);
        AccountingWork accountingWork = new AccountingWork(orderNo);

        try {
            Future<Boolean> inventoryFuture = es.submit(inventoryWork);
            Future<Boolean> shippingFuture = es.submit(shippingWork);
            Future<Boolean> accountingFuture = es.submit(accountingWork);

            Boolean inventoryResult = inventoryFuture.get();
            Boolean shippingResult = shippingFuture.get();
            Boolean accountingResult = accountingFuture.get();

            if (inventoryResult && shippingResult && accountingResult) {
                log("모든 주문 처리가 성공적으로 완료되었습니다.");
                return ;
            }
            log("일부 작업이 실패했습니다.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    static class InventoryWork implements Callable<Boolean> {
        private final String orderNo;

        public InventoryWork(String orderNo) {
            this.orderNo = orderNo;
        }

        public Boolean call() {
            log("재고 업데이트: " + orderNo);
            sleep(1000);
            return true;
        }
    }

    static class ShippingWork implements Callable<Boolean> {
        private final String orderNo;

        public ShippingWork(String orderNo) {
            this.orderNo = orderNo;
        }

        public Boolean call() {
            log("배송 시스템 알림: " + orderNo);
            sleep(1000);
            return true;
        }
    }

    static class AccountingWork implements Callable<Boolean> {
        private final String orderNo;

        public AccountingWork(String orderNo) {
            this.orderNo = orderNo;
        }

        public Boolean call() {
            log("회계 시스템 업데이트: " + orderNo);
            sleep(1000);
            return true;
        }
    }
}
