package geektime.concurrent.race;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimplePolicy {
	
	final int genThreadInPool = 2; //不超过8
	final int computeThreadInPool = 2; //不超过16
	SimpleShareData ssd;
	
	public SimplePolicy() {
		ssd = new SimpleShareData();
		ssd.initGenSignals(genThreadInPool);
		ssd.initCompSignals(computeThreadInPool);
		ssd.initExchange();
	}

	public long go() throws Exception {
		ExecutorService genThreadPool = Executors.newFixedThreadPool(genThreadInPool);
		ExecutorService computeThreadPool = Executors.newFixedThreadPool(computeThreadInPool);
		
		//使用自定义方法A计算
		long startTime = System.currentTimeMillis();
		System.out.println("开始自定义A方法计算计时: " + startTime);
		

		for (int tp = 0; tp < genThreadInPool; tp++) {
		
			SimpleSyncGen simpleSyncGen = new SimpleSyncGen(ssd, SimpleShareData.COUNT / genThreadInPool, tp);
			genThreadPool.execute(simpleSyncGen);
		}
		
		ssd.getGenSig().await();
		
		long genTime = System.currentTimeMillis();
		System.out.println("产生随机数时长: " + (genTime - startTime));
		

		for (int tp = 0; tp < computeThreadInPool; tp++) {
		
			SimpleDivideCompute simpleDiv = new SimpleDivideCompute(ssd, SimpleShareData.COUNT / computeThreadInPool, tp);
			computeThreadPool.execute(simpleDiv);
		}
		ssd.getCompSig().await();
		
		Integer[] box = ssd.getShare().toArray(new Integer[ssd.getShare().size()]);
		Arrays.sort(box);
		for (int i = 0; i < SimpleShareData.BUFSIZE; i++) {
			ssd.getTop()[i] = box[box.length - i - 1];
		}
		printTop();
		
		long sortTime = System.currentTimeMillis();
		System.out.println("自定义A方法计算时长: " + (sortTime - genTime));
		
		computeThreadPool.shutdown();
		genThreadPool.shutdown();
		return sortTime - genTime;
	}
    void printTop() {
    	System.out.println("前10成绩为:");
    	for (int j = 0; j <= 10; j++) {
    		System.out.print(ssd.getTop()[j] + " ");
    	}
    	System.out.println();
    }
}
