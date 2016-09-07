package com.github.xiaofu.demo.spring.orm.jpa;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	/**
	 * TODO:使用jtds 中的在保存二进制流对象时会报错，有些方法没有实现
	 * @param args
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"jpaSpring.xml");
		UserService service = ctx.getBean("userService", UserService.class);
		service.createNewAccount("ZhangJianPing", "123456", 1);

	}

}
