/**
 * 
 */
package com.github.xiaofu.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author fulaihua
 * 
 */
public class FileDemo
{

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{

		 File dirFile=new File("/usr/local/vipcloud/data/data1");
		 System.out.println(dirFile.getAbsolutePath());
		 System.out.println(dirFile.getAbsoluteFile().toURI().toString());
		 System.out.println(dirFile.getCanonicalPath());
		 System.out.println("totalSpace:"+ dirFile.getTotalSpace());
	}

}
