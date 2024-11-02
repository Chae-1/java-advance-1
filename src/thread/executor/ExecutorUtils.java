package thread.executor;

import static thread.start.MyLogger.log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class ExecutorUtils {

    public static void printState(ExecutorService executorService) {

        if (executorService instanceof ThreadPoolExecutor poolExecutor) {
            int pool = poolExecutor.getPoolSize();
            int activeCount = poolExecutor.getActiveCount();
            int queued = poolExecutor.getQueue().size();
            long completedTask = poolExecutor.getCompletedTaskCount();
            log("[pool=" + pool + ", active=" + activeCount + ", queued=" + queued + ", completedTask=" + completedTask
                    + ", queuedTask=" + queued + ", completedTask=" + completedTask + "]");
        } else {
            log(executorService);
        }
    }

    public static void printState(ExecutorService executorService, String taskName) {

        if (executorService instanceof ThreadPoolExecutor poolExecutor) {
            int pool = poolExecutor.getPoolSize();
            int activeCount = poolExecutor.getActiveCount();
            int queued = poolExecutor.getQueue().size();
            long completedTask = poolExecutor.getCompletedTaskCount();
            log(taskName + " -> [pool=" + pool + ", active=" + activeCount + ", queued=" + queued + ", completedTask=" + completedTask
                    + ", queuedTask=" + queued + ", completedTask=" + completedTask + "]");
        } else {
            log(executorService);
        }
    }
}