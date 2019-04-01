package com.cqvip.runwork;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.client.impl.conf.ReaderConfigurationData;
import org.apache.pulsar.client.impl.schema.JSONSchema;
import org.apache.pulsar.common.api.EncryptionContext;

public class SchemaDemo
{

	static class SensorReading {
		String t1;
		String t2;
	}
	public static void main(String[] args) throws PulsarClientException
	{
		
		PulsarClient client = PulsarClient.builder()
		        .serviceUrl("pulsar://hadoop-auth-01:6650")
		        .build();

		final Producer<byte[]> producer = client.newProducer()
		        .topic("test-topic").enableBatching(false)
		        .sendTimeout(0, TimeUnit.SECONDS)
		        .producerName("producer2")
		        .create();
		 
		final int counts=10;
		Thread thread=new Thread(new   Runnable()
		{
			public void run()
			{
				 
				for (int i = 0; i < counts; i++)
				{
					try
					{
						 
						producer.send((""+i).getBytes());
						
					}
					catch (PulsarClientException  e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try
				{
					producer.close();
					 
				}
				catch (PulsarClientException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();
		final Consumer<byte[]> consumer = client.newConsumer()
		        .topic("test-topic")
		        .subscriptionName("topic-1")
		        .receiverQueueSize(0)
		        .subscriptionType(SubscriptionType.Exclusive)
		        .subscribe();
		Thread thread2=new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				while(true)
				{
					try
					{
						Message<byte[]>	message= consumer.receive();
						System.out.println(new String(message.getData()));
						consumer.acknowledge(message.getMessageId());
					}
					catch (PulsarClientException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				 
			}

			}
		});
		thread2.start();
		
		 
	}

}
