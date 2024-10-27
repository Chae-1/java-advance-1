package thread.sync.lock;

import static thread.start.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.concurrent.locks.LockSupport;

public class LockSupportMainV1 {
    public static void main(String[] args) {
        Thread t1 = new Thread(new ParkTest());
        t1.start();

        sleep(100);
        log("Thread-1 state: " + t1.getState());
        log("main -> unpark(Thread-1)");
        LockSupport.unpark(t1);
//        t1.interrupt();
        // 잠시 대기하여 Thread-1이 park 상태에 빠질 시간을 준다.
        log("Thread-1 state: " + t1.getState());

    }

    static class ParkTest implements Runnable {

        @Override
        public void run() {
            log("park 시작");
            LockSupport.park();
            log("park 종료, state: " + Thread.currentThread().getState());
            log("인터럽트 상태: " + Thread.currentThread().isInterrupted());
        }
    }
}