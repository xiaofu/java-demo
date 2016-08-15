package com.github.xiaofu.demo.cxf.ws;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.dynamic.DynamicClientFactory;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsClientFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.message.Message;
import org.springframework.http.converter.xml.Jaxb2CollectionHttpMessageConverter;

public class CxfClient {
	public static void main(String[] args) throws Exception
	{
		//不得生成服务端的类
		//ClientProxyFactoryBean
		/*JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.getInInterceptors().add(new LoggingInInterceptor());
		factory.getOutInterceptors().add(new LoggingOutInterceptor());
		factory.setServiceClass(HelloWorld.class);
		factory.setAddress("http://localhost:9100/helloWorld");
		HelloWorld client = (HelloWorld) factory.create();
		int t=client.test("a", new int[]{1,3}, 2);
		System.out.println(t);
		String reply = client.sayHi("HI");
		System.out.println("Server said: " + reply);*/
		 
		//不得生成 服务端的类
		//ClientFactoryBean
		/*JaxWsClientFactoryBean clientFactory=new JaxWsClientFactoryBean();
		clientFactory.setAddress("http://localhost:9100/helloWorld");
		clientFactory.setServiceClass(HelloWorld.class);
		Client client= clientFactory.create();
		Object[] results=client.invoke("test", "a",new int[]{2,3},2);
		System.out.println(results[0]);*/
		
		
		//DynamicClientFactory 
		//JaxWsDynamicClientFactory
		//会生成服务端的类
		JaxWsDynamicClientFactory clientFactory= JaxWsDynamicClientFactory.newInstance();
		Client client=clientFactory.createClient("http://localhost:9100/helloWorld?wsdl");
		Map<String, List<String>> headers = new HashMap<String, List<String>>();
		headers.put("userName", Arrays.asList("aaaa"));
		headers.put("pwd", Arrays.asList("123456"));
		client.getRequestContext().put(Message.PROTOCOL_HEADERS, headers);
		client.getOutInterceptors().add(new LoggingOutInterceptor());
		Object[] results= client.invoke("sayHi","hello");
		System.out.println(results[0]);
	}
}
