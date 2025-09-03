import java.util.Arrays;

public class MainD {
    static volatile int sharedInt = 0;
    static volatile boolean done = false;

    static final int X = 100;
    static final int Y = 1000;

    static volatile long startTime;
    static volatile long endTime;

    public static class IncrementerB implements Runnable {
        public void run() {
            for (int i = 0; i < 1_000_000; i++) {
                sharedInt++;
            }
            startTime = System.nanoTime();
            done = true;
        }
    }

    public static class PrinterB implements Runnable {
        public void run() {
            while (!done) {
            }
            endTime = System.nanoTime();
            //System.out.println(sharedInt);
        }
    }

    public static class IncrementerC implements Runnable {
        public void run() {
            for (int i = 0; i < 1_000_000; i++) {
                sharedInt++;
            }
            synchronized (MainD.class) {
                done = true;
                startTime = System.nanoTime();
                MainD.class.notify();
            }
        }
    }

    public static class PrinterC implements Runnable {
        public void run() {
            synchronized (MainD.class) {
                while (!done) {
                    try {
                        MainD.class.wait();
                        endTime = System.nanoTime();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            //System.out.println(sharedInt);

        }
    }

    public static long iterB() throws InterruptedException {
        var incB = new Thread(new IncrementerB());
        var prB = new Thread(new PrinterB());

        incB.start();
        prB.start();

        incB.join();
        prB.join();

        sharedInt = 0;
        done = false;

        return endTime - startTime;
    }

    public static long iterC() throws InterruptedException {
        var incC = new Thread(new IncrementerC());
        var prC = new Thread(new PrinterC());

        incC.start();
        prC.start();

        incC.join();
        prC.join();

        sharedInt = 0;
        done = false;

        return endTime - startTime;
    }

    public static void main(String[] args) throws InterruptedException {
        long[] timesB = new long[Y];
        long[] timesC = new long[Y];

        for (int i = 0; i < X; i++) {
            iterB();
        }
        for (int i = 0; i < Y; i++) {
            timesB[i] = iterB();
        }



        for (int i = 0; i < X; i++) {
            iterC();
        }
        for (int i = 0; i < Y; i++) {
            timesC[i] = iterC();
        }



        //Metrics
        double uB = Arrays.stream(timesB).sum() / (double) Y;
        double uC = Arrays.stream(timesC).sum() / (double) Y;

        System.out.printf("uB: %.3f\tuC: %.3f\tuC/uB: %.3f\t", uB, uC, uC / uB);
    }
}
