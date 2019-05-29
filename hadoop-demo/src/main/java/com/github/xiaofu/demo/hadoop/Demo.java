package com.github.xiaofu.demo.hadoop;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.FsShell;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsCreateModes;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.hdfs.DFSOutputStream;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.HdfsConfiguration;
import org.apache.hadoop.hdfs.client.HdfsDataOutputStream;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.protocol.LocatedBlock;
import org.apache.hadoop.hdfs.protocol.LocatedBlocks;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.JobID;
import org.apache.hadoop.mapreduce.TypeConverter;
import org.apache.hadoop.mapreduce.v2.api.records.JobId;
import org.apache.hadoop.mapreduce.v2.app.webapp.JobConfPage;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.NodeId;
import org.apache.hadoop.yarn.api.records.QueueInfo;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.server.resourcemanager.recovery.ZKRMStateStore;
import org.apache.hadoop.yarn.util.ConverterUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

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

		LocatedBlocks locateBlocks = client.getLocatedBlocks(path.toUri()
				.toString(), 0);

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
		conf.set("cqvip.dfs.favored.nodes", "vdatanode2:50010,vdatanode1:50010");
		DistributedFileSystem fs = (DistributedFileSystem) FileSystem.get(conf);
		FSDataOutputStream outputstream = fs.create(new Path("/user/test/t1"));
		outputstream.write(2);
		FileSystem.closeAll();
	}

	private static void writeTest(String[] args) throws IOException,
			InterruptedException
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

	public static void main(String[] args) throws Exception
	{
	 
		FileSystem fs = FileSystem.get(new Configuration());
		 fs.create(new Path("/user/flh/test"));
		 fs.close();
	}

}
