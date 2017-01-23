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
}
