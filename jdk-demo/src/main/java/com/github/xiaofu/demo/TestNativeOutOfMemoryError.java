/**
 * 
 */
package com.github.xiaofu.demo;

import java.util.concurrent.CountDownLatch;

/**
 * @author Administrator
 *
 */
public class TestNativeOutOfMemoryError {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 for (int i = 0;; i++) {
	            System.out.println("i = " + i);
	            new Thread(new HoldThread()).start();
	        }

	}
	
	static class HoldThread extends Thread {
	    CountDownLatch cdl = new CountDownLatch(1);
	    public HoldThread() {
	        this.setDaemon(true);
	    }
	    public void run() {
	        try {cdl.await();} 
	        catch (InterruptedException e) {}
	    }
	}

}
