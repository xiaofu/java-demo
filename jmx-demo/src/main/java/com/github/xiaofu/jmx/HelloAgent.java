package com.github.xiaofu.jmx;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import com.sun.jdmk.comm.HtmlAdaptorServer;

public class HelloAgent {

	public static void main(String[] args) throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();      
        ObjectName helloName = new ObjectName("Hello:name=HelloWorld");    
        Hello hello=new Hello();          
        server.registerMBean(hello, helloName);       
       
        Jack jack = new Jack();    //重点   
        server.registerMBean(jack, new ObjectName("Jack:name=jack"));    //重点   
        jack.addNotificationListener(new HelloListener(), null, hello);    //重点   
      
        
        ObjectName adapterName = new ObjectName("HelloAgent:name=htmladapter,port=8082");      
        HtmlAdaptorServer adapter = new HtmlAdaptorServer();       
        server.registerMBean(adapter, adapterName);      
        adapter.start();           
       
        System.out.println("start.....");     

	}

}
