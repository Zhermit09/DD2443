public class MainA {
    public static class Incrementer implements Runnable {
        private static volatile int count = 0;

        private static void increment() {
            count++;
        }

        static public void printCount() {
            System.out.println(count);
        }

        public void run() {
            for (int i = 0; i < 1_000_000; i++) {
                increment();
            }
        }
    }

    public static void main(String[] args) {
        Thread[] ts = new Thread[4];

        for (int i = 0; i < ts.length; i++) {
            ts[i] = new Thread(new Incrementer());
            ts[i].start();
        }

        for (Thread t : ts) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.err.println("Interrupted at join");
            }
        }
        Incrementer.printCount();
    }
}
