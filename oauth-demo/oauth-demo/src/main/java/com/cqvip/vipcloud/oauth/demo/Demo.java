/**
 * @ProjectName: oauth-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2018年9月7日 下午12:39:01
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.cqvip.vipcloud.oauth.demo;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * <p>
 * </p>
 * 
 * @author Administrator 2018年9月7日 下午12:39:01
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: Administrator 2018年9月7日
 * @modify by reason:{方法名}:{原因}
 */
public class Demo
{
   public static String CALLBACK="http://192.168.20.24:8080/client/code";
	/**
	 * @author Administrator 2018年9月7日 下午12:39:01
	 * @param args
	 */
	public static void main(String[] args)
	{
		OAuth20Service service = new ServiceBuilder("key").apiSecret("secret")
				.callback(CALLBACK).debug().build(CasApi.instance());

		System.out.println("Now go and authorize ScribeJava here:");
		System.out.println(service.getAuthorizationUrl());
		System.out.println("And paste the verifier here");
		System.out.print(">>");
		 
		 
	}

}
