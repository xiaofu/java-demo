package com.github.xiaofu.demo.hadoop;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.protocol.LocatedBlock;
import org.apache.hadoop.hdfs.protocol.LocatedBlocks;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

public class Demo {
	static {

		System.setProperty("hadoop.home.dir",
				"D:\\open-source-projects\\big-data\\Hadoop\\windows\\hadoop-2.6.0-cdh5.9.0");
	}
	private static void test()
	{
		 YarnConfiguration conf=new YarnConfiguration();
		 String aa=conf.get(YarnConfiguration.RM_SCHEDULER);
			System.out.println(conf.get(YarnConfiguration.RM_SCHEDULER, YarnConfiguration.DEFAULT_RM_SCHEDULER));
			System.out.println("==============");
	}
	private static void fileRead() throws IllegalArgumentException, IOException
	{
		Path path = new Path("/user/hive/warehouse/view_down_infos/year=2016/month=12/day=1/catalog=1/virtual=0/44906a4b993c25e-57b63834353b50a6_771350048_data.0");
		DistributedFileSystem fs = (DistributedFileSystem)  FileSystem.get(new Configuration());
	
		DFSClient client =fs.getClient();
		fs.mkdirs(new Path("/temp"),null);
	 
		LocatedBlocks locateBlocks = client.getLocatedBlocks(path.toUri().toString(), 0);
		for (LocatedBlock locateBlock : locateBlocks.getLocatedBlocks()) {
			for (DatanodeInfo datanodeInfo : locateBlock.getLocations()) {
			 
			}

		}
		for (BlockLocation blockLocation : client.getBlockLocations(path.toUri().toString(), 0, fs.getLength(path))) {
			for (String host : blockLocation.getHosts()) {
				System.out.println("host:" + host);
				
			}
			for (String name : blockLocation.getNames()) {
				System.out.println("host:" + name);
			}
		}
	}
	private static void initialSocket()
	{
		YarnConfiguration conf=new YarnConfiguration();
		   final InetSocketAddress initialAddress = conf.getSocketAddr(
			        YarnConfiguration.NM_BIND_HOST,
			        YarnConfiguration.NM_ADDRESS,
			        YarnConfiguration.DEFAULT_NM_ADDRESS,
			        YarnConfiguration.DEFAULT_NM_PORT);
		   boolean usingEphemeralPort = (initialAddress.getPort() == 0);
		    if (usingEphemeralPort) {
		      throw new IllegalArgumentException("Cannot support recovery with an "
		          + "ephemeral server port. Check the setting of "
		          + YarnConfiguration.NM_ADDRESS);
		    }
		    System.out.println(initialAddress.getPort());
		    System.out.println(initialAddress);
	}
	public static void main(String[] args) throws IOException {
		initialSocket();
	}
}
