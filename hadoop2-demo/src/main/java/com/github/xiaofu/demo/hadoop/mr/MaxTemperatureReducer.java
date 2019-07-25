package com.github.xiaofu.demo.hadoop.mr;

 
// == MaxTemperatureReducerV1
import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
 

// vv MaxTemperatureReducerV1
public class MaxTemperatureReducer 
  extends Reducer<Text, IntWritable, Text, IntWritable> {

  public void reduce(Text key, Iterator<IntWritable> values,
      Context context)
      throws IOException, InterruptedException {
    
    int maxValue = Integer.MIN_VALUE;
    while (values.hasNext()) {
      maxValue = Math.max(maxValue, values.next().get());
    }
    context.write(key, new IntWritable(maxValue));
  }
}
// ^^ MaxTemperatureReducerV1
