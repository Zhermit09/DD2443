public class MainB {
	static volatile int sharedInt = 0;
    static volatile boolean done = false;

    public static class Incrementer implements Runnable {
        public void run() {
            for (int i = 0; i < 1_000_000; i++) {
                sharedInt++;
            }
            done = true;
        }
    }

    public static class Printer implements Runnable {
        public void run() {
            while (!done) {
            }
            System.out.println(sharedInt);

        }
    }

    public static void main(String[] args) {
        new Thread(new Incrementer()).start();
        new Thread(new Printer()).start();
    }
}
