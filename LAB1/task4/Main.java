import java.util.HashMap;
import java.util.Map;

public class Main {
    static CountingSemaphore countingSemaphore;

    //Color coding per thread for println()
    public static class Color{
        static final Map<String, String> colors = new HashMap<>();
        static {
            colors.put("Thread-0", "\u001B[31m");
            colors.put("Thread-1", "\u001B[32m");
            colors.put("Thread-2", "\u001B[33m");
            colors.put("Thread-3", "\u001B[34m");
            colors.put("Thread-4", "\u001B[35m");
        }
        static final String RESET  = "\u001B[0m";

        static public void println(String s) {
            System.out.println(colors.get(Thread.currentThread().getName()) + s + RESET);
        }
    }

    public static class Runner implements Runnable {
        public void run() {
            String name = Thread.currentThread().getName();

            Color.println(name + ": s_wait()");
            countingSemaphore.s_wait();

            Color.println(name + ": Start CS");
            try {
                Thread.sleep(2000);
                Color.println(name + ": End CS");
            } catch (InterruptedException e) {
                Color.println(name + ": Work interrupted");
            }finally {
                Color.println(name + ": signal()");
                countingSemaphore.signal();
            }
        }
    }

    public static void twoWaitTest() throws InterruptedException {
        System.out.println("waitTest--------------");
        countingSemaphore = new CountingSemaphore(1);
        var t1 = new Thread(new Runner(), "Thread-0");
        var t2 = new Thread(new Runner(), "Thread-1");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        countingSemaphore = null;
        System.out.println("----------------------\n");
    }

    public static void noWaitTest() throws InterruptedException {
        System.out.println("noWaitTest------------");
        countingSemaphore = new CountingSemaphore(3);
        var t1 = new Thread(new Runner(), "Thread-0");
        var t2 = new Thread(new Runner(), "Thread-1");
        var t3 = new Thread(new Runner(), "Thread-2");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        countingSemaphore = null;
        System.out.println("----------------------\n");
    }

    public static void allWaitTest() throws InterruptedException {
        System.out.println("allWaitTest-----------");
        countingSemaphore = new CountingSemaphore(0);
        var t1 = new Thread(new Runner(), "Thread-0");
        var t2 = new Thread(new Runner(), "Thread-1");
        var t3 = new Thread(new Runner(), "Thread-2");

        t1.start();
        t2.start();
        t3.start();

        t1.join(5000);
        t2.join(5000);
        t3.join(5000);

        System.out.println(t1.getName() + ": Alive = " + t1.isAlive());
        System.out.println(t2.getName() + ": Alive = " + t2.isAlive());
        System.out.println(t3.getName() + ": Alive = " + t3.isAlive());

        System.out.println("Timeout");
        countingSemaphore = null;
        System.out.println("----------------------\n");
    }

    public static void limitedWaitTest() throws InterruptedException {
        System.out.println("limitedWaitTest--------");
        countingSemaphore = new CountingSemaphore(2);
        var t1 = new Thread(new Runner(), "Thread-0");
        var t2 = new Thread(new Runner(), "Thread-1");
        var t3 = new Thread(new Runner(), "Thread-2");
        var t4 = new Thread(new Runner(), "Thread-3");
        var t5 = new Thread(new Runner(), "Thread-4");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();

        countingSemaphore = null;
        System.out.println("----------------------\n");
    }

