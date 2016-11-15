package com.github.xiaofu.demo;

public class InnerClassTest {

	//5*4 字节
	private static final int[]  outer=new int [1024*1024*5];
	public ITest start(final int interval) {
	
		ITest test = new ITest() {
			private int[] tt;
			@Override
			public void doIt() {
				tt=new int[1024*1024*10];
				System.out.println(interval);
				System.out.println(outer.length);
			}
		};
		return test;

	}

	public static void main(String[] args) throws InterruptedException {
	  System.out.println("new inner step1=============="+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
		InnerClassTest test = new InnerClassTest();
		ITest[] tests=new ITest[10];
		for (int i = 0; i < 10; i++) {
			tests[i]=test.start(i);
		}
		for (int i = 0; i < 10; i++) {
			tests[i].doIt();
			tests[i]=null;//会被垃圾回收!!去掉引用
		}
		 System.out.println("new inner after=============="+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
		Thread.currentThread().join();
	}

	interface ITest {
		void doIt();
	}
}
