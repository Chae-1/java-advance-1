package thread.start.ex;

import static thread.start.MyLogger.log;

public class StartTest1Main {
    public static void main(String[] args) {
        new CounterThread().start();
    }

    static class CounterThread extends Thread {
        @Override
        public void run() {

            for (int i = 1; i <= 5; i++) {
                log("value: " + i);
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