    public static void cycleWaitTest() throws InterruptedException {
        System.out.println("cycleWaitTest----------");
        countingSemaphore = new CountingSemaphore(2);
        var t1 = new Thread(new Runner(), "Thread-0");
        var t2 = new Thread(new Runner(), "Thread-1");
        var t3 = new Thread(new Runner(), "Thread-2");
        var t4 = new Thread(new Runner(), "Thread-3");
        var t5 = new Thread(new Runner(), "Thread-4");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        t1.join(5000);
        t2.join(5000);
        t3.join(5000);
        t4.join(5000);
        t5.join(5000);

        System.out.println();
        System.out.println(t1.getName() + ": Alive = " + t1.isAlive());
        System.out.println(t2.getName() + ": Alive = " + t2.isAlive());
        System.out.println(t3.getName() + ": Alive = " + t3.isAlive());
        System.out.println(t4.getName() + ": Alive = " + t4.isAlive());
        System.out.println(t5.getName() + ": Alive = " + t5.isAlive());
        System.out.println();

        t1 = new Thread(new Runner(), "Thread-0");
        t2 = new Thread(new Runner(), "Thread-1");
        t3 = new Thread(new Runner(), "Thread-3");
        t4 = new Thread(new Runner(), "Thread-4");
        t5 = new Thread(new Runner(), "Thread-5");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        t1.join(5000);
        t2.join(5000);
        t3.join(5000);
        t4.join(5000);
        t5.join(5000);

        System.out.println();
        System.out.println(t1.getName() + ": Alive = " + t1.isAlive());
        System.out.println(t2.getName() + ": Alive = " + t2.isAlive());
        System.out.println(t3.getName() + ": Alive = " + t3.isAlive());
        System.out.println(t4.getName() + ": Alive = " + t4.isAlive());
        System.out.println(t5.getName() + ": Alive = " + t5.isAlive());
        System.out.println();

        t1 = new Thread(new Runner(), "Thread-0");
        t2 = new Thread(new Runner(), "Thread-1");
        t3 = new Thread(new Runner(), "Thread-3");
        t4 = new Thread(new Runner(), "Thread-4");
        t5 = new Thread(new Runner(), "Thread-5");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        t1.join(5000);
        t2.join(5000);
        t3.join(5000);
        t4.join(5000);
        t5.join(5000);

        System.out.println();
        System.out.println(t1.getName() + ": Alive = " + t1.isAlive());
        System.out.println(t2.getName() + ": Alive = " + t2.isAlive());
        System.out.println(t3.getName() + ": Alive = " + t3.isAlive());
        System.out.println(t4.getName() + ": Alive = " + t4.isAlive());
        System.out.println(t5.getName() + ": Alive = " + t5.isAlive());
        System.out.println();

        t1 = new Thread(new Runner(), "Thread-0");
        t2 = new Thread(new Runner(), "Thread-1");
        t3 = new Thread(new Runner(), "Thread-3");
        t4 = new Thread(new Runner(), "Thread-4");
        t5 = new Thread(new Runner(), "Thread-5");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        t1.join(5000);
        t2.join(5000);
        t3.join(5000);
        t4.join(5000);
        t5.join(5000);

        System.out.println();
        System.out.println(t1.getName() + ": Alive = " + t1.isAlive());
        System.out.println(t2.getName() + ": Alive = " + t2.isAlive());
        System.out.println(t3.getName() + ": Alive = " + t3.isAlive());
        System.out.println(t4.getName() + ": Alive = " + t4.isAlive());
        System.out.println(t5.getName() + ": Alive = " + t5.isAlive());
        System.out.println();

        t1 = new Thread(new Runner(), "Thread-0");
        t2 = new Thread(new Runner(), "Thread-1");
        t3 = new Thread(new Runner(), "Thread-3");
        t4 = new Thread(new Runner(), "Thread-4");
        t5 = new Thread(new Runner(), "Thread-5");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        t1.join(5000);
        t2.join(5000);
        t3.join(5000);
        t4.join(5000);
        t5.join(5000);

        System.out.println();
        System.out.println(t1.getName() + ": Alive = " + t1.isAlive());
        System.out.println(t2.getName() + ": Alive = " + t2.isAlive());
        System.out.println(t3.getName() + ": Alive = " + t3.isAlive());
        System.out.println(t4.getName() + ": Alive = " + t4.isAlive());
        System.out.println(t5.getName() + ": Alive = " + t5.isAlive());
        System.out.println();

        countingSemaphore = null;
        System.out.println("----------------------\n");
    }

    public static void mockSpuriousTest() throws InterruptedException {
        System.out.println("mockSpuriousTest------");
        countingSemaphore = new CountingSemaphore(1);
        var t1 = new Thread(new Runner(), "Thread-0");
        var t2 = new Thread(new Runner(), "Thread-1");

        t1.start();
        Thread.sleep(500);
        t2.start();
        t2.interrupt();

        t1.join();
        t2.join();
        countingSemaphore = null;
        System.out.println("----------------------\n");
    }

    public static void main(String[] args) throws InterruptedException {
        twoWaitTest();
        noWaitTest();
        noWaitTest();
        allWaitTest();
        limitedWaitTest();
        cycleWaitTest();
        mockSpuriousTest();

        System.exit(0);
    }
}
