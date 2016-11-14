package com.github.xiaofu.demo;

import java.net.HttpURLConnection;
import java.net.URL;

import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.transport.http.CommonsHttpMessageSender;

import com.fasterxml.jackson.databind.ObjectMapper;

public class VipConstraintJsonValidate {
	public static void   main(String[] args) {
		Client client = null;
		try {

			HttpURLConnection httpConnection = (HttpURLConnection) (new URL(
					"http://node901.vipcloud:8080/viplogin/services/VCubeService?wsdl")).openConnection();
			httpConnection.setReadTimeout(120 * 1000);// 设置http连接的读超时,单位是毫秒
			httpConnection.connect();
			client = new Client(httpConnection.getInputStream(), null);
		} catch (Exception e) {
			e.printStackTrace();

		}
		client.setProperty(CommonsHttpMessageSender.HTTP_TIMEOUT, String.valueOf(120 * 100));// 设置发送的超时限制,单位是毫秒;
		client.setProperty(CommonsHttpMessageSender.DISABLE_KEEP_ALIVE, "true");
		client.setProperty(CommonsHttpMessageSender.DISABLE_EXPECT_CONTINUE, "true");
		ObjectMapper mapper = new ObjectMapper();
		for (int i = 1; i < 100; i++) {
			Object[] results = null;
			try {
				results = client.invoke("obtainSingleRegionCfgInfo", new Object[] { i + "" });
				mapper.readTree(results[0].toString());
			} catch (Exception e) {
				System.out.println("=======regionId:" + i);
				System.out.println(results[0]);
			}
		}

	}
}
