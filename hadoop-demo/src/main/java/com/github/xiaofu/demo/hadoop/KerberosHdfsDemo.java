/**
 * @ProjectName: hadoop-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2018年4月16日 下午3:13:11
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

/**
 * <p>
 * 可以shell 脚本登陆再执行程序，这样程序里面不需要进行登陆。
 * </p>
 * @author fulaihua 2018年4月16日 下午3:13:11
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2018年4月16日
 * @modify by reason:{方法名}:{原因}
 */
public class KerberosHdfsDemo
{

	/**
	 * @author fulaihua 2018年4月16日 下午3:13:11
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		testWithShellLogin();
	}
	public static void testWithShellLogin() throws IOException
	{
		 
		  
			 Configuration conf = new Configuration();
	         conf.set("hadoop.security.authentication", "kerberos");
	         try {
	                 FileSystem fs = FileSystem.get(conf);
	                 FileStatus[] fsStatus = fs.listStatus(new Path("/yarn"));
	                 for(int i = 0; i < fsStatus.length; i++){
	                 System.out.println(fsStatus[i].getPath().toString());
	                 }
	         } catch (IOException e) {
	                 e.printStackTrace();
	         }
	}
	
	public static void testWithUserGroupInformationLogin(String krb5Conf) throws IOException
	{
		 
		  	System.setProperty("java.security.krb5.conf", krb5Conf);
			//System.setProperty("sun.security.krb5.debug","true");
			Configuration conf = new Configuration();
			
	         conf.set("hadoop.security.authentication", "kerberos");
	         UserGroupInformation.setConfiguration(conf);
	         UserGroupInformation.loginUserFromKeytab("test@CQVIP.COM","test.keytab");
	         try {
	                 FileSystem fs = FileSystem.get(conf);
	                 FileStatus[] fsStatus = fs.listStatus(new Path("/yarn"));
	                 for(int i = 0; i < fsStatus.length; i++){
	                 System.out.println(fsStatus[i].getPath().toString());
	                 }
	         } catch (IOException e) {
	                 e.printStackTrace();
	         }
	}

}
