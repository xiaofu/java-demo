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
import org.jsoup.select.Elements;
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
public class WallStreetParserJob  implements Job{

	private static final Log LOG=LogFactory.getLog(WallStreetParserJob.class);
	private static  final String CURRENCY="currencies";
	private static  final String COMMODITY="commodities";
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		Configuration conf=null;
		try {
			Date fireTime= context.getFireTime();
			conf = (Configuration)context.getScheduler().getContext().get("conf");
			String tableName=conf.getString(ConfigKeys.SQL_WS_TABLE);
			AccessHelper.init(conf);
			List<RelationModel> codeRelation=AccessHelper.selectRelationModel(tableName);
			//先把所有HTML收集齐再入库
			//外汇
			String baseUrl=conf.getString(ConfigKeys.SITE_WS_KEY);
			Document doc =Jsoup.connect(baseUrl+CURRENCY).get();
			Elements listEles=  doc.getElementsByClass("symbol-list-item");
			//商品
			for (int i =1; i <= 2; i++) {
				doc=	Jsoup.connect(baseUrl+COMMODITY+"?page="+i+"&sort_type=none").get();
				listEles.addAll(doc.getElementsByClass("symbol-list-item"));
			}
			List<DynaBean> datas=Lists.newArrayList();
			
			DateTime dateTime=new DateTime(fireTime);
			for (Element div : listEles) 	{
					for (RelationModel model : codeRelation) {
						if(model.getShowName().trim().equals(div.child(0).child(0).text().trim()))
						{
							DynaBean bean=new LazyDynaBean();
							bean.set("code", model.getCode());
							bean.set("price", div.child(2).child(0).text().trim());
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
