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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsCreateModes;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.hdfs.DFSOutputStream;
import org.apache.hadoop.hdfs.DistributedFileSystem;
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
import org.apache.hadoop.yarn.util.ConverterUtils;
 


import org.apache.tools.ant.taskdefs.TempFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Demo
{

	private static void fileRead() throws IllegalArgumentException, IOException
	{
		Path path = new Path(
				"/temp/libvipudf.so");
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

	private static void initialSocket()
	{
		YarnConfiguration conf = new YarnConfiguration();
		final InetSocketAddress initialAddress = conf.getSocketAddr(
				YarnConfiguration.NM_BIND_HOST, YarnConfiguration.NM_ADDRESS,
				YarnConfiguration.DEFAULT_NM_ADDRESS,
				YarnConfiguration.DEFAULT_NM_PORT);
		boolean usingEphemeralPort = (initialAddress.getPort() == 0);
		if (usingEphemeralPort)
		{
			throw new IllegalArgumentException(
					"Cannot support recovery with an "
							+ "ephemeral server port. Check the setting of "
							+ YarnConfiguration.NM_ADDRESS);
		}
		System.out.println(initialAddress.getPort());
		System.out.println(initialAddress);
	}

	public static void main(String[] args) throws IOException, IllegalArgumentException, YarnException
	{
		 
		Configuration conf=new Configuration();
		conf.set("fs.client.htrace.span.receiver.classes", "StandardOutSpanReceiver");
		conf.set("fs.client.htrace.sampler.classes", "AlwaysSampler");
		
		DistributedFileSystem fs = (DistributedFileSystem) FileSystem
				.get(conf);
		FileStatus[] statuses= fs.listStatus(new Path("/vipcloud/jobs"));
		File tmpFile=new File("/tmp");
		tmpFile.mkdir();
		for (FileStatus status : statuses)
		{
			fs.copyToLocalFile (status.getPath(), new Path("/tmp"));
		}
		/*Path path = new Path(
				"/temp/t9");
		//fs.listStatus(path);
		fs.create(path);*/
		//fs.mkdirs(path);
		//fs.delete(path,true);
	}
	 
}
