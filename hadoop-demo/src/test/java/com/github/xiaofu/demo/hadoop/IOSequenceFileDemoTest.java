package com.github.xiaofu.demo.hadoop;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IOSequenceFileDemoTest {

	 private static final String SF_URI = "file:///temp/seqFile.seq";
	  private FileSystem fs;
	  private SequenceFile.Reader reader;
	  private Writable key;
	  private Writable value;

	  @Before
	  public void setUp() throws IOException {
	   IOSequenceFileDemo.main(new String[] { SF_URI });
	    
	    Configuration conf = new Configuration();
	    fs = FileSystem.get(URI.create(SF_URI), conf);
	    Path path = new Path(SF_URI);

	    reader = new SequenceFile.Reader(fs, path, conf);
	    key = (Writable) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
	    value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);
	  }
	  
	  @After
	  public void tearDown() throws IOException {
	    fs.delete(new Path(SF_URI), true);
	  }

	  @Test
	  public void seekToRecordBoundary() throws IOException {
	    // vv SequenceFileSeekAndSyncTest
	    reader.seek(359);
	    assertEquals(reader.next(key, value), true);
	    assertEquals(((IntWritable) key).get(), 95);
	    //assertThat(reader.next(key, value), true);
	    //assertThat(((IntWritable) key).get(), is(95));
	    // ^^ SequenceFileSeekAndSyncTest
	  }
	  
	  @Test(expected=IOException.class)
	  public void seekToNonRecordBoundary() throws IOException {
	    // vv SequenceFileSeekAndSyncTest-SeekNonRecordBoundary
	    reader.seek(360);
	    reader.next(key, value); // fails with IOException
	    // ^^ SequenceFileSeekAndSyncTest-SeekNonRecordBoundary
	  }
	  
	  @Test
	  public void syncFromNonRecordBoundary() throws IOException {
	    // vv SequenceFileSeekAndSyncTest-SyncNonRecordBoundary
	    reader.sync(2022);
	    assertEquals(reader.getPosition(), (2021L));
	    assertEquals(reader.next(key, value), (true));
	    assertEquals(((IntWritable) key).get(), (59));
	    // ^^ SequenceFileSeekAndSyncTest-SyncNonRecordBoundary
	  }
	  
	  @Test
	  public void syncAfterLastSyncPoint() throws IOException {
	    reader.sync(4557);
	    assertEquals(reader.getPosition(), (4788L));
	    assertEquals(reader.next(key, value), (false));
	  }
}
