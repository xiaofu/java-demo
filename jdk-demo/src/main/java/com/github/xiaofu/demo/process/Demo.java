/**
 * @ProjectName: jdk-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2017年12月15日 下午9:16:53
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.process;

import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * <p>
 * 1.子进程异常退出，父进程可以得到非0退出值
 * 2.父进程被kill -15 杀掉，子进程正常运行！
 * </p>
 * @author fulaihua 2017年12月15日 下午9:16:53
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2017年12月15日
 * @modify by reason:{方法名}:{原因}
 */
public class Demo
{
	 private static final  Logger LOG=Logger.getLogger(Demo.class);
	/**
	 * @author fulaihua 2017年12月15日 下午9:16:53
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException
	{
		ProcessBuilder builder=new ProcessBuilder();
		builder.command(args);
		builder.inheritIO();
		Process process=builder.start();
		int a=process.waitFor();
 
		LOG.info(a);
	}

}
