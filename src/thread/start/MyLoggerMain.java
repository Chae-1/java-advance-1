package thread.start;

import static thread.start.MyLogger.log;

public class MyLoggerMain {
    public static void main(String[] args) {
        log("hello thread");
        log(123);
    }
}
