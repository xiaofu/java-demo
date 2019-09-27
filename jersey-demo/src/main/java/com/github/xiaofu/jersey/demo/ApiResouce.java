/**
 * @ProjectName: download-paper
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2018年10月11日 下午5:27:02
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.jersey.demo;

import java.util.HashMap;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.research.ws.wadl.Request;

/**
 * <p>
 * </p>
 * 
 * @author fulaihua 2018年10月11日 下午5:27:02
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2018年10月11日
 * @modify by reason:{方法名}:{原因}
 */
@Path("/api")
@Singleton
public class ApiResouce
{
	public static final String V1_VERSION = "v1";
	public static final String CURRENT_VERSION = "v2";
 
	

	@GET
	@Path("/version")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCurrentVersion()
	{
		HashMap<String,String> map=new HashMap<>();
		map.put("version", CURRENT_VERSION);
		return Response.ok(map).build();
	}
	
	@Path("/token")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getToken(@Context
    		HttpServletRequest reqeust)
	{
		System.out.println(reqeust.getParameter("abc"));
		return "1234";
	}
}
