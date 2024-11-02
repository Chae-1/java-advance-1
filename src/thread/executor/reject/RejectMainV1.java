package thread.executor.reject;

import static thread.start.MyLogger.log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import thread.executor.RunnableTask;

public class RejectMainV1 {

    public static void main(String[] args) {
        ExecutorService es = new ThreadPoolExecutor(1, 1,
                0, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(),
                new AbortPolicy());

        es.submit(new RunnableTask("task1"));
        try {
            es.submit(new RunnableTask("task2"));
        } catch (RejectedExecutionException e) {
            log("요청 초과");
            log(e);
        }
        es.close();
    }
}
