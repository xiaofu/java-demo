package com.github.xiaofu.demo.hadoop;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileChecksum;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.HdfsConfiguration;
import org.apache.hadoop.hdfs.protocol.LocatedBlocks;

public class Demo
{
	static
	{
		System.setProperty("HADOOP_USER_NAME", "vipcloud");
	}

	private static void fileRead() throws IllegalArgumentException, IOException
	{

		Path path = new Path("/temp/libvipudf.so");
		DistributedFileSystem fs = (DistributedFileSystem) FileSystem
				.get(new Configuration());

		DFSClient client = fs.getClient();

		LocatedBlocks locateBlocks = client
				.getLocatedBlocks(path.toUri().toString(), 0);

		for (BlockLocation blockLocation : fs.getFileBlockLocations(path, 0,
				fs.getLength(path)))
		{
			for (String host : blockLocation.getHosts())
			{
				System.out.println("replication host:" + host);

			}
			for (String name : blockLocation.getCachedHosts())
			{
				System.out.println("replication cached host:" + name);
			}
			for (String name : blockLocation.getTopologyPaths())
			{
				System.out.println("replication TopologyPath:" + name);
			}
			for (String name : blockLocation.getNames())
			{
				System.out.println("replication ip:data_port:" + name);
			}
			for (String name : blockLocation.getStorageIds())
			{
				System.out.println("storeid:" + name);
			}
		}
	}

	private static void favorNodes() throws IOException
	{
		Configuration conf = new Configuration();
		conf.set("cqvip.dfs.favored.nodes",
				"vdatanode2:50010,vdatanode1:50010");
		DistributedFileSystem fs = (DistributedFileSystem) FileSystem.get(conf);
		FSDataOutputStream outputstream = fs.create(new Path("/user/test/t1"));
		outputstream.write(2);
		FileSystem.closeAll();
	}

	private static void writeTest(String[] args)
			throws IOException, InterruptedException
	{
		Configuration conf = new HdfsConfiguration();
		conf.setLong("dfs.client.socket-timeout", 5 * 60 * 1000);
		final DistributedFileSystem fs = (DistributedFileSystem) FileSystem
				.get(conf);
		FSDataOutputStream outStream = fs.create(new Path(args[0]));
		BufferedInputStream inputStream = new BufferedInputStream(
				new FileInputStream(args[1]));
		byte[] bytes = new byte[8192];
		int counts = 0;
		counts = inputStream.read(bytes);
		while (counts != -1)
		{
			outStream.write(bytes, 0, counts);
			counts = inputStream.read(bytes);
			// Thread.currentThread().sleep(100);
		}
		inputStream.close();
		outStream.close();
	}
	private static void flushAndRead() throws InterruptedException, IOException
	{
		final FileSystem fs = FileSystem.get(new Configuration());
		Thread thread1 = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				FSDataOutputStream stream;
				try
				{
					stream = fs.create(new Path("/tmp/testfile"));
					for (int i = 0; i < 10000; i++)
					{

						stream.write(("abc" + i).getBytes());
						stream.hflush();
						Thread.sleep(500);

					}
				}
				catch (IllegalArgumentException | IOException
						| InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		thread1.start();
		Thread.sleep(5000);
		Thread thread2 = new Thread(new Runnable()
		{

			@Override
			public void run()
			{

				try
				{
					while (true)
					{

						FSDataInputStream stream = fs
								.open(new Path("/tmp/testfile"));
						byte[] bytes = new byte[1024];
						int counts = stream.read(bytes);
						while (counts != -1)
						{
							System.out.println(new String(bytes, 0, counts));
							counts = stream.read(bytes);
						}

						Thread.sleep(500);
					}

				}
				catch (IllegalArgumentException | IOException
						| InterruptedException e)
				{

				}
			}

		});
		thread2.start();
	}
	public static void main(String[] args) throws Exception
	{

		 flushAndRead();
	}

}
