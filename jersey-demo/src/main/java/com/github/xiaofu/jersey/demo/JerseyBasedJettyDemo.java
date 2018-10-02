package com.github.xiaofu.jersey.demo;

import org.eclipse.jetty.server.Server;
 
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
 

  

public class JerseyBasedJettyDemo {

	private static int port = 9200;// default port

	private Server server;
 
	/**
	 * ��ʼ
	 * 
	 * @author fulaihua 2012-9-7 ����12:30:46
	 * @throws Exception
	 */
	public void startServer() throws Exception {

		server = new Server(port);
		server.setStopAtShutdown(true);
		 
		//ContextHandlerCollection contexts = new ContextHandlerCollection();
		 
		ResourceConfig resourceConfig = new ResourceConfig(HelloWorldResource.class);
		ServletHolder holder=new ServletHolder();
		ServletContainer jerseyServlet=new ServletContainer(resourceConfig);
		holder.setServlet(jerseyServlet);
		//ר�Ŵ���SERVLET��һ��������
		ServletContextHandler contextHandler=new ServletContextHandler();
		contextHandler.addServlet(holder, "/*");
		//contexts.addHandler(contextHandler);
		server.setHandler(contextHandler);
		server.start();
		
	}

	/**
	 * ֹͣ
	 * 
	 * @author fulaihua 2012-9-7 ����12:30:56
	 * @throws Exception
	 */
	public void stop() throws Exception {
		if (server != null) {
			server.stop();

		}
	}
 

	/**
	 * @author fulaihua 2014-5-15 ����10:27:55
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new JerseyBasedJettyDemo().startServer();
	}
}
