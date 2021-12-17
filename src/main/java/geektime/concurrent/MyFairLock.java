package geektime.concurrent;

import java.util.concurrent.locks.ReentrantLock;
public class MyFairLock extends Thread{  
    private ReentrantLock lock = new ReentrantLock(true);  
    public void lock(){  
      while(true) {
        try {  
            lock.lock();  
            System.out.println(Thread.currentThread().getName()  + " hold");  
        } finally {  
            System.out.println(Thread.currentThread().getName()  + " release");  
            lock.unlock();  
        }  
      }
    }  
    public static void main(String[] args) {  
        int COUNT = 4;
        MyFairLock myFairLock = new MyFairLock();  
        Runnable runnable = () -> {  
            System.out.println(Thread.currentThread().getName() + " started");  
            myFairLock.lock();  
        };  
        Thread[] thread = new Thread[COUNT];  
        for(int i = 0;i < COUNT;i++){  
            thread[i] = new Thread(runnable);  
        }  
        for(int i = 0;i < COUNT;i++){  
            thread[i].start();  
        }  
    }  
} 
