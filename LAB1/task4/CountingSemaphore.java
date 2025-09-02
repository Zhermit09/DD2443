public class CountingSemaphore {
    private volatile int permits;
    private volatile boolean go = false;

    public CountingSemaphore(int n) {
        permits = n;
    }

    public synchronized void signal() {
        go  = true;
        permits++;
        if (permits <= 0) {
            notify();
        }
    }

    public synchronized void s_wait() {
        permits--;
        while (permits < 0 && !go) {
            try {
                wait();
            } catch (InterruptedException _) {  //Mock spurious wakeup
                System.out.println("Interrupt");
            } catch (Exception e) {             //Leave the queue
                permits++;
                throw new RuntimeException(e);
            }
        }
        go = false;
    }
}
