/**
 * @ProjectName: hadoop1
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2018年4月26日 上午11:12:35
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.hdfs.demo;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

/**
 * <p></p>
 * @author fulaihua 2018年4月26日 上午11:12:35
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2018年4月26日
 * @modify by reason:{方法名}:{原因}
 */
public class KerberosHdfsDemo
{
	public static  void main(String[] args) throws IOException
	{
		System.setProperty("java.security.krb5.conf", "d:/krb5.conf");
		//System.setProperty("sun.security.krb5.debug","true");
		Configuration conf = new Configuration();
         conf.set("hadoop.security.authentication", "kerberos");
         conf.set("fs.default.name", "hftp://node102.vipcloud:50070");
         UserGroupInformation.setConfiguration(conf);
         UserGroupInformation.loginUserFromKeytab("vipcloud/node102.vipcloud@CQVIP.COM","d:/vipcloud.keytab");
         try {
                 FileSystem fs = FileSystem.get(conf);
                 FileStatus[] fsStatus = fs.listStatus(new Path("/"));
                 for(int i = 0; i < fsStatus.length; i++){
                 System.out.println(fsStatus[i].getPath().toString());
                 }
         } catch (IOException e) {
                 e.printStackTrace();
         }
	}
}
