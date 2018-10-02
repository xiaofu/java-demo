package com.github.xiaofu.jersey.demo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

@Path("helloworld")
public class HelloWorldResource {
	public static final String CLICHED_MESSAGE = "Hello World!";

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getHello() {
		return CLICHED_MESSAGE;
	}

	 
}