concurrent.Lock
==
**synchronized 단점**
- **무한 대기:** 락이 풀릴 때 까지 무한 대기한다.
- **공정성:** 락이 돌아왔을 때 BLOCKED 상태의 여러 스레드 중에 어떤 스레드가 락을 획득할 지 알 수 없다.
  - 최악의 경우 오랜 시간동안 락을 획득하지 못하는 스레드가 존재할 수 있다.

이런 단점을 극복하기 위해 java.util.concurrent 라이브러리 패키지가 추가되었다.

## LockSupport
LockSupport는 스레드를 WAITING 상태로 변경한다.

RUNNABLE -> WAITING
- 누가 깨워주기 전까지는 계속 대기한다.
- park(), unpark(Thread thread)를 갖는다.
- unpark()가 아닌 interrupt()로도 WAITING 상태의 스레드를 깨울 수 있다. 


**시간 대기**
- parkNanos(nanos)
- 지정한 나노초 만큼 스레드를 일시정지 시킨다. TIMED_WAITING 상태로 변경된다.

**BLOCKED vs WAITING**
- 인터럽트 시
  - BLOCKED는 인터럽트가 걸려도 대기 상태를 빠져나오지 못하고 여전히 BLOCKED
  - WAITING은 인터럽트를 걸면 대기상태에서 벗어나 RUNNABLE 상태로 변경된다.

**용도**
- BLOCKED 상태는 LOCK을 획득하기 위해 대기할 때 사용된다.
- WAITING, TIMED_WAITING 상태는 스레드가 특정 조건, 시간 동안 대기할 때 발생하는 상태.
- WAITING 상태는 다양한 상황에서 사용됨.
  - Thread.join, sleep, wait, LockSupport.park() 호출 시 WAITING 상태가 된다.

synchronized 동기화 방식의 단점을 해결하기 위해 LockSupport를 활용한 Lock 기능을 제공하는데 바로 ReentrantLock 이다.

## ReentrantLock
자바 1.5부터 Lock 인터페이스와 ReentrantLock 구현체를 통해, synchronized 키워드의 임계 영역 관리의 단점을 보완했다.
```java
public interface Lock {
   void lock();
   void lockInterruptibly() throws InterruptedException;
   boolean tryLock();
   boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
   void unlock();
   Condition newCondition();
}
```
- void lock()
  - 락을 획득한다. 다른 스레드가 이미 락을 획득했다면 WAITING 상태로 대기하고 interrupt로 인해 RUNNABLE 상태로 변경되지 않는다.
- void lockInterruptibly()
  - 락을 획득시도를 하지만, lock()과 달리 다른 스레드가 인터럽트할 수 있도록 한다.
  - 대기 중에 인터럽트 발생 시, InterruptedException이 발생하며 락 획득을 포기한다.
- boolean tryLock()
  - 락 획득을 시도하고, 즉시 성공여부를 반환한다.
  - 락 획득 성공 시 true, 그렇지 않으면 false.
- boolean tryLock(long time, TimeUnit unit)
  - 주어진 시간 동안 락 획득을 시도한다.
  - 주어진 시간 안에 락을 획득하면 true를 반환한다.
- void unlock()
  - 락을 해제한다. 락을 해제하면 락 획득을 대기 중인 스레드 중 하나가 락을 획득할 수 있게 된다.
  - 락을 획득할 스레드가 호출해야하고, 그렇지 않으면 IllegalMonitorStateException이 발생할 수 있음.
- Condition newCondition()
  - Condition 객체를 생성하여 반환한다.

Lock 구현체를 통해 스레드가 무한정 대기하는 상태 놓이는 상황을 방지할 수 있다.

공정성 문제를 해결하기 위해서 스레드가 공정하게 락을 얻을 수 있는 모드를 제공하는 ReentrantLock 클래스가 존재한다.

#### 비공정 모드 (non-fair mode)
- ReentrantLock의 기본 모드.
- 락을 먼저 요청한 스레드가 락을 먼저 획득한다는 보장이 없음.
- 대기 중인 스레드 중 아무나 락을 획득할 수 있다.

**특징**
- 락을 획득하는 속도가 빠름.
- 새로운 스레드가 기존 대기 스레드보다 락을 먼저 획득할 수 있다.
- 특정 스레드가 락을 획득하지 못하는 기아 현상이 발생할 수 있다.

#### 공정 모드 (fair mode)
- 락을 요청한 순서대로 스레드가 락을 획득할 수 있게 한다. 스레드간의 공정성을 보장한다.
- 모든 스레드가 언젠가 락을 획득할 수 있게 보장된다.
- 획득하는 속도가 느리다.

모니터 락과 BLOCKED 상태는 synchronized 상태에서만 사용된다.

ReentrantLock은 별개의 매커니즘으로 Lock을 제공한다.
- 내부적으로 LockSupport를 사용하기 때문에 lock을 대기하는 스레드는 WAITING 상태로 변경된다.
- Waiting 상태의 스레드는 내부의 대기 큐에서 대기하며 lock 획득을 기다린다. 이후 모드에 따라 동작한다.

