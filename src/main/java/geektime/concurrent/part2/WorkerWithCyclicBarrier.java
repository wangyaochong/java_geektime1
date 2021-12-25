package geektime.concurrent.part2;

import java.time.Instant;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class WorkerWithCyclicBarrier extends Thread {
    private CyclicBarrier barrier;

    public WorkerWithCyclicBarrier(String name, CyclicBarrier barrier) {
        this.barrier = barrier;
        this.setName(name);
    }

    @Override public void run() {
        try {
            System.out.printf("[ %s ] 已创建, 阻塞在 barrier\n", getName());
            barrier.await();
            System.out.printf("[ %s ] 开始了: %s\n", getName(), Instant.now());
            // do actual work here...
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
