volatile, 메모리 가시성
==

```java
 public class VolatileFlagMain {
    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread t = new Thread(task, "work");
        log("runFlag = " + task.runFlag);
        t.start();
        sleep(1000);

        log("runFlag를 false로 변경 시도");
        task.runFlag = false;
        log("runFlag = " + task.runFlag);
        log("main 종료");
    }


    static class MyTask implements Runnable {
        boolean runFlag = true;

        //volatile boolean runFlag = true;
        @Override
        public void run() {
            log("task 시작");
            while (runFlag) {
                // runFlag가 false로 변하면 탈출
            }
            log("task 종료");
        }
    }
}
```
- 멀티스레드 환경에서 volatile 키워드를 사용하지 않은 변수를 선언하고 다른 스레드가 해당 변수를 변경하면 의도대로 동작하지 않는다.
- main 스레드에서 work 스레드가 실행 중인 work runFlag의 값을 false로 변경했지만 무한 루프를 탈출하지 못한다.

이는 메모리 가시성으로 인해 발생하는 문제이다.

#### 메모리 가시성 문제
- memory visibility
- 각 CPU 코어는 개별 캐시 메모리를 갖는다. 
- 데이터를 메인 메모리에서 조회하지 않고 우선 캐시 메모리에 있는 데이터를 조회하여 사용한다.
- 이 때문에 메모리 가시성 문제가 발생한다.
  - 변수를 변경한다는 것은 캐시 메모리에 있는 데이터를 변경하는 것이다. 이를 메인 메모리에 반영하는 정확한 시점은 알 수 없다.

요약하자면 멀티스레드 환경에서 한 스레드가 변경한 값을 다른 스레드에서 언제 조회가 가능한지에 대한 문제를 메모리 가시성문제라고 한다.

java에서는 volatile 키워드를 사용하여 캐시 메모리가 아니라 항상 메인 메모리에서 값을 조회하도록 지정할 수 있다.


## 자바 메모리 모델

**Java Memory Model**

Java Memory Model은 자바 프로그램이 어떻게 메모리에 접근하고 수정할 수 있는지를 규정하며, 멀티스레드 프로그래밍에서 스레드간의 상호작용을 정의.

**happens-before**
- 자바 메모리 모델에서 스레드 간의 작업 순서를 정의하는 개념.
- 만약 A 작업이 B 작업보다 happens-before 관계에 있다면, A 작업에서의 모든 메모리 변경 사항은 B 작업에서 볼 수 있다.
- B 작업이 시작되기 전에 모두 메모리에 반영된다.

**규칙**
- 말 그대로 한 동작이 다른 동작보다 먼저 발생함을 보장한다.
- 메모리 가시성을 보장하는 규칙
- happens-before 관계가 성립하면, 한 스레드의 작업을 다른 스레드에서 볼 수 있게 된다.
- 즉, 한 스레드가 수행할 작업을 다른 스레드가 참조할 때 최신 상태가 보장되는 것.

volatile 또는 스레드 동기화 기법을 사용하면 메모리 가시성의 문제가 발생하지 않는다.