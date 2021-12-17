package geektime.concurrent;

class NewRunnable implements Runnable {
    public void run() {
        System.out.println("thread is running...");
    }

    public static void main(String[] args) {
        NewRunnable m1 = new NewRunnable();
        Thread t1 = new Thread(m1); // Using the constructor Thread(Runnable r)
        t1.start();
    }
}
