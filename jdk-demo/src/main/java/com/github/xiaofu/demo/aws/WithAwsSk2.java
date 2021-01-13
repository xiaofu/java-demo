package com.github.xiaofu.demo.aws;

import java.nio.file.Paths;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public class WithAwsSk2
{
	private static String bucket1="你的桶";
	private static S3Client s3;
	public static void main(String[] args)
	{
		
		 s3 = S3Client.builder()
				.build();
		 
		putObject();
	}
	private static void putObject()
	{
		PutObjectRequest putObjectRequest=PutObjectRequest.builder().bucket(bucket1).key("def").build();
		PutObjectResponse response= s3.putObject(putObjectRequest,Paths.get("pom.xml"));
		//System.out.println(response.);
	}
 

}
