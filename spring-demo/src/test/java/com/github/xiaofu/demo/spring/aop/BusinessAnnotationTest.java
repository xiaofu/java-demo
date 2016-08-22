package com.github.xiaofu.demo.spring.aop;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BusinessAnnotationTest {

	@Test
	public void test() {

        ApplicationContext context = new ClassPathXmlApplicationContext("aopConfiguration.xml");
        BusinessAnnotation business = (BusinessAnnotation) context.getBean("businessAnnotation");
        business.delete("猫");
	}

}
