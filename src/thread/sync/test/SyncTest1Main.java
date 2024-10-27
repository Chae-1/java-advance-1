package thread.sync.test;

public class SyncTest1Main {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    counter.increment();
                }
            }
        };
        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("결과: " + counter.getCount());
    }

    static class Counter {
        private int count = 0; // 공유 자원
        // 20,000이 되지 못하는 이유
        // -> 동시에 여러 스레드가 자원에 접근하여 수정
        // -> 스레드 하나가 수정 작업을 완료했을 때, 다른 스레드가 진입하여 작업을 수행하도록 변경
        // -> 동기화가 필요하다.
        public void increment() {
            synchronized (this) {
                count = count + 1;
            }
        }

        public synchronized int getCount() {
            return count;
        }
    }
}
