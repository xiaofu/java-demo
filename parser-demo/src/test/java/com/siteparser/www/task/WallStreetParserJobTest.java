package com.siteparser.www.task;

import static org.junit.Assert.*;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.siteparser.www.utils.ConfigurationFileManager;

public class WallStreetParserJobTest {

	@Test
	public void test() throws SchedulerException, InterruptedException {
		SchedulerFactory schedFact = new  StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.getContext().put("conf", ConfigurationFileManager.getServiceConfigInfo());
		JobDetail job1 = newJob(WallStreetParserJob.class)
			    .withIdentity("job1", "group1")
			    .build();

			// Define a Trigger that will fire "now", and not repeat
			Trigger trigger = newTrigger()
			    .withIdentity("trigger1", "group1")
			    .startNow()
			    .build();
			sched.scheduleJob(job1, trigger);
			sched.start();
			Thread.currentThread().join();
	}

}
