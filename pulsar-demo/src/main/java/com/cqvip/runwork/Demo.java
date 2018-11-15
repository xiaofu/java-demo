package com.cqvip.runwork;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

public class Demo
{
	public static void main(String[] args) throws PulsarClientException
	{
		PulsarClient client = PulsarClient.builder()
		        .serviceUrl("pulsar://hadoop-auth-02:6650")
		        .build();
		Producer<byte[]> producer = client.newProducer()
		        .topic("my-topic")
		        .create();
		producer.send("test".getBytes());
	}
}
