package com.github.xiaofu.demo.cxf.ws;

import javax.jws.WebService;

 
public class HelloWorldImpl implements HelloWorld {

	@Override
	public String sayHi(String text) {
		System.out.println("sayHi called");
		return "Hello " + text;
	}

	@Override
	public int test(String a,int[] types,int b) {
		 
		return types.length;
	}

	@Override
	public Results test2(String p1) {
		Results results=new Results();
		results.setStr(p1);
		return results;
	}
	
	

}
