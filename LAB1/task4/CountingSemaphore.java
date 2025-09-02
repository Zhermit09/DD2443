public class CountingSemaphore {
    private volatile int permits;
    private volatile int go = 0;

    public CountingSemaphore(int n) {
        permits = n;
    }

    public synchronized void signal() {
        go++;
        permits++;
        if (permits <= 0) {
            notify();
        }
    }

    public synchronized void s_wait() {
        permits--;
        while (permits < 0 && go == 0) {
            try {
                wait();
            } catch (InterruptedException _) {  //Mocking spurious wakeups, should not be part of real implementation
                System.out.println("Spurious wakeup ignored");
            } catch (Exception e) {             //Mocking interrupt, leave the queue gracefully
                permits++;
                throw new RuntimeException(e);
            }
        }
        go = go > 0 ? go - 1 : 0;
    }
}