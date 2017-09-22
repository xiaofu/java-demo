/**
 * @ProjectName: jdk-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2017年9月22日 上午10:21:01
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.zeromq;

import org.zeromq.ZMQ;

/**
 * <p></p>
 * @author fulaihua 2017年9月22日 上午10:21:01
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2017年9月22日
 * @modify by reason:{方法名}:{原因}
 */
public class Client
{
	public static void main(String[] args)
	{
		 ZMQ.Context context = ZMQ.context(1);  
	        ZMQ.Socket socket = context.socket(ZMQ.REQ);  
	        socket.connect("tcp://localhost:5555");  
	  
	        for (int request_nbr = 0; request_nbr <= 10; request_nbr++) {  
	            // 创建一个末尾为0的字符串  
	            String requestString = "Hello" + " ";  
	            byte[] request = requestString.getBytes();  
	            request[request.length - 1] = 0;  
	            // 发送  
	            socket.send(request, ZMQ.NOBLOCK);  
	  
	            // 获得返回值  
	            byte[] reply = socket.recv(0);  
	            // 输出去掉末尾0的字符串  
	            System.out.println("Received reply " + request_nbr + ": [" + new String(reply, 0, reply.length - 1) + "]");  
	        }  
	}
}
