/**
 * 
 */
package com.github.xiaofu.demo.classpath;

import java.net.URL;

/**
 * @author fulaihua
 * 测试需要在classpath参数中如何指定才能读取到资源文件
 */
public class Demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassLoader loader= Demo.class.getClassLoader();
		 URL demoURL=loader.getResource("demo.log");
		 if (demoURL!=null) {
			 System.out.println(demoURL.toString());
		 }
	}

}
