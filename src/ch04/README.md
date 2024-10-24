인터럽트
==
```java
public static void main(String[] args) throws InterruptedException {
    MyTask task = new MyTask();
    Thread thread = new Thread(task, "work");

    thread.start();
    Thread.sleep(4000);
        
    task.runFlag = false;
}

static class MyTask implements Runnable {

    volatile boolean runFlag = true;

    @Override
    public void run() {
        while (runFlag) {
            log("작업 중");
            sleep(3000);
        }
        log("자원 정리");
        log("종료");
    }
}
```
- runFlag를 true로 만들어도, work 스레드가 sleep 상태에 존재하기 때문에 몇 초 이후에 작업이 중단된다.
- while 문 안에서 sleep을 하기 때문에 바로 스레드가 종료되지 않는다.
- 이렇게 timed_waiting 상태에 놓인 스레드를 바로 깨우는 방법은 interrupt() 메서드를 호출하는 것이다.

```java
    public static void main(String[] args) throws InterruptedException {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");

        thread.start();
        Thread.sleep(4000);
        log("작업 중단");
        thread.interrupt();
        log("work 스레드 인터럽트 상태1 = " + thread.isInterrupted());
    }

    static class MyTask implements Runnable {

        volatile boolean runFlag = true;

        @Override
        public void run() {
            try {
                while (true) {
                    log("작업 중");
                    Thread.sleep(3000);
                }
            } catch (InterruptedException e) {
                log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted());
                log("interrupt message=" + e.getMessage());
                log("state = " + Thread.currentThread().getState());
            }
            log("자원 정리");
            log("종료");
        }
    }
```    
- WAITING 상태의 스레드를 InterruptedException 예외를 발생시켜 interrupt 시키고 정상 흐름으로 변경할 수 있다.
- 오직 Thread.sleep() 같은 InterruptedException을 발생시키는 메서드를 호출하거나 대기중일 때 예외가 발생한다.
- 인터럽트된 스레드는 RUNNABLE 상태로 변경된다.
- 예외 발생 직후 isInterrupted의 결과를 false로 설정한다.
- 인터럽트가 발생해도 while 플래그가 항상 true이기에 다음 코드로 넘어간다.

```java
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

            while(!Thread.currentThread().isInterrupted()) { // 인터럽트 상태 변경 X
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
```
- 코드가 다음과 같을 때, sleep 호출 시점까지 isInterrupted가 true가 된다.
- 이렇게 되면 sleep 같은 메서드를 호출할 때 그 즉시 인터럽트 예외가 발생하는데 이를 방지하기 위해 isInterrupted 플래그를 정상으로 되돌려야한다.
- 스레드의 인터럽트 상태를 확인하는 용도라면 isInterrupted를 사용해야한다.

**Thread.interrupted()**
- 스레드가 인터럽트 상태라면 true를 반환하고 인터럽트 상태를 false로 변경
- 그렇지 않다면 false를 반환하고 인터럽트 상태를 변경하지 않는다.


#### yield
- 특정 스레드가 다른 스레드에 CPU 실행 기회를 양보할 수 있다.

자바의 스레드가 RUNNABLE 상태에 놓여있을 때, 스케쥴링은 다음과 같은 상태를 갖는다.
- 실행 상태 : 스레드가 CPU에서 실제로 실행 중인 상태
- 실행 대기 상태 : 스레드가 스케쥴링 큐에서 대기하고 있는 상태.

**yield의 동작**
- 현재 실행 중인 스레드가 자발적으로 CPU를 양보하여 다른 스레드가 실행될 수 있도록 한다.
- RUNNABLE 상태를 유지하면서 다른 스레드에게 CPU를 양보한다.
  - 실행 중인 상태거나 대기 상태일 때 다시 스케쥴링 큐에 맨 뒤에 들어간다.

운영체제의 스케줄러에게 힌트를 주는 것이지 강제적인 순서를 지정하지도 않고 반드시 다른 스레드가 실행되는 것도 아니다.
- 양보할 스레드가 존재하지 않으면 계속 실행될 수 있다.

yield는 busy-waiting 상태에 존재하는 반복 로직에서 사용할 때 스레드를 효율적으로 사용할 수 있다.
