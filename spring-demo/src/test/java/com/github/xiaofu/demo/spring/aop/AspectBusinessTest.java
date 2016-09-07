package com.github.xiaofu.demo.spring.aop;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.xiaofu.demo.spring.aop.config.AspectBusiness;

public class AspectBusinessTest {

	@Test
	public void test() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"aopConfiguration.xml");
		 
		 AspectBusiness business = (AspectBusiness) context.getBean("aspectBusiness");
	      //  business.delete("猫");
	        business.add("dog");
	}

}
