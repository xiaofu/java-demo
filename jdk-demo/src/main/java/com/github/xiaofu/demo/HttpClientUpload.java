package com.github.xiaofu.demo;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.RequestExpectContinue;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.util.EntityUtils;

public class HttpClientUpload
{
	public static void main(String[] args)
	{
		String url=args[0];
		String fileName=args[1];
		HttpPost postMethod = new HttpPost(url);
		int status = 0;
		try
		{
			File file = new File(fileName);
			 FileBody bin = new FileBody(file);
			 
			 HttpEntity reqEntity = MultipartEntityBuilder.create()
	                    .addPart("bin", bin)
	                    .addTextBody("flag", "1")
	                    .addTextBody("siteid", "100")
	                    .addTextBody("expiretime", "0")
	                    .addTextBody("splitColumnSizes", "-1")
	                    .build();

			postMethod.setEntity(reqEntity);
			postMethod.setHeader("upload.user", "vipUser");
			postMethod.setHeader("upload.pwd", "005c4b5be817b79cf37d7bc2301cd544");
			
			CloseableHttpClient httpclient = HttpClients.custom().setHttpProcessor(HttpProcessorBuilder.create().addAll(
	                    new RequestContent(true),
	                    new RequestTargetHost(),
	                    new RequestExpectContinue()).build()).build();
		 
			CloseableHttpResponse response =httpclient.execute(postMethod);
			System.out.println(EntityUtils.toString(response.getEntity()));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
