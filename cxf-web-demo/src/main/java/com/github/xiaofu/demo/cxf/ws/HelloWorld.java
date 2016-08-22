package com.github.xiaofu.demo.cxf.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(name="HelloWorld",serviceName="HelloWorld",targetNamespace="http://com.github.xiaofu.demo.cxf.ws/HelloWorld")
public interface HelloWorld {
	@WebMethod
    String sayHi(String text);
	@WebMethod
	int test(String a,int[] types,int b);
	@WebMethod
	Results test2(String p1);
}