/**
 * 
 */
package com.github.xiaofu.demo.thread;

/**
 * @author fulaihua
 *
 */
public class ThreadExitDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 
				Thread t1=new Thread(new Runnable() {
					
					@Override
					public void run() {
						 try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
				t1.start();
				Thread t2=new Thread(new Runnable() {
					
					@Override
					public void run() {
						 try {
							Thread.sleep(5000);
							System.exit(0);
						} catch (InterruptedException e) {
							 
							e.printStackTrace();
						}
						
					}
				});
				t2.start();
				
	}

}
