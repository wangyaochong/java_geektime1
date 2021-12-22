package geektime.concurrent.race;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

public class SimpleShareData extends ShareData {
	final String GEN = "genlatch";
	final String COMP = "complatch";

	void initGenSignals(int genThreadNum) {
		CountDownLatch latch = new CountDownLatch(genThreadNum);
		if (signals == null) {
			signals = new HashMap<String, Object>();
		}
		signals.put(GEN, latch);
	}

	CountDownLatch getGenSig() {
		if (signals != null) {
			return (CountDownLatch)signals.get(GEN);
		}
		return null;
	}
	
	void initCompSignals(int genThreadNum) {
		CountDownLatch latch = new CountDownLatch(genThreadNum);
		if (signals == null) {
			signals = new HashMap<String, Object>();
		}
		signals.put(COMP, latch);
	}

	CountDownLatch getCompSig() {
		if (signals != null) {
			return (CountDownLatch)signals.get(COMP);
		}
		return null;
	}
	
	void initExchange() {
		exchange = new CopyOnWriteArrayList<Integer>();
	}
	
	void addExchange(int data) {
		exchange.add(data);
	}
	
	List<Integer> getShare() {
		return exchange;
	}
}
