package thread.collection.simple;

import static thread.start.MyLogger.log;

public class SimpleListMainV2 {
    public static void main(String[] args) throws InterruptedException {
//        test(new BasicList());
        test(new SyncList());
    }

    public static void test(SimpleList list) throws InterruptedException {
        Runnable addA = () -> {
            list.add("A");
            log("Thread-1: list.add(A)");
        };

        Runnable addB = () -> {
            list.add("B");
            log("Thread-2: list.add(B)");
        };

        Thread threadA = new Thread(addA, "Thread-1");
        Thread threadB = new Thread(addB, "Thread-2");

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();
        log(list);
    }
}
