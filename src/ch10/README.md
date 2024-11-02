동시성 컬렉션
==
여러 스레드가 동시에 접근해도 괜찮은 경우를 **스레드 세이프**하다고 한다.

자바에서 제공하는 컬렉션 프레임워크에서 사용하는 연산은 원자적이지 않다.

그래서 동시성 문제가 발생하고 동기화가 필요하다.

### 컬렉션 프레임워크의 대부분은 스레드 세이프하지 않다.
ArrayList, LinkedList, HashSet, HashMap 등 수 많은 자료구조들 안에는 수많은 연산들이 함께 사용하는데 이 모든 연산이 CAS 연산이 아니다.

### 동시성 컬렉션 - 프록시
기존 컬렉션 프레임워크를 모두 복사해서 synchronized 기능을 추가하는 것이 아닌 **프록시** 객체를 사용하여 멀티스레드 상황에 동기화가 필요할 때만 synchronized 기능을 추가할 수 있다.

#### 프록시
실제 호출해야하는 대상 클래스를 대신 호출해주는 호출자 클래스를 프록시 객체라고 한다.

기존 코드에 부가 기능을 추가하거나 접근 제어를 추가하고 싶을 때 주로 사용하는 객체이다.

```java
public class SyncProxyList implements SimpleList {

    private SimpleList target;

    public SyncProxyList(SimpleList target) {
        this.target = target;
    }

    @Override
    public synchronized int size() {
        return target.size();
    }

    @Override
    public synchronized void add(Object e) {
        target.add(e);
    }

    @Override
    public synchronized Object get(int index) {
        return target.get(index);
    }
}

```
- 인터페이스를 구현했기 때문에 SimpleList 인터페이스를 사용하는 클라이언트 코드의 수정 없이 구현체를 주입할 수 있다.
  - 추상화에 의존한다.
- SyncProxyList는 target으로 주입받은 SimpleList 인터페이스의 구현체 중 하나를 호출하는 대리자 역할을 수행한다.
  - 이 진입점에서 동기화를 수행한다.
  - 이로 인해 기존 코드를 수정하지 않고도 기존 컬렉션 프레임워크에 동기화 기능을 추가할 수 있다.
- 이처럼 어떤 객체에 대한 접근을 제어하는 대리인 또는 인터페이스 역할을 하는 객체를 제공해주는 디자인 패턴을 **프록시 패턴**이라고한다. 
- 프록시 패턴의 이점은 다음과 같다.
  - **접근 제어:** 실제 객체에 대한 접근을 제한하거나 통제할 수 있다.
  - **성능 향상:** 실제 객체의 생성을 지연시키거나 캐싱하여 성능을 최적화할 수 있다.
  - **부가 기능 제공:** 실제 객체에 추가적인 기능을 투명하게 제공할 수 있다.

## 동시성 컬렉션 1 - synchronized
자바에서는 필요할 경우 synchronized가 적용된 컬렉션 프레임워크를 반환하는 유틸리티 메서드를 제공한다.
```java
public class SynchronizedListMain {
    public static void main(String[] args) {
        List<String> list = Collections.synchronizedList(new ArrayList<>());
        list.add("data1");
        list.add("data2");
        list.add("data3");
        System.out.println(list.getClass());
        System.out.println("list = " + list);
    }
}
```
- Collections.synchronizedList(target)
  - 인자로 전달한 list를 target으로 가지는 SynchronizedRandomAccessList를 반환한다.
  - synchronized를 추가하는 프록시 역할을 수행한다.
  - 이외에도 Collections 유틸리티 클래스에 다양한 synchronized 동기화 메서드를 지원한다.
- 매우 편하게 동기화를 적용할 수 있지만 다음과 같은 단점들이 존재한다.
  - 동기화 오버헤드 발생
  - 전체 컬렉션에 대한 동기화가 이뤄지기 때문에, 잠금 범위가 넓어서 생기는 잠금 경합을 증가시킨다.
  - 정교한 동기화 불가능.
- 이로 인해 성능적인 이점이 존재하지 않아 동시성 컬렉션(concurrent collection)을 사용한다.

## 동시성 컬렉션
자바는 스레드 안전한 컬렉션 자료구조를 제공하기 위해 동시성 컬렉션을 제공한다. 대표적인 동시성 컬렉션은 다음과 같다.
- ConcurrentHashMap
- CopyOnWriteArrayList
- BlockingQueue

### 종류
- **List**
  - CopyOnWriteArrayList: ArrayList의 대안
- **Set**
  - CopyOnWriteArraySet: HashSet의 대안
  - ConcurrentSkipLinkedSet: TreeSet의 대안(정렬된 순서를 유지)
- **Map**
  - ConcurrentHashMap: HashMap의 대안
  - ConcurrentSkipListMap: TreeMap의 대안
- **Queue**
  - ConcurrentLinkedQueue: 동시성 큐, Non-blocking 큐이다.
- **Deque**
  - ConcurrentLinkedDeque: 동시성 덱, 비 차단 덱이다.
- **BlockingQueue**
  - ArrayBlockingQueue
    - 크기가 고정된 블로킹 큐
    - fair 모드를 사용할 수 있다.
  - LinkedBlockingQueue
    - 크기가 무한하거나 고정된 블로킹 큐
  - PriorityBlockingQueue
    - 우선순위가 높은 요소를 먼저 처리한다.
  - SynchronousQueue
    - 데이터를 저장하지 않는 블로킹 큐.
    - 생산자가 데이터를 추가하면 소비자가 그 데이터를 받을 때까지 대기한다.
  - DelayQueue
    - 지연된 요소를 처리하는 블로킹 큐.



