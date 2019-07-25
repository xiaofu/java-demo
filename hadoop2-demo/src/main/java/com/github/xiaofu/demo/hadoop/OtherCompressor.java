/**
 * 
 */
package com.github.xiaofu.demo.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;
import org.xerial.snappy.SnappyOutputStream;

/**
 * @author fulaihua
 *
 */
public class OtherCompressor {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, IOException {
//		String codecClassname = "org.apache.hadoop.io.compress.SnappyCodec";
//		Class<?> codecClass = Class.forName(codecClassname);
		Configuration conf = new Configuration();
//		CompressionCodec codec = (CompressionCodec) ReflectionUtils
//				.newInstance(codecClass, conf);
		FileSystem fs = FileSystem.get(conf);
		Path inputFile = new Path("/user/hive/warehouse/tmp_kfshuju/kfshuju.txt");
		Path outputFile = new Path(
				"/user/hive/warehouse/com_kfshuju/kfshuju");
		FSDataInputStream inputStream = fs.open(inputFile);
		FSDataOutputStream outputStream = fs.create(outputFile);
		SnappyOutputStream out=new SnappyOutputStream(outputStream);
//		CompressionOutputStream out = codec.createOutputStream(outputStream);
		IOUtils.copyBytes(inputStream, out, 65535, true);

	}

}
