/**
 * @ProjectName: jdk-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2018年7月24日 下午5:47:57
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.thread;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 所有线程，不管主线程还是子线程或后台线程，一旦出现异常都会被未处理的线程处理器捕获处理。
 * </p>
 * @author fulaihua 2018年7月24日 下午5:47:57
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2018年7月24日
 * @modify by reason:{方法名}:{原因}
 */
public class UnCautchExceptionHandleDemo
{
	public  static void main(String[] args) throws InterruptedException
	{
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler()
		{
			
			@Override
			public void uncaughtException(Thread t, Throwable e)
			{
				 System.out.println(t.getName());
				 System.out.println(e.getMessage());
			}
		});
		ExecutorService executorService= Executors.newFixedThreadPool(1);
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				 int a=2;
				 int b=0;
				 int c =a/b;
			}
		});
		executorService.shutdown();
		executorService.awaitTermination(500, TimeUnit.MILLISECONDS);
		/*
		 * Thread t = new Thread(new Runnable() {
		 * 
		 * @Override public void run() { int a=2; int b=0; int c =a/b; } });
		 */
		/*
		 * t.setName("inner"); t.start();
		 */
		/* Thread.sleep(800); */
		System.out.println("over");
  
	}
}
