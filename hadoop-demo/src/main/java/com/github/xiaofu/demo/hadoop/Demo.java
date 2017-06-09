package com.github.xiaofu.demo.hadoop;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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


	private static void fileRead() throws IllegalArgumentException, IOException
	{
		Path path = new Path("/user/flh/HRegion/modify_title_info_zt/part-m-00000");
		DistributedFileSystem fs = (DistributedFileSystem)  FileSystem.get(new Configuration());
	
		DFSClient client =fs.getClient();
		 
	 
		LocatedBlocks locateBlocks = client.getLocatedBlocks(path.toUri().toString(), 0);
		for (LocatedBlock locateBlock : locateBlocks.getLocatedBlocks()) {
			for (DatanodeInfo datanodeInfo : locateBlock.getLocations()) {
			 
			}

		}
		
		for (BlockLocation blockLocation : fs.getFileBlockLocations(path, 0, fs.getLength(path))) {
			for (String host : blockLocation.getHosts()) {
				System.out.println("replication host:" + host);
				
			}
			for (String name : blockLocation.getCachedHosts()) {
				System.out.println("replication cached host:" + name);
			}
			for (String name : blockLocation.getTopologyPaths()) {
				System.out.println("replication TopologyPath:" + name);
			}
			for (String name : blockLocation.getNames()) {
				System.out.println("replication ip:data_port:" + name);
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
		 fileRead();
	 
	}
}
