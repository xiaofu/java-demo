package com.cqvip.runwork;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class Demo
{

	public static void main(String[] args) throws IOException
	{
		
		FileSystem fs=FileSystem.get(new Configuration());
		/*
		 * fs.create(new Path("/test/test2/file")); fs.close();
		 */
		FileContext fc =FileContext.getFileContext(fs.getUri());
		FileSystem.enableSymlinks();
		fc.createSymlink(new Path("/test/test2/file"), new Path("/link/linktest"), true);
	}

}
