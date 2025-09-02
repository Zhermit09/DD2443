public class Main {
    static final int N = 1_000_000;
    static final Buffer buffer = new Buffer(N);

    public static class Producer implements Runnable {
        public void run() {
            try {
                for (int i = 0; i < N; i++) {
                    buffer.add(i);
                }
                buffer.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Consumer implements Runnable {
        public void run() {
            int i = 1;
            try {
                while (true) {
                    System.out.println((i++) + ":\t" + buffer.remove());
                }
            } catch (InterruptedException _) {
            }
        }
    }

    public static void main(String[] args) {
        Thread producer = new Thread(new Producer());
        Thread consumer = new Thread(new Consumer());

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            System.err.println("Interrupted at join");
        }
    }
}
