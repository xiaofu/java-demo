/**
 * 
 */
package com.github.xiaofu.demo.hadoop.mr;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.github.xiaofu.demo.hadoop.mr.NewMaxTemperature.NewMaxTemperatureMapper;
import com.github.xiaofu.demo.hadoop.mr.NewMaxTemperature.NewMaxTemperatureReducer;

/**
 * @author xiaofu
 * 
 */
public class WorkFlow {
	static {

		System.setProperty("hadoop.home.dir",
				"E:\\open-source-projects\\big-data\\hadoop\\install\\hadoop-2.3.0-cdh5.0.0");
	}
	static class ObjectMapper1 extends
			Mapper<LongWritable, Text, Text, LongWritable> {
		public void map(LongWritable key, Text value, /* [ */Context context/* ] */)
				throws IOException, /* [ */InterruptedException/* ] */{
			//reverse
			context.write(value,key);
		}
	}

	static class ObjectReduce1 extends
			Reducer<Text, LongWritable, NullWritable, Text> {
		public void reduce(Text key, Iterable<LongWritable> values,
				Context context) throws IOException, InterruptedException {
		 
			context.write(NullWritable.get(),key);
		}
	}

	 

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err
					.println("Usage: NewMaxTemperature <input path> <output path>");
			System.exit(-1);
		}
		Job job1=new Job();
		job1.setJobName("job1");
		job1.setMapperClass(NewMaxTemperatureMapper.class);
		job1.setReducerClass(NewMaxTemperatureReducer.class);
	
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(IntWritable.class);
		
		FileInputFormat.addInputPath(job1, new Path(args[0]));
		FileOutputFormat.setOutputPath(job1, new Path(args[1]));
		
		job1.getConfiguration().set("fs.defaultFS", "file:///");
		job1.getConfiguration().set("mapred.job.tracker", "local");
		job1.getConfiguration().setBoolean(MRJobConfig.MAP_OUTPUT_COMPRESS,
				false);
		
		Job job2 = new Job();
		 job2.setJobName("job2");
		job2.setMapperClass(ObjectMapper1.class);
		job2.setReducerClass(ObjectReduce1.class);
		job2.setMapOutputKeyClass(Text.class);
		job2.setMapOutputValueClass(LongWritable.class);
		job2.setOutputKeyClass(NullWritable.class);
		job2.setOutputValueClass(Text.class);
		
		FileInputFormat.addInputPath(job2, new Path("/tmp/test"));
		FileOutputFormat.setOutputPath(job2, new Path("/tmp/test1"));
		
		job2.getConfiguration().set("fs.defaultFS", "file:///");
		job2.getConfiguration().set("mapred.job.tracker", "local"); 
		job2.getConfiguration().setBoolean(MRJobConfig.MAP_OUTPUT_COMPRESS,
				false);
		//此玩意适合放到一个线程去跑，然后在外面不断的调用allFinished方法来轮询所有任务是否完成
		JobControl jct=new JobControl("test");
		 
		 ControlledJob cj1=  new ControlledJob(job1.getConfiguration());
		 ControlledJob cj2=new ControlledJob(job2.getConfiguration());
		 
		 cj2.addDependingJob( cj1);
		 //必须把所有需要进行控制的ControlledJob放入JobControl中，因为ControlledJob之间只是体现依赖关系，而在JobControl中启动时并不会从某个项的最底层依赖开始启动
		 //而是在它的集合中循环查找启动
		 jct.addJob(cj1);
		 jct.addJob(cj2);
		  //这样跑是记远不会退出此方法的
		 jct.run();
		  
	}

}
