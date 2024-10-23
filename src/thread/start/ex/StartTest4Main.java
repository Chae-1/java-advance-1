package thread.start.ex;

import static thread.start.MyLogger.log;

public class StartTest4Main {
    public static void main(String[] args) {
        Thread threadA = new Thread(new PrintWork("A", 1000L), "Thread-A");
        threadA.start();


        Thread threadB = new Thread(new PrintWork("B", 500L), "Thread-B");
        threadB.start();
    }

    static class PrintWork implements Runnable {

        private final String content;
        private final Long sleepMs;

        public PrintWork(String content, Long sleepMs) {
            this.content = content;
            this.sleepMs = sleepMs;
        }

        @Override
        public void run() {
            while(true) {
                log(content);
                try {
                    Thread.sleep(sleepMs);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



    private static Runnable getB(String str, long mills) {
        return () -> {
            while(true) {
                System.out.println(str);
                try {
                    Thread.sleep(mills);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
