package com.github.xiaofu.demo.thread;

public class InteruptTest {

	public static void main(String[] args) throws InterruptedException {
		
		Thread t=innerInterrupt();
		Thread.sleep(800);
		t.interrupt();
		 
	}
	
	private static void test() throws InterruptedException
	{
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					long a = 0;
					//while (!Thread.interrupted())
					while (!Thread.currentThread().isInterrupted()) {
						Thread.sleep(600);
						for (int i = 0; i < 1000000; i++) {
							for (int j = 0; j < 10000; j++) {
								a++;
							}
						}
					}
					System.out.println("exit by flag");
				} catch (InterruptedException e) {
					System.out.println("exit by exception");
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().isInterrupted());
			}
		});
		t.start();
		Thread.sleep(800);
		//Thread.sleep(400);
		t.interrupt();
	}
	
	private static Thread innerInterrupt()
	{
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				
				Thread inner=new Thread(new Runnable() {
					
					@Override
					public void run() {
						 
						while(true)
						{
							try {
								Thread.sleep(2*1000);
							} catch (InterruptedException e) {
								 System.out.println("I was Interrupted by Outer");
							}
							System.out.println("I'm Inner,Interrupted Status:"+Thread.currentThread().isInterrupted());
						}
					}
				});
				inner.start();
				while(true)
				{
					try {
						Thread.sleep(2*1000);
					} catch (InterruptedException e) {
						 System.out.println("I was Interrupted by user");
						 break;
					}
				}
				System.out.println("Outer exit!");
			}
			
		});
		t.start();
		return t;
	}

}
