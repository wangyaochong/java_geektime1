package geektime.concurrent.part2;


import java.time.Instant;
import java.util.concurrent.Phaser;

public class WorkerWithPhaser extends Thread {
    private Phaser phaser;

    public WorkerWithPhaser(String name, Phaser phaser) {
        this.phaser = phaser;
        phaser.register();
        setName(name);
    }

    @Override public void run() {
        try {
            System.out.printf("[ %s ] 已创建, 阻塞在 phaser\n", getName());
            phaser.arriveAndAwaitAdvance();
            System.out.printf("[ %s ] 开始了: %s\n", getName(), Instant.now());
            // do actual work here...
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
