package com.github.xiaofu.demo.hadoop.mr;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 远程提交到集群上去运行
 * @author xiaofu
 * 
 */
public class MultipleFileOutputDriver extends Configured implements Tool {

	enum KeyCounter{
		MapRecords,
		ReduceKeyCounts
	}
	
	static class DemoMapper extends Mapper<LongWritable, Text, Text, Text> {

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] fields = value.toString().split(Pattern.quote("|"));
			context.getCounter(KeyCounter.MapRecords).increment(1);
			context.write(new Text(fields[0]), value);
		}
	}

	static class DemoReducer extends Reducer<Text, Text, NullWritable, Text> {
		private MultipleOutputs<NullWritable, Text> outputs;

		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			context.getCounter(KeyCounter.ReduceKeyCounts).increment(1);
			for (Text value : values) {
				// 此方法是使用了当前reduce上下文以及输出配置包装了一个上下文，只是可以指定输出名而已，而且这个文件名可以加/，它表达一个路径，前面是路径
				// ，最后是文件名。但是这里标准输出还是会创建一个空文件，使用lazy包装输出!
				//outputs.write(NullWritable.get(), value, key.toString());
				// 以下是另起了一个上下文和指定名称的输出配置，如果单独输出这些路径，无法将临时目录中的数据移动到正式目录
				//outputs.write("userid", NullWritable.get(), value, "userid"
				//		+ "/" + key.toString());
				// 改变了根路径，变成了test2,这里在path内部组合父路径和子路径时发现它使用的是uri进行的解析，所以发现子路径是绝对路径就直接返回了子
				//outputs.write("test2", NullWritable.get(), value, "/test2/"
//+ key.toString());
			}
		}

		protected void setup(Context context) throws IOException,
				InterruptedException {
			outputs = new MultipleOutputs<NullWritable, Text>(context);
		}

		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			outputs.close();
		}
	}

	@Override
	public int run(String[] args) throws Exception {
		Job job = Job.getInstance(getConf());
	//	job.getConfiguration().set("fs.hdfs.impl", "fs.hdfs.impl=org.apache.hadoop.hdfs.DistributedFileSystem");;
		job.setJobName(this.getClass().getSimpleName());
		job.setJarByClass(this.getClass());
		job.setJar("d:\\open-source-projects\\github\\java-demo-parent\\hadoop-demo\\target\\hadoop-demo-0.0.1-SNAPSHOT.jar");
		job.setMapperClass(DemoMapper.class);
		job.setReducerClass(DemoReducer.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.getConfiguration().set(MRJobConfig.MAP_JAVA_OPTS, "-Xmx2048m");
		job.getConfiguration().set(MRJobConfig.MAP_MEMORY_MB, "2048");
		job.getConfiguration().set(MRJobConfig.REDUCE_MEMORY_MB, "2048");
		job.getConfiguration().set(MRJobConfig.REDUCE_JAVA_OPTS, "-Xmx2048m");
		job.getConfiguration().set(MRJobConfig.MR_AM_ENV, "CLASSPATH=job.jar,LD_LIBRARY_PATH=/usr/local/vipcloud");
		job.setMaxMapAttempts(1);
		job.setMaxReduceAttempts(2);
		job.setInputFormatClass(TextInputFormat.class);
		LazyOutputFormat.setOutputFormatClass(job, TextOutputFormat.class);
		job.setOutputFormatClass(LazyOutputFormat.class);
		job.setOutputKeyClass(NullWritable.class);
		FileInputFormat.setInputPaths(job,
				"/user/hive/warehouse/tmp_view_infos/");
		FileOutputFormat.setOutputPath(job, new Path("/data"));
		MultipleOutputs.addNamedOutput(job, "userid", TextOutputFormat.class,
				NullWritable.class, Text.class);
		MultipleOutputs.addNamedOutput(job, "test2", TextOutputFormat.class,
				NullWritable.class, Text.class);
		FileSystem fs=FileSystem.get(job.getConfiguration());
		fs.delete(new Path("/data"), true);
		job.waitForCompletion(true);

		return 0;
	}

	public static void main(String[] args) throws Exception {
		//System.setProperty("HADOOP_USER_NAME", "vipcloud");
		int exitCode = ToolRunner.run(new MultipleFileOutputDriver(), args);
		System.exit(exitCode);
	}
}
