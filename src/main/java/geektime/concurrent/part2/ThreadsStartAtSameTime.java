package geektime.concurrent.part2;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;

public class ThreadsStartAtSameTime {

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        usingCountDownLatch();

        Thread.sleep(30);

        usingCyclicBarrier();

        Thread.sleep(30);

        usingPhaser();

    }

    private static void usingCountDownLatch() throws InterruptedException {
        System.out.println("===============================================");
        System.out.println("        >>> 使用 CountDownLatch <<<<");
        System.out.println("===============================================");

        CountDownLatch latch = new CountDownLatch(1);

        WorkerWithCountDownLatch worker1 = new WorkerWithCountDownLatch("工作 latch 1", latch);
        WorkerWithCountDownLatch worker2 = new WorkerWithCountDownLatch("工作 latch 2", latch);

        worker1.start();
        worker2.start();

        Thread.sleep(10);//simulation of some actual work

        System.out.println("-----------------------------------------------");
        System.out.println(" 释放 latch:");
        System.out.println("-----------------------------------------------");
        latch.countDown();
    }

    private static void usingCyclicBarrier() throws BrokenBarrierException, InterruptedException {
        System.out.println("\n===============================================");
        System.out.println("        >>> 使用 CyclicBarrier <<<<");
        System.out.println("===============================================");

        CyclicBarrier barrier = new CyclicBarrier(3);

        WorkerWithCyclicBarrier worker1 = new WorkerWithCyclicBarrier("工作 barrier 1", barrier);
        WorkerWithCyclicBarrier worker2 = new WorkerWithCyclicBarrier("工作 barrier 2", barrier);

        worker1.start();
        worker2.start();

        Thread.sleep(10);//simulation of some actual work

        System.out.println("-----------------------------------------------");
        System.out.println(" 打开 barrier:");
        System.out.println("-----------------------------------------------");
        barrier.await();
    }

    private static void usingPhaser() throws InterruptedException {
        System.out.println("\n===============================================");
        System.out.println("        >>> 使用 Phaser <<<");
        System.out.println("===============================================");

        Phaser phaser = new Phaser();
        phaser.register();

        WorkerWithPhaser worker1 = new WorkerWithPhaser("工作 phaser 1", phaser);
        WorkerWithPhaser worker2 = new WorkerWithPhaser("工作 phaser 2", phaser);

        worker1.start();
        worker2.start();

        Thread.sleep(10);//simulation of some actual work

        System.out.println("-----------------------------------------------");
        System.out.println(" 打开 phaser barrier:");
        System.out.println("-----------------------------------------------");
        System.out.println("现在的 phase: " + phaser.getPhase());
        System.out.println("已注册的 parties: "  + phaser.getRegisteredParties());
        System.out.println("已到达的 parties: " + phaser.getArrivedParties());
        phaser.arriveAndAwaitAdvance();
    }
}
