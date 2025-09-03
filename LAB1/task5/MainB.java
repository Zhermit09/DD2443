import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class MainB {
    static final int n = 5;

    //Color coding per thread for println()
    public static class Color {
        static final Map<String, String> colors = new HashMap<>();

        static {
            colors.put("Ph-0", "\u001B[32m");
            colors.put("Ph-1", "\u001B[33m");
            colors.put("Ph-2", "\u001B[34m");
            colors.put("Ph-3", "\u001B[35m");
            colors.put("Ph-4", "\u001B[36m");
        }

        static final String RESET = "\u001B[0m";

        static public void println(String s) {
            var name = Thread.currentThread().getName();
            System.out.println(colors.getOrDefault(name, RESET) + name + ": " + s + RESET);
        }
    }

    public static class Chopstick {
        private final String name;
        private Thread holder;

        public Chopstick(int id) {
            name = "Ch-" + id;
        }

        public synchronized void pickUp() throws InterruptedException {
            while (holder != null) {
                wait();
            }
            holder = Thread.currentThread();
        }

        public synchronized void putDown() {
            if (Thread.currentThread() != holder) {
                throw new IllegalStateException();
            }
            holder = null;
            notifyAll();
        }
    }

    public static class Philosopher implements Runnable {
        static final Deque<Philosopher> deque = new ArrayDeque<>();

        Chopstick lchopstick;
        Chopstick rchopstick;

        public Philosopher(Chopstick lch, Chopstick rch) {
            lchopstick = lch;
            rchopstick = rch;
        }

        public void run() {
            try {
                while (true) {
                    //Think
                    Color.println("Starts Thinking...");
                    Thread.sleep(100);
                    Color.println("Stops Thinking...");

                    synchronized (deque) {
                        deque.offer(this);
                        while (deque.peek() != this) {
                            deque.wait();
                        }
                    }

                    //Pick up
                    Color.println("Tries to pick up left: " + lchopstick.name);
                    lchopstick.pickUp();
                    Color.println("Picked up left: " + lchopstick.name);
                    Color.println("Tries to pick up right: " + rchopstick.name);
                    rchopstick.pickUp();
                    Color.println("Picked up right: " + rchopstick.name);

                    synchronized (deque) {
                        deque.poll();
                        deque.notifyAll();
                    }

                    //Dine
                    Color.println("Starts Dining...");
                    Thread.sleep(100);
                    Color.println("Stops Dining...");

                    //Put down
                    Color.println("Puts down left: " + lchopstick.name);
                    lchopstick.putDown();
                    Color.println("Puts down right: " + rchopstick.name);
                    rchopstick.putDown();

                    //Repeat
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        Thread[] ph = new Thread[n];
        Chopstick[] ch = new Chopstick[n];

        for (int i = 0; i < n; i++) {
            ch[i] = new Chopstick(i);
        }

        ph[0] = new Thread(new Philosopher(ch[n - 1], ch[0]), "Ph-0");
        ph[0].start();
        for (int i = 1; i < n; i++) {
            ph[i] = new Thread(new Philosopher(ch[i - 1], ch[i]), "Ph-" + i);
            ph[i].start();
        }

    }
}
