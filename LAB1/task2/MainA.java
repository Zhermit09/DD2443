public class MainA {
    static volatile int sharedInt = 0;

    public static class Incrementer implements Runnable {
        public void run() {
            for (int i = 0; i < 1_000_000; i++) {
                sharedInt++;
            }
        }
    }

    public static class Printer implements Runnable {
        public void run() {
            System.out.println(sharedInt);
        }
    }

    public static void main(String[] args) {
        new Thread(new Incrementer()).start();
        new Thread(new Printer()).start();
    }
}
