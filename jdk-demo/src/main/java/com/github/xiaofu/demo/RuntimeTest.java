package com.github.xiaofu.demo;

import java.io.IOException;


public class RuntimeTest
{
	public static void main(String[] args) throws InterruptedException {
				//构造方法被私有化了。
				Runtime myRun = Runtime.getRuntime();
				System.out.println("已用内存" + myRun.totalMemory());
				System.out.println("最大内存" + myRun.maxMemory());
				System.out.println("可用内存" + myRun.freeMemory());
				String i = "";
				long start = System.currentTimeMillis();
				System.out.println("浪费内存中.....");
				byte[] bytes=new byte[1024*1024*100];
				long end = System.currentTimeMillis();
				System.out.println("执行此程序总共花费了" + ( end - start )+ "毫秒");
				System.out.println("已用内存" + myRun.totalMemory());
				System.out.println("最大内存" + myRun.maxMemory());
				System.out.println("可用内存" + myRun.freeMemory());
				myRun.gc();
				System.out.println("清理垃圾后");
				System.out.println("已用内存" + myRun.totalMemory());
				System.out.println("最大内存" + myRun.maxMemory());
				System.out.println("可用内存" + myRun.freeMemory());
				Thread.currentThread().join(1000*20);
	}
}
