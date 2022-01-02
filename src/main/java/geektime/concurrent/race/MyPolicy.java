package geektime.concurrent.race;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyPolicy {
    final int genThreadInPool = Runtime.getRuntime().availableProcessors() > 8 ? 8 : Runtime.getRuntime().availableProcessors(); //不超过8
    final int computeThreadInPool = Runtime.getRuntime().availableProcessors() > 16 ? 16 : Runtime.getRuntime().availableProcessors(); //不超过16

    public long go() throws InterruptedException {
        ShareData shareData = new ShareData();
        Random rand = new Random(System.currentTimeMillis());
        CountDownLatch genCountDownLatch = new CountDownLatch(genThreadInPool);
        CountDownLatch sortCountDownLatch = new CountDownLatch(computeThreadInPool);
        List<Integer> score = shareData.getScore();
        ExecutorService genThreadPool = Executors.newFixedThreadPool(genThreadInPool);
        ExecutorService computeThreadPool = Executors.newFixedThreadPool(computeThreadInPool);

        long startTime = System.currentTimeMillis();
        System.out.println("开始自定义A方法计算计时: " + startTime);

        for (int i = 0; i < genThreadInPool; i++) {
            genThreadPool.execute(() -> {
                List<Integer> list = new ArrayList<>();//使用缓存列表，防止添加的时候锁冲突
                for (int j = 0; j < ShareData.COUNT / genThreadInPool; j++) {
                        list.add(rand.nextInt(SimpleShareData.COUNT));
                }
                synchronized (score){//一次批量添加
                    score.addAll(list);
                }
                genCountDownLatch.countDown();
            });
        }
        genCountDownLatch.await();
        long genTime = System.currentTimeMillis();
        System.out.println("产生随机数时长: " + (genTime - startTime));

        int[] top = shareData.getTop();
        int[] cache = new int[top.length];
        int oneShareCount = ShareData.COUNT / computeThreadInPool;
        for (int i = 0; i < computeThreadInPool; i++) {
            int finalI = i;
            computeThreadPool.execute(() -> {
                //使用分组排序
                List<Integer> integers = shareData.getScore().subList(finalI * oneShareCount, (finalI + 1) * oneShareCount);
                Collections.sort(integers, Comparator.reverseOrder());
                //取出每组中最大的10个数据
                for (int j = 0; j < 10; j++) {
                    cache[finalI * 10 + j] = integers.get(j);
                }
                sortCountDownLatch.countDown();
            });
        }
        sortCountDownLatch.await();
        Arrays.sort(cache);
        for (int i = 0; i < 10; i++) {
            top[i] = cache[cache.length - 1 - i];
        }
        // System.out.println(Arrays.toString(top));
        printTop(shareData);
        long sortTime = System.currentTimeMillis();
        System.out.println("自定义A方法计算时长: " + (sortTime - genTime));

        computeThreadPool.shutdown();
        genThreadPool.shutdown();
        return sortTime - genTime;
    }

    void printTop(ShareData shareData) {
        System.out.println("前10成绩为:");
        for (int j = 0; j < 10; j++) {
            System.out.print(shareData.getTop()[j] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
        MyPolicy myPolicy = new MyPolicy();
        myPolicy.go();
    }
}
