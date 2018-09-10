package com.cqvip.vipcloud.oauth.demo;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.SessionManager;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.oauth.OAuthService;
import com.github.scribejava.httpclient.apache.ApacheHttpClient;

/**
 * Servlet implementation class OAuthTest
 */
public class CodeServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	// private static String URL = "http://192.168.20.24:8080/client/OAuthTest";

	/**
	 * Default constructor.
	 */
	public CodeServlet()
	{

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		OAuth20Service service = new ServiceBuilder("key").apiSecret("secret")
				.callback(Demo.CALLBACK).debug().build(CasApi.instance());
		String code = request.getParameter("code");
		try
		{
			OAuth2AccessToken accessToken= service.getAccessToken(code);
			System.out.println("Got the Access Token!");
			System.out.println("(The raw response looks like this: "
					+ accessToken.getRawResponse() + "')");
			System.out.println();
		}
		catch (InterruptedException | ExecutionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{

	}

}
