package com.github.xiaofu.demo.minio;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public class WithAwsSk2
{
	private static String bucket1="abc";
	private static S3Client s3;
	public static void main(String[] args)
			throws InvalidKeyException, NoSuchAlgorithmException, IOException, URISyntaxException
	{
		AwsCredentials credentials = AwsBasicCredentials.create("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
		S3Configuration configuration = S3Configuration.builder()
				.pathStyleAccessEnabled(true)
				.checksumValidationEnabled(false)//windows docker返回的etag有-,造成无法解析,所以需要添加这一行,实现环境没有测试，在线版本没有这个问题!
				.build();

		 s3 = S3Client.builder().region(Region.US_EAST_1)
				.serviceConfiguration(configuration)
				.endpointOverride(new URI("http://127.0.0.1:9000"))
				.credentialsProvider(StaticCredentialsProvider.create(credentials))
				.build();
		createBucket(bucket1);
		putObject();
	}
	private static void putObject()
	{
		PutObjectRequest putObjectRequest=PutObjectRequest.builder().bucket(bucket1).key("def").build();
		PutObjectResponse response= s3.putObject(putObjectRequest,Paths.get("pom.xml"));
		//System.out.println(response.);
	}
 
	private static  void createBucket(String bucket)
	{
		// Create bucket
		CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
				.bucket(bucket)
				.build();
		s3.createBucket(createBucketRequest);

		ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder()
				.build();
		ListBucketsResponse listBucketsResponse = s3
				.listBuckets(listBucketsRequest);
		listBucketsResponse.buckets().stream()
				.forEach(x -> System.out.println(x.name()));

	}

}
