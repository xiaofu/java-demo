package com.github.xiaofu.demo.cxf.programmatically;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

import com.github.xiaofu.demo.cxf.web.Customer;

public class JsonClient {
	public static void main(String[] args) throws Exception {
		System.out.println("Sent HTTP GET request to query customer info");

		GetMethod get = new GetMethod(
				"http://localhost:9000/customerservice/customers/123");
		get.addRequestHeader("Accept", "application/json");
		HttpClient httpclient = new HttpClient();
		try {
			int result = httpclient.executeMethod(get);
			System.out.println("Response status code: " + result);
			System.out.println("Response body: ");
			System.out.println(get.getResponseBodyAsString());
		} finally {
			// Release current connection to the connection pool once you are
			// done
			get.releaseConnection();
		}
		System.out.println("\n");
		System.out.println("Sent HTTP POST request to query customer info");
		PostMethod post = new PostMethod(
				"http://localhost:9000/customerservice/customers");
		Customer customer = new Customer();
		customer.setId(2);
		customer.setName("xiaofu");
		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();
		JsonGenerator gen = new JsonFactory().createJsonGenerator(sw);
		mapper.writeValue(gen, customer);
		gen.close();

		post.addRequestHeader("Accept", "application/json");
		RequestEntity entity = new StringRequestEntity(sw.toString(),
				"application/json", "UTF-8");
		post.setRequestEntity(entity);
		httpclient = new HttpClient();
		try {
			int result = httpclient.executeMethod(post);
			System.out.println("Response status code: " + result);
			System.out.println("Response body: ");
			System.out.println(post.getResponseBodyAsString());
		} finally {
			// Release current connection to the connection pool once you are
			// done
			post.releaseConnection();
		}
	}
}
