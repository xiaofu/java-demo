package com.github.xiaofu.demo.hadoop;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewFsDemo
{
	private static final Logger LOG = LoggerFactory.getLogger(ViewFsDemo.class);
	private static final String CONGFIG_PROPERTY_FILE = "mapreduce.properties";

	public static Properties getCustomProperties() throws Exception
	{

		String propertiesPath = System.getProperty("user.dir") + File.separator
				+ CONGFIG_PROPERTY_FILE;
		File file = new File(propertiesPath);
		if (!file.exists())
		{
			file = new File(CONGFIG_PROPERTY_FILE);
			LOG.warn(
					"no config mapreduce properties ,will use classpath config "
							+ file.getAbsolutePath());
			// throw new Exception("conf file must exist");
		}

		Properties prop = new Properties();
		prop.load(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		LOG.info("config properties loaded ");
		return prop;
	}

	public static void main(String[] args) throws Exception
	{
		Configuration conf = new Configuration();
		for (Entry<Object, Object> entry : getCustomProperties().entrySet())
		{
			conf.set(entry.getKey().toString(), entry.getValue().toString());
		}
		FileSystem fs = FileSystem.get(conf);
		FileStatus[]  status= fs.listStatus(new Path("/SolrIndex"));
		for (FileStatus fileStatus : status)
		{
			System.out.println(fileStatus.getPath());
		}
	}

}
