import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private final Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    private int[] buffer;
    private int idx = 0;
    private boolean closed = false;


    public Buffer(int size) {
        buffer = new int[size];
    }

    void add(int i) throws InterruptedException {
        if (closed) {
            throw new InterruptedException("Buffer is closed");
        }

        lock.lock();
        try {
            while (idx == buffer.length) {
                notFull.await();
            }
            buffer[idx++] = i;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public int remove() throws InterruptedException {
        if (closed && idx == 0) {
            throw new InterruptedException("Buffer is closed");
        }

        int i;
        lock.lock();
        try {
            while (idx == 0) {
                notEmpty.await();
            }
            i = buffer[--idx];
            notFull.signal();
        } finally {
            lock.unlock();
        }
        return i;
    }

    public void close() throws InterruptedException {
        if (closed) {
            throw new InterruptedException("Buffer is closed");
        }

        closed = true;
    }
}
