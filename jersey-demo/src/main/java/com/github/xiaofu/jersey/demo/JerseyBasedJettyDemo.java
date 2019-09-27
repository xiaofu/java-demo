package com.github.xiaofu.jersey.demo;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.jetty.server.Server;

import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.jetty.servlet.JettyWebContainerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;

public class JerseyBasedJettyDemo
{

	private static int port = 9200;// default port

	private Server server;

	public void startServer() throws Exception
	{

		server = new Server(port);
		server.setStopAtShutdown(true);

		// ContextHandlerCollection contexts = new ContextHandlerCollection();

		ResourceConfig resourceConfig = new ResourceConfig(
				HelloWorldResource.class);
		ServletHolder holder = new ServletHolder();
		ServletContainer jerseyServlet = new ServletContainer(resourceConfig);
		holder.setServlet(jerseyServlet);

		ServletContextHandler contextHandler = new ServletContextHandler();
		contextHandler.addServlet(holder, "/*");

		// contexts.addHandler(contextHandler);
		// server.setHandler(contextHandler);
		server.start();

	}

	public void stop() throws Exception
	{
		if (server != null)
		{
			server.stop();

		}
	}

	/**
	 * 
	 * @author flh 2019年9月27日 上午10:30:39
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		//1. 这是使用了jetty 容器，并且需要servlet的创建服务的方式
		Map<String, String> initParams = new HashMap<>();
		initParams.put(ServerProperties.PROVIDER_PACKAGES,
				"com.github.xiaofu.jersey.demo");
		initParams.put(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL, Level.INFO.toString());
		JettyWebContainerFactory.create("http://localhost:8080/",
				ServletContainer.class,initParams);
		
		// 2.这是使用了jetty 容器，并且需要servlet,自定义封装servlet
		// new JerseyBasedJettyDemo().startServer();
		
		/*
		 *3.
		 * 这是使用了jetty 容器，但是不需要servlet的创建服务的方式
		 * JettyHttpContainerFactory.createServer( new
		 * URI("http://localhost:8080/"),resourceConfig,true );
		 */

	}
}
