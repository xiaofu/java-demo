package com.cqvip.runwork;

import org.apache.pulsar.client.api.AuthenticationFactory;
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
		        .authentication(
		                AuthenticationFactory.token("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UifQ.ipevRNuRP6HflG8cFKnmUPtypruRC4fb1DWtoLL62SY"))
		            .build();
		Consumer<byte[]> cs= client.newConsumer().topic("my-topic").subscribe();
		 Message<byte[]> ms= cs.receive();
		 
		 
	}
}
