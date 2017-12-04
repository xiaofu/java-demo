/**
 * @ProjectName: jdk-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2017年12月1日 下午4:06:42
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.httpclient;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.RequestExpectContinue;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * <p></p>
 * @author fulaihua 2017年12月1日 下午4:06:42
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2017年12月1日
 * @modify by reason:{方法名}:{原因}
 */
public class Demo
{
	 static final String SESSIONS_URI = "/sessions";
	 private static final String APPLICATION_JSON = "application/json";
	public static void  main(String[] args)
	{
		ObjectMapper mapper=new ObjectMapper();
		//HttpPost postMethod = new HttpPost(uploadUrl);
		HttpPost httpMethod=new HttpPost("http://vdatanode1:8998/batches");
		CloseableHttpClient httpclient = HttpClients.custom().setHttpProcessor(HttpProcessorBuilder.create().addAll(
                new RequestContent(true),
                new RequestTargetHost(),
                new RequestExpectContinue()).build()).build();
		int status = 0;
		try
		{
			
			 ObjectNode obj=mapper.createObjectNode();
			 ArrayNode array=mapper.createArrayNode();
			 array.add("hdfs://mrcluster/user/flh/SparkDemo-0.0.1-SNAPSHOT.jar");
			 obj.put("file","/user/Administrator/SparkDemo-0.0.1-SNAPSHOT.jar");
			 //obj.set("jars",array);
			 obj.put("className","com.cqvip.spark.demo.sql.SparkSqlDemo");
			 byte[] bodyBytes = mapper.writeValueAsBytes(obj);
			 httpMethod.setEntity(new ByteArrayEntity(bodyBytes));
			
			
			CloseableHttpResponse response =httpclient.execute(httpMethod);
			System.out.println(EntityUtils.toString(response.getEntity()));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
