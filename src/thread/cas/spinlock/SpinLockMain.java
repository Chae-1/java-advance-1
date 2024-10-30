package thread.cas.spinlock;

import static thread.start.MyLogger.log;
import static util.ThreadUtils.sleep;

public class SpinLockMain {
    public static void main(String[] args) {
        SpinLock spinLockBad = new SpinLock();

        Runnable task = () -> {
            spinLockBad.lock();
            try {
                //critical section
                log("비즈니스 로직 실행");
                sleep(1);
            } finally {
                spinLockBad.unlock();
            }
        };
        new Thread(task).start();
        new Thread(task).start();
    }
}
