/**
 * @ProjectName: crunch-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2018年1月16日 下午1:15:19
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.example;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

import org.apache.crunch.DoFn;
import org.apache.crunch.Emitter;
import org.apache.crunch.FilterFn;
import org.apache.crunch.MapFn;
import org.apache.crunch.PCollection;
import org.apache.crunch.PGroupedTable;
import org.apache.crunch.PTable;
import org.apache.crunch.Pair;
import org.apache.crunch.Pipeline;
import org.apache.crunch.PipelineResult;
import org.apache.crunch.fn.Aggregators;
import org.apache.crunch.impl.mr.MRPipeline;
import org.apache.crunch.io.From;
import org.apache.crunch.lib.PTables;
import org.apache.crunch.types.PType;
import org.apache.crunch.types.writable.Writables;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * <p></p>
 * @author fulaihua 2018年1月16日 下午1:15:19
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2018年1月16日
 * @modify by reason:{方法名}:{原因}
 */
public class ZlfDataExport extends Configured implements Tool,Serializable
{
	 public static void main(String[] args) throws Exception {
	        ToolRunner.run(new Configuration(), new ZlfDataExport(), args);
	    }

	    public int run(String[] args) throws Exception {

	        if (args.length != 2) {
	            System.err.println("Usage: hadoop jar crunch-demo-1.0-SNAPSHOT-job.jar"
	                                      + " [generic options] input output");
	            System.err.println();
	            GenericOptionsParser.printGenericCommandUsage(System.err);
	            return 1;
	        }

	        String inputPath = args[0];
	        String outputPath = args[1];

	        // Create an object to coordinate pipeline creation and execution.
	        MRPipeline pipeline = new MRPipeline(ZlfDataExport.class, getConf());
	        PType<XXXXObject> customType=  Writables.derived(XXXXObject.class, new MapFn<BytesWritable, XXXXObject>()
	    			{
	    				@Override
	    				public XXXXObject map(BytesWritable input)
	    				{
	    					XXXXObject tt=new XXXXObject();
	    					try
							{
								VipcloudUtil.DeserializeObject(input.getBytes(),tt);
							}
							catch (IOException e)
							{
								throw new RuntimeException(e);
							}
	    					return tt;
	    				}
	    			}, new MapFn<XXXXObject, BytesWritable>()
	    			{
	    				@Override
	    				public BytesWritable map(XXXXObject input)
	    				{
	    					BytesWritable bytes= null;
	    					try
							{
	    						bytes= new BytesWritable(VipcloudUtil.SerializeObject(input));
							}
							catch (IOException e)
							{
								throw new RuntimeException(e);
							}
	    					return bytes;
	    				}
	    			}, Writables.writables(BytesWritable.class));
	       
	        PTable<String,XXXXObject> titleinfoTable= pipeline.read(From.sequenceFile(inputPath, Writables.strings(),customType));
	        //调用一下groupbyKey ，为了产生reduce阶段代码
	        // Take the collection of words and remove known stop words.
	        PTable<String, String> dataResult = titleinfoTable.filter(new FilterFn<Pair<String,XXXXObject>>()
			{
				
				@Override
				public boolean accept(Pair<String, XXXXObject> input)
				{
					String type= input.second().data.get("type");
					return  type!=null && type.equalsIgnoreCase("2");
				}
			}).parallelDo(new MapFn<Pair<String,XXXXObject>, Pair<String,String>>()
			{
				@Override
				public Pair<String,String> map(Pair<String, XXXXObject> input)
				{
					return new Pair<String,String>( input.first(),input.second().data.get("type"));
				}
			},  Writables.tableOf(Writables.strings(), Writables.strings())).groupByKey(1).parallelDo(new MapFn<Pair<String,Iterable<String>>, Pair<String,String>>()
			{

				@Override
				public Pair<String,String> map(Pair<String, Iterable<String>> input)
				{
					Iterator<String> iter= input.second().iterator();
					iter.hasNext();
					return new Pair(input.first(),iter.next());
				}
			}, Writables.tableOf(Writables.strings(), Writables.strings()));
	        
	       
	        pipeline.writeTextFile(dataResult, outputPath);
	        

	        // Execute the pipeline as a MapReduce.
	        PipelineResult result = pipeline.done();

	        return result.succeeded() ? 0 : 1;
	    }

}
