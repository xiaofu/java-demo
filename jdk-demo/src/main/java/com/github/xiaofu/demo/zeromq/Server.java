/**
 * @ProjectName: jdk-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2017年9月22日 上午10:16:53
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.zeromq;

import org.zeromq.ZMQ;

/**
 * <p></p>
 * @author fulaihua 2017年9月22日 上午10:16:53
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2017年9月22日
 * @modify by reason:{方法名}:{原因}
 */
public class Server
{
	public  static void main(String[] args)
	{
		ZMQ.Context context = ZMQ.context(1);  
        ZMQ.Socket socket = context.socket(ZMQ.REP);  
        socket.bind("tcp://*:5555");  
        while (true) {  
            byte[] request;  
            // 等待下一个client端的请求  
            // 等待一个以0结尾的字符串  
            // 忽略最后一位的0打印  
            request = socket.recv(0);  
            System.out.println("Received request: [" + new String(request, 0, request.length - 1) + "]");  
  
            try {  
                Thread.sleep(1000);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
  
            // 返回一个最后一位为0的字符串到客户端  
            String replyString = "World" + " ";  
            byte[] reply = replyString.getBytes();  
            reply[reply.length - 1] = 0;  
            socket.send(reply, ZMQ.NOBLOCK);  
        }  
	}
}
