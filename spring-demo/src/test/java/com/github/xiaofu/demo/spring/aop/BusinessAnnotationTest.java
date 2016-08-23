package com.github.xiaofu.demo.spring.aop;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.xiaofu.demo.spring.aop.annotation.BusinessAnnotation;


public class BusinessAnnotationTest {

	@Test
	public void test() {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"aopAnnotation.xml");
        BusinessAnnotation business = (BusinessAnnotation) context.getBean("businessAnnotation");
        business.delete("猫");
	}

}
