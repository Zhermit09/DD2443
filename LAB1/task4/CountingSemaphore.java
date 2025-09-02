public class CountingSemaphore {
    private volatile int permits;

    public CountingSemaphore(int n) {
        permits = n;
    }

    public synchronized void signal() {
        permits++;
        if (permits <= 0) {
            notify();
        }
    }

    public synchronized void s_wait() throws InterruptedException {
        permits--;
        while (permits < 0) {
            wait();
        }
    }
}
