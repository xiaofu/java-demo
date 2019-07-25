package com.github.xiaofu.demo.hadoop;

import java.io.IOException;
import java.net.URI;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.ReflectionUtils;

public class IOMapFileDemo {
	private static final String[] DATA = {
		    "One, two, buckle my shoe",
		    "Three, four, shut the door",
		    "Five, six, pick up sticks",
		    "Seven, eight, lay them straight",
		    "Nine, ten, a big fat hen"
		  };
	public static void main(String[] args) throws IOException
	{
			String uri = "file:///temp/mapFile.map";
		  write(uri);
		  read(uri);
		  
	}
	
	public static void read(String filePath) throws IOException
	{
		Configuration conf = new Configuration();
	    FileSystem fs = FileSystem.get(URI.create(filePath), conf);
	    Path path = new Path(filePath);

	    MapFile.Reader reader = null;
	    try {
	      reader = new MapFile.Reader(fs,filePath, conf);
	      WritableComparable key = (WritableComparable)
	        ReflectionUtils.newInstance(reader.getKeyClass(), conf);
	      Writable value = (Writable)
	        ReflectionUtils.newInstance(reader.getValueClass(), conf);
	      while (reader.next(key, value)) {
	        System.out.printf("\t%s\t%s\n", key, value);
	      }
	    } finally {
	      IOUtils.closeStream(reader);
	    }
	}
	public static void write(String filePath) throws IOException
	{
		Random ran=new Random();
		  Configuration conf = new Configuration();
		    FileSystem fs = FileSystem.get(URI.create(filePath), conf);

		    IntWritable key = new IntWritable();
		    Text value = new Text();
		    MapFile.Writer writer = null;
		    try {
		      writer = new MapFile.Writer(conf, fs, filePath,
		          key.getClass(), value.getClass());
		      for (int i = 0; i < 1024; i++) {
		        key.set(i + 1);
		        value.set(DATA[i % DATA.length]);
		        writer.append(key, value);
		      }
		    } finally {
		      IOUtils.closeStream(writer);
		    }
	}
}
