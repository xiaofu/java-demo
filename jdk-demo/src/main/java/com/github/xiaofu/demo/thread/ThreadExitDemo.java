/**
 * 
 */
package com.github.xiaofu.demo.thread;

/**
 * 1.后台线程也是属于活线程，GC中不会被无缘无故被回收。从中能想到，如果后台线程在执行任务被回收了多可怕？
 * 不管是创建了一个对象内部包含有一个后台线程，还是main方法中有一个后台线程，都不会因GC而回收对象。
 * @author fulaihua
 *
 */
public class ThreadExitDemo {
	static class Test {
		public  void test() {
			Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while (true) {
							System.out.println("" + Thread.currentThread().getName());
							Thread.sleep(1 * 500);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			t1.setName("test");
			t1.setDaemon(true);
			t1.start();
		}
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		new Test().test();
		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(10000000);
					System.exit(0);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}

			}
		});
		t2.start();

	}

}
