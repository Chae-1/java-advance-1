package thread.control.join;

import static thread.start.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV4 {

    public static void main(String[] args) {
        log("Start");

        SumTask task1 = new SumTask(1, 50);
        SumTask task2 = new SumTask(51, 100);

        Thread t1 = new Thread(task1, "thread-1");
        Thread t2 = new Thread(task2, "thread-2");

        t1.start();
        t2.start();


        // t1, t2 스레드가 종료될 때 까지 현재 스레드 대기
        log("join() - main 스레드가 1초 동안 대기한다.");

        try {
            t1.join(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log("task1.result = " + task1.result);
        log("task2.result = " + task2.result);

        int sumAll = task1.result + task2.result;
        log("sumAll = " + sumAll);
        log("End");
    }

    static class SumTask implements Runnable {
        int startValue;
        int endValue;
        int result = 0;

        public SumTask(int startValue, int endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }

        @Override
        public void run() {
            log("작업 시작");
            sleep(2000);
            int sum = 0;
            for (int value = startValue; value <= endValue; value++) {
                sum += value;
            }
            result = sum;
            log("작업 완료 result = " + result);
        }
    }
}
