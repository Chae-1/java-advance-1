package thread.control;

import static thread.start.MyLogger.log;

public class ThreadStateMain {

    public static void main(String[] args) {
        try {
            Thread thread = new Thread(new MyRunnable(), "myThread");
            log("myThread.state1 = " + thread.getState());
            log("myThread.start()");
            thread.start();
            Thread.sleep(1000);
            log("myThread.state3 = " + thread.getState());
            Thread.sleep(4000);
            log("myThread.state5 = " + thread.getState()); // TERMINATED
            log("end");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static class MyRunnable implements Runnable {

        @Override
        public void run() {
            try {
                log("myThread.state2 = " + Thread.currentThread().getState());
                log("sleep() start");
                Thread.sleep(3_000);
                log("sleep() end");
                log("myThread.state4 = " + Thread.currentThread().getState());
                log("end");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
