/**
 * @ProjectName: hbase-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2019年12月16日 下午2:20:22
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.hbase;

import org.apache.hadoop.hbase.util.hdfsblockbalancer.HBaseBlockBalancer;

/**
 * <p></p>
 * @author flh 2019年12月16日 下午2:20:22
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: flh 2019年12月16日
 * @modify by reason:{方法名}:{原因}
 */
public class HbaseBalancerDemo
{

	/**
	 * @author flh 2019年12月16日 下午2:20:22
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.setProperty("HADOOP_USER_NAME", "vipcloud");
		 HBaseBlockBalancer.main(new String[0]);

	}

}
