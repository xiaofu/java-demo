package com.github.xiaofu.demo.hadoop;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.protocol.HdfsConstants;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

public class IOSequenceFileDemo {
	private static final String[] DATA = {
		    "One, two, buckle my shoe",
		    "Three, four, shut the door",
		    "Five, six, pick up sticks",
		    "Seven, eight, lay them straight",
		    "Nine, ten, a big fat hen"
		  };
	public static void main(String[] args) throws IOException {
		String path="file:///temp/seqFile.seq";
		write(path);
		read(path);
	}	
	
	public static void read(String filePath) throws IOException
	{
		 Configuration conf = new Configuration();
		    FileSystem fs = FileSystem.get(URI.create(filePath), conf);
		    Path path = new Path(filePath);

		    SequenceFile.Reader reader = null;
		    try {
		      reader = new SequenceFile.Reader(fs, path, conf);
		      Writable key = (Writable)
		        ReflectionUtils.newInstance(reader.getKeyClass(), conf);
		      Writable value = (Writable)
		        ReflectionUtils.newInstance(reader.getValueClass(), conf);
		      long position = reader.getPosition();
		      while (reader.next(key, value)) {
		        String syncSeen = reader.syncSeen() ? "*" : "";
		        System.out.printf("[%s%s]\t%s\t%s\n", position, syncSeen, key, value);
		        position = reader.getPosition(); // beginning of next record
		      }
		    } finally {
		      IOUtils.closeStream(reader);
		    }
	}
	public static void write(String filePath) throws IOException
	{
		String uri =filePath;
		 
	    Configuration conf = new Configuration();
	    FileSystem fs = FileSystem.get(URI.create(uri), conf);
	    Path path = new Path(uri);
	    IntWritable key = new IntWritable();
	    Text value = new Text();
	    SequenceFile.Writer writer = null;
	    try {
	      writer = SequenceFile.createWriter(fs, conf, path,
	          key.getClass(), value.getClass());
	      for (int i = 0; i < 100; i++) {
	        key.set(100 - i);
	        value.set(DATA[i % DATA.length]);
	        System.out.printf("[%s]\t%s\t%s\n", writer.getLength(), key, value);
	        writer.append(key, value);
	      }
	    } finally {
	      IOUtils.closeStream(writer);
	    }
	}
}
