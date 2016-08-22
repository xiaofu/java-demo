package com.github.xiaofu.demo.cxf.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
/**
 * targetNamespace 与spring 配置文件中的 xmlns不一致客户调用会产生一些麻烦，就是生成的名空间不一致！！尽量保持一致。
 * targetNamespace要么和配置文件中的名称空间一致，要么都不配置.
 */
@WebService(name="HelloWorld",portName="HelloWorld",serviceName="HelloWorld",targetNamespace="http://service.jaxws.cxf.apache.org/service")
public interface HelloWorld {
	@WebMethod
    String sayHi(String text);
	@WebMethod
	int test(String a,int[] types,int b);
	@WebMethod
	Results test2(String p1);
}