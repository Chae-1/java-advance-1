package thread.control.interrupt;

import static thread.start.MyLogger.log;

public class ThreadStopMainV4 {
    public static void main(String[] args) throws InterruptedException {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");

        thread.start();
        Thread.sleep(500);
        log("작업 중단");
        thread.interrupt();
        log("work 스레드 인터럽트 상태1 = " + thread.isInterrupted());
    }

    static class MyTask implements Runnable {

        volatile boolean runFlag = true;

        @Override
        public void run() {

            while(!Thread.interrupted()) { // 인터럽트 상태 변경 X
                log("작업 중");
            }

            log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted());
            log("state = " + Thread.currentThread().getState());

            try {
                log("자원 정리");
                Thread.sleep(1000);
                log("종료");

            } catch (InterruptedException e) {
                log("자원 정리 실패 - 인터럽트 발생");
                log("work 스레드 인터럽트 상태3 = " + Thread.currentThread().isInterrupted());
            }

        }
    }
}