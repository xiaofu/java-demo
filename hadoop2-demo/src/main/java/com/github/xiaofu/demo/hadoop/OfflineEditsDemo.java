package com.github.xiaofu.demo.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.HdfsConfiguration;
import org.apache.hadoop.hdfs.server.namenode.FSNamesystem;
import org.apache.hadoop.hdfs.tools.offlineEditsViewer.OfflineEditsViewer;

public class OfflineEditsDemo {
	public static void main(String[] args) throws Exception {
		 OfflineEditsViewer.main(new String[]{"-i","edits_0000000000000001209-0000000000000001210","-o","edits.xml" });
		 
	}
	
}
