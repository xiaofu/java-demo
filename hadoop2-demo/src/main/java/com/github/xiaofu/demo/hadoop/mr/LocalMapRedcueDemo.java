/**
 * @ProjectName: hadoop2-tools
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2018年9月20日 下午3:47:17
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.hadoop.mr;

import java.io.IOException;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 可以打断点调试local模式的MR程序
 * 如果提交到集群，直接使用HADOOP JAR，简单方便
 * </p>
 * 
 * @author Administrator 2018年9月20日 下午3:47:17
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: Administrator 2018年9月20日
 * @modify by reason:{方法名}:{原因}
 */
public class LocalMapRedcueDemo extends Configured implements Tool
{
	static boolean isCluster = false;

	static class PrintMapper extends
			Mapper<Text, BytesWritable, NullWritable, NullWritable>

	{
		private static final Logger LOG = LoggerFactory
				.getLogger(PrintMapper.class);
		private static boolean ok = false;

		protected void setup(Context context) throws IOException,
				InterruptedException
		{
			System.out.println("=========setSetup======");
		}

		protected void map(Text key, BytesWritable value, Context context)
				throws IOException, InterruptedException
		{

			if (ok)
				return;
			LOG.info("==================中国人民");
			LOG.info("==================中国人民");
			LOG.info("==================中国人民");
			LOG.info("==================中国人民");
			LOG.info("==================中国人民");
			LOG.info("==================中国人民");
			context.getCounter("test", "不知道").increment(1);
			ok = true;
		}

	}

	@Override
	public int run(String[] args) throws Exception
	{
		Job job = Job.getInstance(getConf());
		job.setJobName(this.getClass().getSimpleName());
		job.setJarByClass(this.getClass());
		job.setNumReduceTasks(0);
		job.setMapperClass(PrintMapper.class);
		// job.setOutputKeyClass(LongWritable.class);
		// job.setOutputValueClass(Text.class);
		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setOutputFormatClass(NullOutputFormat.class);
		FileInputFormat.setInputPaths(job, args[0]);
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		if (!isCluster)
		{
			// local模式运行
			job.getConfiguration().set("fs.defaultFS", "file:///");
			job.getConfiguration().set("mapreduce.framework.name", "local");
		}
		job.getConfiguration().setBoolean(MRJobConfig.MAP_OUTPUT_COMPRESS,
				false);
		job.waitForCompletion(true);

		return 0;
	}

	public static void main(String[] args) throws Exception
	{

		// args = new String[2];
		// args[0] = "D:\\part-r-00000";
		// args[1] = "/tmp";
		isCluster = Boolean.valueOf(args[2]);
		int exitCode = ToolRunner.run(new LocalMapRedcueDemo(), args);
		System.exit(exitCode);
	}

}
