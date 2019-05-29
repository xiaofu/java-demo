/**
 * @ProjectName: kafka-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2019年5月16日 下午4:53:40
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.cqvip.runwork.demo;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * <p>
 * </p>
 * 
 * @author fulaihua 2019年5月16日 下午4:53:40
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2019年5月16日
 * @modify by reason:{方法名}:{原因}
 */
public class Demo
{

	/**
	 * @author fulaihua 2019年5月16日 下午4:53:41
	 * @param args
	 */
	public static void main(String[] args)
	{
        Properties props = new Properties();
        props.put("bootstrap.servers", "hadooplearn:9092");
        props.put("metadata.broker.list","hadooplearn:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        // 发送业务消息
        // 读取文件 读取内存数据库 读socket端口
        for (int i = 1; i <= 100; i++) {
              try {
                    Thread.sleep(500);
              } catch (InterruptedException e) {
                    e.printStackTrace();
              }
              producer.send(new ProducerRecord<String, String>("wordcount",
                          i+" said "+ i + " love you baby for " + i + " times,will you have a nice day with me tomorrow"));
             
        }
	}

}
 
