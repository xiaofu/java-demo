package com.github.xiaofu.demo.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class S3FSDemo {

	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		conf.set("fs.s3a.connection.ssl.enabled", "false");
		conf.set("fs.s3a.access.key", "AKIA6KDVMQXG4665SAFU");
		conf.set("fs.s3a.secret.key", "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");
		conf.set("fs.s3a.endpoint", "s3.cn-northwest-1.amazonaws.com.cn");
		conf.set("fs.defaultFS", "s3a://aws-yinzhaohui-test-data");
		FileSystem fs=FileSystem.get(conf);
		FileStatus[] fileStatus= fs.listStatus(new Path("/TestData"));
		for (FileStatus fileStatus2 : fileStatus) {
			System.out.println(fileStatus2.getPath());
		}
				
	}
}
