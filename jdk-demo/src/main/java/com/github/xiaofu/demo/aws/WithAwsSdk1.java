package com.github.xiaofu.demo.aws;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class WithAwsSdk1
{
	private static String bucketName="abc";
	private static String keyName="abc";
	private static AmazonS3 s3Client;
	public static void main(String[] args) throws IOException
	{
         s3Client = AmazonS3ClientBuilder
                .standard()
                .build();

        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            File file = new File("pom.xml");
            // 上传文件
            s3Client.putObject(new PutObjectRequest(bucketName, keyName, file));

            // 下载文件
            GetObjectRequest rangeObjectRequest = new GetObjectRequest(bucketName, keyName);
            S3Object objectPortion = s3Client.getObject(rangeObjectRequest);
            System.out.println("Printing bytes retrieved:");
            displayTextInputStream(objectPortion.getObjectContent());
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " + "means your request made it "
                    + "to Amazon S3, but was rejected with an error response" + " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());

        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " + "means the client encountered " + "an internal error while trying to "
                    + "communicate with S3, " + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());

        }
	}
	 private static void displayTextInputStream(InputStream input) throws IOException {
	        // 按行读取并打印。
	        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	        while (true) {
	            String line = reader.readLine();
	            if (line == null)
	                break;

	            System.out.println("    " + line);
	        }
	        System.out.println();
	    }
}
