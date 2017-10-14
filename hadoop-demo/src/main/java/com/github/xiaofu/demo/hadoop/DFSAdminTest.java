/**
 * @ProjectName: hadoop-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2017年7月5日 下午1:03:26
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.hadoop;

import java.io.IOException;

import org.apache.hadoop.hdfs.tools.DFSAdmin;

/**
 * <p></p>
 * @author Administrator 2017年7月5日 下午1:03:26
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: Administrator 2017年7月5日
 * @modify by reason:{方法名}:{原因}
 */
public class DFSAdminTest
{

	/**
	 * @author Administrator 2017年7月5日 下午1:03:26
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		 DFSAdmin admin=new DFSAdmin();
		 admin.refreshNodes();

	}

}
