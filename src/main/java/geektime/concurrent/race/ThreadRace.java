package geektime.concurrent.race;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * 线程竞赛
 * 
 * @author shihang
 */
public class ThreadRace {

	final String DONE = "done";
	class BaseShareData extends ShareData {
		void initSignals() {
			signals = new HashMap<String, Object>();
		}
		boolean getSig() {
			if (signals != null && signals.get(DONE) != null) {
				return ((Boolean)signals.get(DONE)).booleanValue();
			}
			return false;
		}
		void setSig(boolean sig) {
			Boolean s = (Boolean)signals.get(DONE);
			if (s == null) {
				signals.put(DONE, sig);
			} else {
				signals.replace(DONE, sig);
			}
		}
	}

	public static void main(String[] args) throws Exception {

		long startTime = System.currentTimeMillis();
		System.out.println("开始单线程基准计时: " + startTime);
		ThreadRace tr = new ThreadRace();
		
		//以下为单一线程顺序执行，基准时间
		//step A
		tr.gen();
		long genTime = System.currentTimeMillis();
		System.out.println("产生随机数时长: " + (genTime - startTime));
		
		//step B
		tr.sort();
		long sortTime = System.currentTimeMillis();
		System.out.println("计算过程时长: " + (sortTime - genTime));
		
		//step C
		tr.printTop();
		long totalTime = System.currentTimeMillis() - startTime;
		System.out.println("总时长: " + totalTime);
		
		SimplePolicy sp = new SimplePolicy();
		long totalSimple = sp.go();
		
		double rate = totalSimple / totalTime;
		System.out.println("自定义A和基准比较: " + new Double(rate).toString());
	}
	
	BaseShareData bsd;
	public ThreadRace() {
		bsd = new BaseShareData();
		bsd.initSignals();
	}
    void gen() throws Exception {
		Thread genRandom = new BaseGenThread(bsd);
		genRandom.start();
		genRandom.join();
    }
    
    void sort() throws Exception {
		Thread sortRandom = new BaseSortThead(bsd);
		sortRandom.start();
		sortRandom.join();
    }
    
    void printTop() {
    	System.out.println("前10成绩为:");
    	for (int j = 0; j <= 10; j++) {
    		System.out.print(bsd.getTop()[j] + " ");
    	}
    	System.out.println();
    }
    
    public class BaseGenThread extends Thread {
    	private BaseShareData bsd = null;
    	BaseGenThread(BaseShareData bsd) {
    		this.bsd = bsd;
    	}
        @Override
        public void run() {
        	Random rand = new Random(System.currentTimeMillis());
        	for (int i = 0; i <= BaseShareData.COUNT; i++) {
        		bsd.getScore().add(new Integer(rand.nextInt(BaseShareData.COUNT)));
        	}
        	bsd.setSig(false);
        }
    }
    public class BaseSortThead extends Thread {
    	private BaseShareData bsd = null;
    	BaseSortThead(BaseShareData bsd) {
    		this.bsd = bsd;
    	}
        @Override
        public void run() {
        	while (bsd.getSig()) {
        		try {
        			Thread.sleep(10);
        		} catch (Exception e) {
        		}
        	}
        	Integer[] box = bsd.getScore().toArray(new Integer[bsd.getScore().size()]);
        	Arrays.sort(box);
        	for (int i = 0; i < BaseShareData.BUFSIZE; i++) {
        		bsd.getTop()[i] = box[box.length - i - 1];
        	}
        }
    }
}
