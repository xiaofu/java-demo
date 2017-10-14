/**
 * 
 */
package com.siteparser.www.task;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.siteparser.www.exception.ServiceUncaughtExceptionHandler;
import com.siteparser.www.service.CompositeService;
import com.siteparser.www.utils.ConfigurationFileManager;
import com.siteparser.www.utils.Daemon;
import com.siteparser.www.utils.JvmPauseMonitor;
import com.siteparser.www.utils.ShutdownHookManager;
import com.siteparser.www.utils.StringUtils;

/**
 * @author fulaihua 贵金属，黄金类解析处理
 */
public class Main extends CompositeService {

	private static final Log LOG = LogFactory.getLog(Main.class);
	private static final int SHUTDOWN_HOOK_PRIORITY = 30;
	private   Scheduler sched;
	private JvmPauseMonitor jvmMonitor;
	@Override
	protected void serviceInit(Configuration conf) throws Exception {
		jvmMonitor=new JvmPauseMonitor();
		addIfService(jvmMonitor);
		super.serviceInit(conf);
	}

	@Override
	protected void serviceStart() throws Exception {
		SchedulerFactory schedFact = new  StdSchedulerFactory();
		sched = schedFact.getScheduler();
		sched.getContext().put("conf", getConfig());
		sched.start();
		 
		super.serviceStart();
	}

	@Override
	protected void serviceStop() throws Exception {
		if(sched!=null)
			sched.shutdown();
		super.serviceStop();
	}

	Main() {
		super("MetalSiteTask");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(new ServiceUncaughtExceptionHandler());
		StringUtils.startupShutdownMessage(Main.class, args, LOG);
		try {
			Configuration conf = ConfigurationFileManager
					.getServiceConfigInfo();
			Main task = new Main();
			ShutdownHookManager.get().addShutdownHook(
					new CompositeServiceShutdownHook(task),
					SHUTDOWN_HOOK_PRIORITY);
			task.init(conf);
			task.start();
		} catch (Throwable e) {
			LOG.fatal("Error starting " + Main.class.getSimpleName(),e);
			System.exit(1);
		}
	}

}
