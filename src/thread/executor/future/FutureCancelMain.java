package thread.executor.future;

import static thread.start.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureCancelMain {

    private static boolean mayInterruptIfRunning = true;

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(1);

        Future<String> future = es.submit(new MyTask());
        log("Future.state: " + future.state());
        sleep(3000);

        // cancel() 호출
        log("future.cancel(" + mayInterruptIfRunning + ") 호출");
        boolean cancelResult = future.cancel(mayInterruptIfRunning);
        log("cancel(" + mayInterruptIfRunning + ") result: " + cancelResult);

        try {
            future.get();
        } catch (CancellationException e) {
            log("Future는 이미 취소 되었습니다.");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        es.close();

    }

    static class MyTask implements Callable<String> {

        @Override
        public String call() {

            try {
                for (int i = 0; i < 10; i++) {
                    log("작업 중: " + i);
                    Thread.sleep(1000); // 1초 동안 sleep
                }
            } catch (InterruptedException e) {
                log("인터럽트 발생");
                return "Interrupted";
            }

            return "Completed";
        }
    }
}
