package thread.cas.increment;

import static util.ThreadUtils.sleep;

import java.util.ArrayList;
import java.util.List;

public class IncrementThreadMain {

    public static final int THREAD_COUNT = 1000;

    public static void main(String[] args) throws InterruptedException {
        test(new BasicInteger());
        test(new VolatileInteger());
        test(new SyncInteger());
        test(new MyAtomicInteger());
    }

    private static void test(IncrementInteger incrementInteger) throws InterruptedException {
        long startTimeMs = System.currentTimeMillis();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                sleep(10); // 다른 스레드와 동시 실행을 위해 잠깐 쉬었다가 수행
                incrementInteger.increment();
            }
        };

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread =  new Thread(runnable);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        long endTimeMs = System.currentTimeMillis();
        System.out.println(incrementInteger.getClass().getSimpleName() + " result : " + incrementInteger.get() + ", spent time : " + (endTimeMs - startTimeMs));

    }
}
