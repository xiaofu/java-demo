/**
 * @ProjectName: jdk-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2018年4月13日 上午11:24:23
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo;
import com.sun.security.auth.module.Krb5LoginModule;

import javax.security.auth.Subject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
/**
 * <p></p>
 * @author fulaihua 2018年4月13日 上午11:24:23
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2018年4月13日
 * @modify by reason:{方法名}:{原因}
 */
public class KerberosDemo
{
	 private void loginImpl(final String propertiesFileName) throws Exception {

	        System.out.println(System.getProperty("java.version"));

	        System.setProperty("sun.security.krb5.debug", "true");
	        System.setProperty("java.security.krb5.conf", "d:/krb5.conf");
	        final Subject subject = new Subject();

	        final Krb5LoginModule krb5LoginModule = new Krb5LoginModule();
	        final Map<String,String> optionMap = new HashMap<String,String>();

	        if (propertiesFileName == null) {
	            //optionMap.put("ticketCache", "/tmp/krb5cc_1000");
	            optionMap.put("keyTab", "d:/test.keytab");
	            optionMap.put("principal", "test@CQVIP.COM"); // default realm

	            optionMap.put("doNotPrompt", "true");
	            optionMap.put("refreshKrb5Config", "true");
	            optionMap.put("useTicketCache", "true");
	            optionMap.put("renewTGT", "true");
	            optionMap.put("useKeyTab", "true");
	            optionMap.put("storeKey", "true");
	            optionMap.put("isInitiator", "true");
	        } else {
	            File f = new File(propertiesFileName);
	            System.out.println("======= loading property file ["+f.getAbsolutePath()+"]");
	            Properties p = new Properties();
	            InputStream is = new FileInputStream(f);
	            try {
	                p.load(is);
	            } finally {
	                is.close();
	            }
	            optionMap.putAll((Map)p);
	        }
	        optionMap.put("debug", "true"); // switch on debug of the Java implementation

	        krb5LoginModule.initialize(subject, null, new HashMap<String,String>(), optionMap);
	        
	        boolean loginOk = krb5LoginModule.login();
	        System.out.println("======= login:  " + loginOk);

	        boolean commitOk = krb5LoginModule.commit();
	        System.out.println("======= commit: " + commitOk);
	        System.out.println("======= Subject: " + subject);
	    }
	/**
	 * @author fulaihua 2018年4月13日 上午11:24:23
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		 
		 System.out.println("A property file with the login context can be specified as the 1st and the only paramater.");
	        final KerberosDemo krb = new KerberosDemo();
	        krb.loginImpl(args.length == 0 ? null : args[0]);
	}

}
