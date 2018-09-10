/**
 * @ProjectName: oauth-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2018年9月7日 下午12:17:38
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.cqvip.vipcloud.oauth.demo;

import com.github.scribejava.core.builder.api.ClientAuthenticationType;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenExtractor;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Verb;

/**
 * <p>
 * </p>
 * 
 * @author Administrator 2018年9月7日 下午12:17:38
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: Administrator 2018年9月7日
 * @modify by reason:{方法名}:{原因}
 */
public class CasApi extends DefaultApi20
{
	protected CasApi()
	{
		
	}
	public ClientAuthenticationType getClientAuthenticationType() {
	        return ClientAuthenticationType.REQUEST_BODY;
	}
	 
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OAuth2AccessTokenExtractor.instance();
    }
	private static class InstanceHolder
	{
		private static final CasApi INSTANCE = new CasApi();
	}

	public static CasApi instance()
	{
		return InstanceHolder.INSTANCE;
	}

	@Override
	public String getAccessTokenEndpoint()
	{
		return "http://192.168.20.24:8090/cas/oauth2.0/accessToken";
	}

	@Override
	protected String getAuthorizationBaseUrl()
	{
		return "http://192.168.20.24:8090/cas/oauth2.0/authorize";
	}

}