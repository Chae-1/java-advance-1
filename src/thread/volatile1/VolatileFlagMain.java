package thread.volatile1;

import static thread.start.MyLogger.log;
import static util.ThreadUtils.sleep;

public class VolatileFlagMain {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread t = new Thread(task, "work");
        log("runFlag = " + task.runFlag);
        t.start();
        
        sleep(1000);
        log("runFlag를 false로 변경");
        task.runFlag = false;
        log("runFlag = " + task.runFlag);
        log("main 종료");
    }

    static class MyTask implements Runnable {

        boolean runFlag = true;

        @Override
        public void run() {
            log("task 시작");
            while (runFlag) {
                
            }
            log("task 종료");
        }
    }
}
