/**
 * @ProjectName: jdk-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2017年7月6日 上午9:32:23
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.aspectJ;

/**
 * <p></p>
 * @author fulaihua 2017年7月6日 上午9:32:23
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user:fulaihua 2017年7月6日
 * @modify by reason:{方法名}:{原因}
 */

	public class TestTarget {
	    public static void main (String[] args) {
	        System.out.println(">--------- Start test -----------<");
	        new TestTarget ().test () ;
	        System.out.println(">---------- End test -----------<");
	    }
	 
	    public void test () {
	        System.out.println ("TestTarget.test()") ;
	    }
	}
