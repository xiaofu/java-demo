/**
 * 
 */
package com.siteparser.www.task;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import com.google.common.collect.Lists;
import com.siteparser.www.dal.AccessHelper;
import com.siteparser.www.entities.RelationModel;
import com.siteparser.www.utils.ConfigKeys;

/**
 * @author fulaihua
 *
 */
@DisallowConcurrentExecution
public class ICBCParserJob implements Job {

	private static final Log LOG=LogFactory.getLog(ICBCParserJob.class);
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		Configuration conf=null;
		try {
			Date fireTime= context.getFireTime();
			conf = (Configuration)context.getScheduler().getContext().get("conf");
			String tableName=conf.getString(ConfigKeys.SQL_ICBC_TABLE);
			AccessHelper.init(conf);
			List<RelationModel> codeRelation=AccessHelper.selectRelationModel(tableName);
			Document doc =Jsoup.connect(conf.getString(ConfigKeys.SITE_ICBC_KEY)).get();
			Element tableEle= doc.getElementById("TABLE1");
			List<DynaBean> datas=Lists.newArrayList();
			DateTime dateTime=new DateTime(fireTime);
			for (Element row : tableEle.children().select("tr:gt(0)")) 	{
					for (RelationModel model : codeRelation) {
						if(model.getShowName().trim().equals(row.child(0).text().trim()))
						{
							DynaBean bean=new LazyDynaBean();
							bean.set("code", model.getCode());
							bean.set("buyPrice", row.child(2).text().trim());
							bean.set("sellPrice", row.child(3).text().trim());
							bean.set("tYear", dateTime.getYear());
							bean.set("tMonth", dateTime.getMonthOfYear());
							bean.set("tDay", dateTime.getDayOfMonth());
							bean.set("currentTime", dateTime.toString("yyyy-MM-dd HH:mm:ss"));
							datas.add(bean);
							break;
						}
					}
			} 
			
			if(datas.size()!=0)
			{
				AccessHelper.insert(datas, tableName);
				LOG.info("insert "+tableName+" data successfully!");
			}
			else
				LOG.warn(tableName+" is no datas");
			
			
		} catch (SchedulerException e) {
			 //igore
		} catch (IOException e) {
			LOG.error("read html failed",e);
		}

	}

	 

}
