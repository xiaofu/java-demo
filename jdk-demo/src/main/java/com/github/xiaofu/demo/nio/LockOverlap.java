package com.github.xiaofu.demo.nio;


import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Random;

/**
 * Test overlapping locks on different file channels.
 *
 * @author Ron Hitchens (ron@ronsoft.com)
 */
public class LockOverlap
{
	public static void main (String [] argv)
		throws Exception
	{
		 
		File tempFile=File.createTempFile("tes", null);
		//String filename = argv [0];

		RandomAccessFile raf1 = new RandomAccessFile (tempFile, "rw");
		FileChannel fc1 = raf1.getChannel();

		RandomAccessFile raf2 = new RandomAccessFile (tempFile, "rw");
		FileChannel fc2 = raf2.getChannel();

		System.out.println ("Grabbing first lock");
		FileLock lock1 = fc1.lock (0L, 5, false);

		System.out.println ("Grabbing second lock");
		FileLock lock2 = fc2.lock (6, 10, false);

		System.out.println ("Exiting");
		tempFile.delete();
	}
}
