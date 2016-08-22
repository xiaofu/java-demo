package com.github.xiaofu.demo.cxf.programmatically;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.ext.search.SearchContextProvider;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.github.xiaofu.demo.cxf.web.BooksService;
import com.github.xiaofu.demo.cxf.web.CustomerService;
import com.github.xiaofu.demo.cxf.web.PersonServiceImpl;

/**
 * 需要引入cxf-rt-transports-http-jetty
 * 
 * @author xiaofu
 *
 */
public class WithoutSpringService {
	public static void main(String[] args) {
		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		// disable this functionality
		// context props
	/*	Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("fiql.support.single.equals.operator", "true");
		maps.put("search.use.all.query.component", "true");
		sf.setProperties(maps);*/
		
		sf.setResourceClasses(PersonServiceImpl.class, CustomerService.class,
				BooksService.class);
		sf.setProvider(new SearchContextProvider());
		sf.setProvider(new JacksonJsonProvider());
		sf.setResourceProvider(PersonServiceImpl.class,
				new SingletonResourceProvider(new PersonServiceImpl()));
		sf.getInInterceptors().add(new LoggingInInterceptor());
		sf.getOutInterceptors().add(new LoggingOutInterceptor());
		sf.setAddress("http://localhost:9000/");
		// 官网上说加下面代码可以不用spring,但是我不加一样可以不用spring
		/*
		 * BindingFactoryManager manager =
		 * sf.getBus().getExtension(BindingFactoryManager.class);
		 * JAXRSBindingFactory factory = new JAXRSBindingFactory();
		 * factory.setBus(sf.getBus());
		 * manager.registerBindingFactory(JAXRSBindingFactory.JAXRS_BINDING_ID,
		 * factory);
		 */
		sf.create();
	}
}
