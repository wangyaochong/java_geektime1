package geektime.concurrent.race;

import java.util.Random;

public class SimpleSyncGen implements GenRunnable {

	SimpleShareData ssd;
	int size;
	int offset;
	public SimpleSyncGen(SimpleShareData ssd, int size, int offset) {
		this.ssd = ssd;
		this.size = size;
		this.offset = offset;
	}
	
	@Override
	public void run() {
		gen();
	}

	@Override
	public void gen() {
		//System.out.println("开始产生随机数: " + size);
		Random rand = new Random(System.currentTimeMillis());
		int r;
		int i;
    	for (i = 0; i < size; i++) {
    		r = rand.nextInt(SimpleShareData.COUNT);
    		synchronized (ssd.getScore()) {
    			ssd.getScore().add(new Integer(r));
    		}
    	}
    	//System.out.println("产生随机数个数: " + i);
    	ssd.getGenSig().countDown();
	}

}
