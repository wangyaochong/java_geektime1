package geektime.concurrent.part2;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;

public class WorkerWithCountDownLatch extends Thread {
    private CountDownLatch latch;

    public WorkerWithCountDownLatch(String name, CountDownLatch latch) {
        this.latch = latch;
        setName(name);
    }

    @Override public void run() {
        try {
            System.out.printf("[ %s ] 已创建, 阻塞在 latch\n", getName());
            latch.await();
            System.out.printf("[ %s ] 开始了: %s\n", getName(), Instant.now());
            // do actual work here...
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
