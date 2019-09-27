package com.github.xiaofu.demo.embeded.jetty;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.component.LifeCycle.Listener;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;


/**
 * 
 */

/**
 * @author xiaofu
 * 
 */
public class Demo
{
	private static int port = 8080;// default port

	private Server server;
	private Connector listener;

	/**
	 * 开始
	 * 
	 * @author fulaihua 2012-9-7 下午12:30:46
	 * @throws Exception
	 */
	public void startServer() throws Exception
	{

		initServer();
		// listener.open();
		publishService();

	}

	/**
	 * 停止
	 * 
	 * @author fulaihua 2012-9-7 下午12:30:56
	 * @throws Exception
	 */
	public void stop() throws Exception
	{
		if (server != null)
		{

			server.stop();

		}
	}

	/**
	 * 初始化server
	 * 
	 * @author fulaihua 2012-9-7 下午12:31:06
	 * @throws Exception
	 */
	public void initServer() throws Exception
	{
		server = new Server(createThreadPool());
		// add connector
		listener = createConnector(server);
		server.addConnector(listener);
		server.setStopAtShutdown(true);
	}

	/**
	 * 添加Listener
	 * 
	 * @author fulaihua 2012-9-7 下午12:31:22
	 * @return
	 * @throws Exception
	 */
	private Connector createConnector(Server server)
	{
		ServerConnector connector = new ServerConnector(server);
		connector.setHost("localhost");
		connector.setPort(port);
	    
		connector.setAcceptQueueSize(128);

		return connector;
	}

	private QueuedThreadPool createThreadPool()
	{
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMaxThreads(200);
		threadPool.setMinThreads(10);
		return threadPool;
	}

	/**
	 * 使用的热布署方式，但是这里还是会有一个BUG，那就是扫描线程正在卸载，把webapphandler移出集合，新的集合还没有添加进来，而外面的请求已经
	 * 到来，发现找不到handler，就会发生404
	 * 
	 * @throws Exception
	 */
	private void publishService() throws Exception
	{

		ServletHolder holder = new ServletHolder();
		TestServlet jerseyServlet = new TestServlet();
		holder.setServlet(jerseyServlet);

		ServletContextHandler contextHandler = new ServletContextHandler();
		contextHandler.addServlet(holder, "/*");
		server.setHandler(contextHandler);
		server.start();
		server.join();

	}

	/**
	 * @author fulaihua 2014-5-15 上午10:27:55
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		new Demo().startServer();
	}

	private static class TestServlet extends HttpServlet
	{
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException
		{
			((Request)req).setQueryEncoding("GB2312");
			System.out.println(req.getParameter("FileName"));
		}
	}
}
