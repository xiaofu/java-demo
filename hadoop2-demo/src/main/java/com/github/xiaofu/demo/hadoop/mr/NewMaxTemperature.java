package com.github.xiaofu.demo.hadoop.mr;

// cc NewMaxTemperature Application to find the maximum temperature in the weather dataset using the new context objects MapReduce API
import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.LocalJobRunner;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

// vv NewMaxTemperature
public class NewMaxTemperature {
	/**
	 * 本地运行需要SHELL，所以需要指定HADOOP目录
	 */
	static {

		System.setProperty("hadoop.home.dir",
				"D:\\open-source-projects\\big-data\\Hadoop\\src\\hadoop-2.6.0-cdh5.10.0\\hadoop-dist\\target\\hadoop-2.6.0-cdh5.10.0");
	}

	static class NewMaxTemperatureMapper
	/* [ */extends Mapper<LongWritable, Text, Text, IntWritable>/* ] */{

		private static final int MISSING = 9999;

		public void map(LongWritable key, Text value, /* [ */Context context/* ] */)
				throws IOException, /* [ */InterruptedException/* ] */{
			 
			String line = value.toString();
			String year = line.substring(15, 19);
			int airTemperature;
			if (line.charAt(87) == '+') { // parseInt doesn't like leading plus
											// signs
				airTemperature = Integer.parseInt(line.substring(88, 92));
			} else {
				airTemperature = Integer.parseInt(line.substring(87, 92));
			}
			String quality = line.substring(92, 93);
			if (airTemperature != MISSING && quality.matches("[01459]")) {
				/* [ */context.write/* ] */(new Text(year), new IntWritable(
						airTemperature));

			}
		}
	}

	static class NewMaxTemperatureReducer
	/* [ */extends Reducer<Text, IntWritable, Text, IntWritable>/* ] */{

		public void reduce(Text key, /* [ */
				Iterable/* ] */<IntWritable> values,
				/* [ */Context context/* ] */) throws IOException, /* [ */
		InterruptedException/* ] */{

			int maxValue = Integer.MIN_VALUE;
			for (IntWritable value : values) {
				maxValue = Math.max(maxValue, value.get());
			}
			/* [ */context.write/* ] */(key, new IntWritable(maxValue));
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err
					.println("Usage: NewMaxTemperature <input path> <output path>");
			System.exit(-1);
		}

		/* [ */Job job = new Job();
		job.setJarByClass(NewMaxTemperature.class);/* ] */

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		 
		job.setMapperClass(NewMaxTemperatureMapper.class);
		job.setReducerClass(NewMaxTemperatureReducer.class);
		FileInputFormat.setMinInputSplitSize(job, 1024*1024*256);
		job.getConfiguration().setInt(MRJobConfig.IO_SORT_MB, 4);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.getConfiguration().set("fs.defaultFS", "file:///");
		job.getConfiguration().set("mapreduce.framework.name", "local");
		 
		job.getConfiguration().setBoolean(MRJobConfig.MAP_OUTPUT_COMPRESS,
			
				false);
		//job.submit();
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
// ^^ NewMaxTemperature
