import java.util.Arrays;

public class MainC {
    static final int X = 10;
    static final int Y = 100;


    public static class Incrementer implements Runnable {
        private static volatile int count = 0;

        private static synchronized void increment() {
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

    static long run_experiments(int n) {
        long startTime = System.nanoTime();
        Thread[] ts = new Thread[n];

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
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        System.out.println("\t\tμ\t\tσ");

        long[] times = new long[Y];

        //Warmup
        for (int i = 0; i < X; i++) {
            run_experiments(n);
        }

        //Main run
        for (int i = 0; i < Y; i++) {
            times[i] = run_experiments(n);
        }

        //Metrics
        double u = Arrays.stream(times).sum() / (double) Y;
        double o = Math.sqrt(Arrays.stream(times).mapToDouble(x -> (u - x) * (u - x)).sum() / (float) Y);
        System.out.printf("#%d\t%.3f\t%.3f\n", n, u, o);
    }
}
