/**
 * 
 */
package com.github.xiaofu.demo.cxf.ws;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.message.Message;

/**
 * @author xiaofu
 * 
 */
public class CxfServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 

		  //can use ServerFactoryBean
		  HelloWorldImpl implementor = new HelloWorldImpl();
		  JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
		  svrFactory.setServiceClass(HelloWorld.class);
		  svrFactory.setAddress("http://localhost:8080/webService/HelloWorld");
		  svrFactory.setServiceBean(implementor);
		/*  svrFactory.getInInterceptors().add(new LoggingInInterceptor());
		  svrFactory.getOutInterceptors().add(new LoggingOutInterceptor());*/
		  svrFactory.create();
		 
		 
		
	}
}
