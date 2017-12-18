/**
 * @ProjectName: jdk-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2017年12月15日 下午9:21:09
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.process;

import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * <p></p>
 * @author fulaihua 2017年12月15日 下午9:21:09
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2017年12月15日
 * @modify by reason:{方法名}:{原因}
 */
public class ComputeTest
{
	 private static final  Logger LOG=Logger.getLogger(ComputeTest.class);
	/**
	 * @author fulaihua 2017年12月15日 下午9:21:09
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		int count=0;
		Runtime.getRuntime().addShutdownHook(new Thread(){
			
			@Override
			public void run()
			{
				LOG.info("=====received quit commander");
			}
			
		});
		int counts=0;
		 while(true)
		 {
			 LOG.info("=======I'm ok==="+count++);
			try
			{
				Thread.currentThread().sleep(500);
			}
			catch (InterruptedException e)
			{
				LOG.warn("interrupted by user");
				return ;
				
			}
			//子进程异常退出，父进程可以得到非0退出值
			if(counts>20)
				throw new IOException("subprocess exit");
			 
		 }

	}

}
