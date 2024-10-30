package thread.cas.spinlock;

import static thread.start.MyLogger.log;
import static util.ThreadUtils.sleep;

public class SpinLockBad {

    private volatile boolean lock = false;

    public void lock() {
        log("락 획득 시도");
        while (true) {
            // 여러 스레드 중 하나의 스레드만 락을 획득할 수 있다.

            // 1. 락 사용여부 확인
            // false인 경우 lock을 사용하고 있지 않다.
            // true인 경우 lock을 사용하고 있다.
            if (!lock) {
                sleep(100);
                lock = true; // 2. 락의 값 변경
                break;
            } else {
                // 락을 획득할 때 까지 스핀 대기한다.
                log("락 획득 실패 - 스핀 대기");
            }
        }
        log("락 획득 완료");
    }

    public void unlock() {
        lock = false;
        log("락 반납 완료");
    }
}
