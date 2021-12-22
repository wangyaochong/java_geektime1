package geektime.concurrent.race;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShareData {
	public static final int COUNT = 100_000; // 产生随机数个数，不超过 Integer.MAX_VALUE
	
	public static final int BUFSIZE = 160; //
	
	//以下为共享数据
	//存放所有的课程成绩，类型非线程安全且不能更改
	private ArrayList<Integer> score = new ArrayList<Integer>(COUNT);

	//待输出共享数据区，类型非线程安全且不能更改，需要保证前十个数据是正确的
	private int[] top = new int[BUFSIZE];

	//临界变量，可以添加多个，并自行定义类型
	protected Map<String, Object> signals; //可以定义多个用于同步的变量，可自定义类型
	protected List<Integer> exchange; //开辟一块交换数据的区域，可自定义类型
	
	public List<Integer> getScore() {
		return score;
	}
	public int[] getTop() {
		return top;
	}
}
