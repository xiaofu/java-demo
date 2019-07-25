package com.github.xiaofu.demo.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CodecPool;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.io.compress.Compressor;
import org.apache.hadoop.util.ReflectionUtils;
/**
 * 使用压缩/解压缩器池来重用
 * @author fulaihua
 *
 */
public class PooledStreamCompressor
{
	public static void main(String[] args) throws Exception
	{
		String codecClassname = args[0];
		Class<?> codecClass = Class.forName(codecClassname);
		Configuration conf = new Configuration();
		CompressionCodec codec = (CompressionCodec) ReflectionUtils
				.newInstance(codecClass, conf);
		Compressor compressor = null;
		try
		{
			compressor = CodecPool.getCompressor(codec);
			CompressionOutputStream out = codec.createOutputStream(System.out,
					compressor);
			IOUtils.copyBytes(System.in, out, 4096, false);
			out.finish();
		}
		finally
		{
			CodecPool.returnCompressor(compressor);
		}
	}
}
