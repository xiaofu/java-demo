/**
 * @ProjectName: fast-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2019年3月21日 下午2:44:04
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.cqvip.runwork.fast.demo;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.cqvip.runwork.common.service.CompositeService;
import com.cqvip.runwork.common.service.exception.ServiceUncaughtExceptionHandler;
import com.cqvip.runwork.common.service.utils.JvmPauseMonitor;
import com.cqvip.runwork.common.service.utils.ShutdownHookManager;
import com.cqvip.runwork.common.service.utils.StringUtils;
import com.cqvip.runwork.fast.demo.utils.ConfigurationFileManager;
import com.cqvip.runwork.fast.demo.utils.Tools;

/**
 * <p>
 * </p>
 * 
 * @author fulaihua 2019年3月21日 下午2:44:04
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2019年3月21日
 * @modify by reason:{方法名}:{原因}
 */
public class HadoopBlockStatisticsService extends CompositeService
{

	private static final Log LOG = LogFactory
			.getLog(HadoopBlockStatisticsService.class);
	private static final int SHUTDOWN_HOOK_PRIORITY = 30;
	private Scheduler sched;

	public HadoopBlockStatisticsService()
	{
		super(HadoopBlockStatisticsService.class.getName());
	}

	@Override
	protected void serviceInit(Configuration conf) throws Exception
	{
		JvmPauseMonitor jvmMonitor = new JvmPauseMonitor();
		addIfService(jvmMonitor);
		super.serviceInit(conf);
	}

	@Override
	protected void serviceStart() throws Exception
	{
		SchedulerFactory schedFact = new StdSchedulerFactory(
				Tools.getConfigFileAbsolutePath(ConfigurationFileManager.QUARTZ_CONFIG_FILE));
		sched = schedFact.getScheduler();
		sched.getContext().put("conf", getConfig());
		sched.start();

		super.serviceStart();
	}

	@Override
	protected void serviceStop() throws Exception
	{
		if (sched != null)
			sched.shutdown();
		super.serviceStop();
	}

	/**
	 * @author fulaihua 2019年3月21日 下午2:44:04
	 * @param args
	 */
	public static void main(String[] args)
	{
		PropertyConfigurator
				.configure(Tools
						.getConfigFileAbsolutePath(ConfigurationFileManager.LOG4J_CONFIG_FILE));
		Thread.setDefaultUncaughtExceptionHandler(new ServiceUncaughtExceptionHandler());
		StringUtils.startupShutdownMessage(HadoopBlockStatisticsService.class,
				args, LOG);
		try
		{
			Configuration conf = ConfigurationFileManager.getServiceConfig();
			HadoopBlockStatisticsService task = new HadoopBlockStatisticsService();
			ShutdownHookManager.get().addShutdownHook(
					new CompositeServiceShutdownHook(task),
					SHUTDOWN_HOOK_PRIORITY);
			task.init(conf);
			task.start();
		}
		catch (Throwable e)
		{
			LOG.fatal(
					"Error starting "
							+ HadoopBlockStatisticsService.class
									.getSimpleName(), e);
			System.exit(1);
		}

	}

}
