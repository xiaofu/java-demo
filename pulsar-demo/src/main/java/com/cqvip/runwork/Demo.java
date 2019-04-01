package com.cqvip.runwork;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.ConsumerEventListener;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

public class Demo
{
	public static void main(String[] args) throws PulsarClientException
	{
		 
		PulsarClient client = PulsarClient.builder()
		        .serviceUrl("pulsar://192.168.30.139:6650")
		        .build();
		Consumer<byte[]> cs= client.newConsumer().topic("my-topic").subscribe();
		 Message<byte[]> ms= cs.receive();
		 
		 
	}
}
