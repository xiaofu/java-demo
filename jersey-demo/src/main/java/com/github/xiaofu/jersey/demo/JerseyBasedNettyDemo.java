package com.github.xiaofu.jersey.demo;

 
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.ProcessingException;

import io.netty.channel.Channel;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;

public class JerseyBasedNettyDemo {

	public static void main(String[] args) throws ProcessingException, URISyntaxException {
		ResourceConfig config =new ResourceConfig(); 
		config.register(HelloWorldResource.class);
		config.register(LoggingFeature.class);
		final Channel server = NettyHttpContainerProvider.createServer(new URI("http://localhost:8080/"), config, false);

	}

}
