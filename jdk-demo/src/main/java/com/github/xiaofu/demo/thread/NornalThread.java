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
		Thread thread = new Thread()
		{

			@Override
			public void run()
			{
				 for (int i = 0; i < 10000000; i++) {
					System.out.println(i);
					try {
						Thread.sleep(1*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
		Thread.currentThread().join();
		System.out.println("fdsafasdfsd");
	}

}
