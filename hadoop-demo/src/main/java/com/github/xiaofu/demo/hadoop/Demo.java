package com.github.xiaofu.demo.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.protocol.LocatedBlock;
import org.apache.hadoop.hdfs.protocol.LocatedBlocks;

public class Demo {
	public static void main(String[] args) throws IOException {
		Path path = new Path("/vipcloud/journal/down_infos/current_vnamenode_2016-11-09");
		DistributedFileSystem fs = (DistributedFileSystem)  FileSystem.get(new Configuration());
		
		DFSClient client =fs.getClient();
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
}
