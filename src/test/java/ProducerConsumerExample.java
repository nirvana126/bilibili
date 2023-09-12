import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于Condition实现生产者消费者模型
 */
public class ProducerConsumerExample {
    private static final int MAX_CAPACITY = 10; // 缓冲区最大容量
    private final Queue<Integer> buffer = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public void produce() throws InterruptedException {
        lock.lock();
        try {
            while (buffer.size() == MAX_CAPACITY) { // 缓冲区已满，等待消费者消费
                notFull.await();
            }

            int value = (int) (Math.random() * 100);
            buffer.offer(value);
            System.out.println("生产者生产数据：" + value);

            notEmpty.signal(); // 通知消费者缓冲区已有数据
        } finally {
            lock.unlock();
        }
    }

    public void consume() throws InterruptedException {
        lock.lock();
        try {
            while (buffer.isEmpty()) { // 缓冲区为空，等待生产者生产
                notEmpty.await();
            }

            int value = buffer.poll();
            System.out.println("消费者消费数据：" + value);

            notFull.signal(); // 通知生产者缓冲区有空位
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ProducerConsumerExample example = new ProducerConsumerExample();

        Thread producerThread = new Thread(() -> {
            try {
                while (true) {
                    example.produce();
                    Thread.sleep(1000); // 生产者每隔1秒生产一个数据
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread consumerThread = new Thread(() -> {
            try {
                while (true) {
                    example.consume();
                    Thread.sleep(2000); // 消费者每隔2秒消费一个数据
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producerThread.start();
        consumerThread.start();
    }
}
