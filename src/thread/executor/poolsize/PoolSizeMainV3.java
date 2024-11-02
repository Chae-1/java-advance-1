package thread.executor.poolsize;

import static thread.executor.ExecutorUtils.printState;
import static thread.start.MyLogger.log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import thread.executor.RunnableTask;

public class PoolSizeMainV3 {

    public static void main(String[] args) {
        ExecutorService es = Executors.newCachedThreadPool();
        //

        log("pool 생성");
        printState(es);

        for (int i = 1; i <= 6; i++) {
            String taskName = "task" + i;
            es.execute(new RunnableTask(taskName));
            printState(es, taskName);
        }

        es.close();
        log("== shutdown 완료 ==");
    }
}
