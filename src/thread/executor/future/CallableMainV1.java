package thread.executor.future;

import static thread.start.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableMainV1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyCallable myCallable = new MyCallable();

        ExecutorService es = Executors.newFixedThreadPool(2);
        //
        Future<Integer> submit = es.submit(myCallable);
        Integer result = submit.get();
        log("result value = " + result);
        es.close();
    }

    static class MyCallable implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {

            log("Runnable 시작");
            sleep(2000);
            int value = new Random().nextInt(10);
            log("create value = " + value);
            log("Runnable 완료");
            return value;
        }
    }
}
