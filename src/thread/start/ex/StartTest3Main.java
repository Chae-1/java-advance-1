package thread.start.ex;

import static thread.start.MyLogger.log;

public class StartTest3Main {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i < 5; i++) {
                    log("value: " + i);
                    try {
                        Thread.sleep(1_000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "counter").start();
    }
}
