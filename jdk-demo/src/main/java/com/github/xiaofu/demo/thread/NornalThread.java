/**
 * @ProjectName: demo
 * @Copyright: 版权所有 Copyright © 2001-2012 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2014-9-21 上午1:13:36
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.thread;

/**
 * 
 * 只是想说明的一问题是子线程抛出异常，不影响主线程
 * <p>
 * </p>
 * 
 * @author fulaihua 2014-9-21 上午1:13:36
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2014-9-21
 * @modify by reason:{方法名}:{原因}
 */
public class NornalThread
{

	/**
	 * @author fulaihua 2014-9-21 上午1:13:36
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException
	{
		try
		{
			MyThread thread = new MyThread();
			thread.start();
			Thread.sleep(2000);
			thread.interrupt();// 请求中断MyThread线程
		}
		catch (InterruptedException e)
		{
			System.out.println("main catch");
			e.printStackTrace();
		}
		System.out.println("end!");

	}

	static class MyThread extends Thread
	{
		@Override
		public void run()
		{
			super.run();
			try
			{
				for (int i = 0; i < 500000; i++)
				{
					if (this.interrupted())
					{
						System.out.println("should be stopped and exit");
						throw new InterruptedException();
					}
					System.out.println("i=" + (i + 1));
				}
				System.out
						.println("this line cannot be executed. cause thread throws exception");
			}
			catch (InterruptedException e)
			{
				/**
				 * 这样处理不好 System.out.println("catch interrupted exception");
				 * e.printStackTrace();
				 */
				Thread.currentThread().interrupt();// 这样处理比较好
			}
		}
	}

}
