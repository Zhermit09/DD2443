public class MainC {
    static volatile int sharedInt = 0;
    static volatile boolean done = false;

    public static class Incrementer implements Runnable {
        public void run() {
            for (int i = 0; i < 1_000_000; i++) {
                sharedInt++;
            }
            synchronized (MainC.class) {
                done = true;
                MainC.class.notify();
            }
        }
    }

    public static class Printer implements Runnable {
        public void run() {
            synchronized (MainC.class) {
                while (!done) {
                    try {
                        MainC.class.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            System.out.println(sharedInt);

        }
    }

    public static void main(String[] args) {
        new Thread(new Incrementer()).start();
        new Thread(new Printer()).start();
    }
}
