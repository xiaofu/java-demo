/**
 * @ProjectName: fast-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2019年3月21日 下午3:11:49
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.cqvip.runwork.fast.demo.quartz;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.RequestExpectContinue;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.util.EntityUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * <p>
 * </p>
 * 
 * @author fulaihua 2019年3月21日 下午3:11:49
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2019年3月21日
 * @modify by reason:{方法名}:{原因}
 */
@DisallowConcurrentExecution
public class BlockedScheduledJob implements Job
{
	static ObjectMapper MAPPER = new ObjectMapper();
	private static final Log LOG = LogFactory.getLog(BlockedScheduledJob.class);
	static List<String> excludeNodes = Lists.newArrayList();
	static
	{
		excludeNodes.add("node21.vipcloud");
		excludeNodes.add("node22.vipcloud");
		excludeNodes.add("node23.vipcloud");
		excludeNodes.add("node24.vipcloud");
		excludeNodes.add("node25.vipcloud");
		excludeNodes.add("node26.vipcloud");
		excludeNodes.add("node27.vipcloud");
		excludeNodes.add("node28.vipcloud");
		excludeNodes.add("node29.vipcloud");
		excludeNodes.add("node30.vipcloud");
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException
	{

		try (CloseableHttpClient httpclient = HttpClients
				.custom()
				.setHttpProcessor(
						HttpProcessorBuilder
								.create()
								.addAll(new RequestContent(true),
										new RequestTargetHost(),
										new RequestExpectContinue()).build())
				.build())
		{
			// TODO:should judge value exist or not
			String activeAddress = null;
			String nn1 = context.getJobDetail().getJobDataMap().get("nn1")
					.toString();
			String nn2 = context.getJobDetail().getJobDataMap().get("nn2")
					.toString();
			HttpGet nn1StateRequest = new HttpGet(new URIBuilder(nn1)
					.addParameter(
							"qry",
							context.getJobDetail().getJobDataMap()
									.getString("state")).build());
			HttpGet nn2StateRequest = new HttpGet(new URIBuilder(nn2)
					.addParameter(
							"qry",
							context.getJobDetail().getJobDataMap()
									.getString("state")).build());
			// nn1StateRequest.addHeader( "User-Agent",
			// "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
			int total = 0;
			try (CloseableHttpResponse response = httpclient
					.execute(nn1StateRequest))
			{
				if (response.getStatusLine().getStatusCode() == 200)
				{
					JsonNode node1 = MAPPER.readTree(EntityUtils
							.toString(response.getEntity()));
					if (((ArrayNode) node1.get("beans")).get(0).get("State")
							.asText().equalsIgnoreCase("active"))
					{
						activeAddress = nn1;
					}
					else
					{
						try (CloseableHttpResponse response2 = httpclient
								.execute(nn2StateRequest))
						{
							if (response2.getStatusLine().getStatusCode() == 200)
							{
								node1 = MAPPER.readTree(EntityUtils
										.toString(response2.getEntity()));
								if (((ArrayNode) node1.get("beans")).get(0)
										.get("State").asText()
										.equalsIgnoreCase("active"))
								{
									activeAddress = nn2;
								}
							}
						}
					}
				}
			}
			catch (IOException | ParseException e)
			{
				LOG.error("fetch info errors", e);
				return;
			}
			if (!Strings.isNullOrEmpty(activeAddress))
			{
				try (CloseableHttpResponse response = httpclient
						.execute(new HttpGet(new URIBuilder(activeAddress)
								.addParameter(
										"get",
										context.getJobDetail().getJobDataMap()
												.getString("liveNodes"))
								.build())))
				{
					if (response.getStatusLine().getStatusCode() == 200)
					{
						JsonNode node1 = MAPPER.readTree(EntityUtils
								.toString(response.getEntity()));
						ObjectNode liveNodes = (ObjectNode) MAPPER
								.readTree(((ArrayNode) node1.get("beans"))
										.get(0).get("LiveNodes").asText());
						Iterator<Entry<String, JsonNode>> iter = liveNodes
								.fields();
						while (iter.hasNext())
						{
							Entry<String, JsonNode> entity = iter.next();
							if (!excludeNodes.contains(entity.getKey()
									.toString().toLowerCase()))
							{
								total += entity.getValue()
										.get("blockScheduled").asInt();
							}
						}
					}
				}
				try (CloseableHttpResponse response = httpclient
						.execute(new HttpGet(new URIBuilder(activeAddress)
								.addParameter(
										"qry",
										context.getJobDetail().getJobDataMap()
												.getString("FSNamesystemState"))
								.build())))
				{
					if (response.getStatusLine().getStatusCode() == 200)
					{
						JsonNode node1 = MAPPER.readTree(EntityUtils
								.toString(response.getEntity()));
						int totalLoad= ((ArrayNode) node1.get("beans"))
										.get(0).get("TotalLoad").asInt();
						int liveNodes=((ArrayNode) node1.get("beans"))
								.get(0).get("NumLiveDataNodes").asInt();
						int averageLoad=totalLoad/liveNodes;
						LOG.info("==========totalLoad======"+totalLoad+"==============");
						LOG.info("==========averageLoad======"+averageLoad+"==============");
						LOG.info("==========2*averageLoad======"+(averageLoad<<1)+"==============");
					}
				}
			}
			LOG.info("======blocks======" + total + "=============");
			

		}
		catch (IOException | URISyntaxException e1)
		{
			// close igore
		}
	}
}
