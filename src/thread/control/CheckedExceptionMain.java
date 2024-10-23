package thread.control;

public class CheckedExceptionMain {

    public static void main(String[] args) throws Exception {
        throw new Exception();
    }

    static class CheckedException implements Runnable {
        @Override
        public void run() {
            throw new RuntimeException();
        }
    }
}
