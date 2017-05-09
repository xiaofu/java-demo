/**
 * 
 */
package com.github.xiaofu.demo.mmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.hamcrest.core.Is;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

 
/**
 * @author Administrator
 *
 */
public class MMapTest {
	private static FileChannel channel;  
	  
    @BeforeClass  
    public static void runBeforeClass() throws FileNotFoundException {  
        File file = new File("tt.txt");  
        channel = new RandomAccessFile(file, "rw").getChannel();  
  
    }  
  
    @AfterClass  
    public static void runAfterClass() throws IOException {  
        channel.close();  
    }  
  
    @Before  
    public void runBeforeEveryTest() throws IOException {  
        ByteBuffer temp = ByteBuffer.allocate(100);  
        temp.put("12345".getBytes());  
        temp.flip();  
        channel.truncate(5);  
        channel.write(temp);  
    }  
  
    /** 
     *  
     * @author weip.pengw 
     * @time 2012-3-19 下午02:53:21 
     * @param buffer 
     * @throws Exception 
     */  
    public static String dumpBuffer(ByteBuffer buffer) throws Exception {  
  
        StringBuffer sb = new StringBuffer();  
        int nulls = 0;  
        int limit = buffer.limit();  
        for (int i = 0; i < limit; i++) {  
            char c = (char) buffer.get(i);  
            if (c == '\u0000') {  
                nulls++;  
                continue;  
            }  
  
            if (nulls != 0) {  
                sb.append("|[" + nulls + " nulls]|");  
                nulls = 0;  
            }  
            sb.append(c);  
        }  
        return sb.toString();  
    }  
  
    /** 
     *  
     * @author weip.pengw 
     * @throws Exception 
     * @time 2012-3-19 下午02:45:28 
     */  
    @Test  
    public void testCopyOnWrite() throws Exception {  
        // 产生一个文件，文件跨内存页  
        ByteBuffer temp = ByteBuffer.allocate(100);  
        temp.put("This is the file content".getBytes());  
        temp.flip();  
        channel.write(temp, 0);  
        temp.clear();  
        temp.put("This is more file content".getBytes());  
        temp.flip();  
        channel.write(temp, 8192);  
  
        // 产生两个MAPFILE  
        MappedByteBuffer rw = channel.map(FileChannel.MapMode.READ_WRITE, 0,  
                channel.size());  
        MappedByteBuffer cow = channel.map(FileChannel.MapMode.PRIVATE, 0,  
                channel.size());  
        // 测试之前  
        
        Assert.assertThat(  dumpBuffer(rw), Is.is("This is the file content|[8168 nulls]|This is more file content"));  
        Assert.assertThat(  
                dumpBuffer(cow),  
               Is.is("This is the file content|[8168 nulls]|This is more file content"));  
  
        // 测试step1，修改rw前几个字节  
        rw.position(0);  
        rw.put("RW".getBytes());  
        rw.force();  
        Assert.assertThat(  
                dumpBuffer(rw),  
                Is.is("RWis is the file content|[8168 nulls]|This is more file content"));  
        Assert.assertThat(  
                dumpBuffer(cow),  
                Is.is("RWis is the file content|[8168 nulls]|This is more file content"));  
  
        // 测试step2，修改cow前几个字节,触发copy on write  
        cow.position(0);  
        cow.put("COW".getBytes());  
        Assert.assertThat(  
                dumpBuffer(rw),  
                Is. is("RWis is the file content|[8168 nulls]|This is more file content"));  
        Assert.assertThat(  
                dumpBuffer(cow),  
                Is.is("COWs is the file content|[8168 nulls]|This is more file content"));  
  
        // 测试step3，修改rw的最后几个字节,cow后面的字节反应了改变  
        rw.position(8192);  
        rw.put("RW".getBytes());  
        rw.force();  
        Assert.assertThat(  
                dumpBuffer(rw),  
                Is.is("RWis is the file content|[8168 nulls]|RWis is more file content"));  
        Assert.assertThat(  
                dumpBuffer(cow),  
                Is.is("COWs is the file content|[8168 nulls]|RWis is more file content"));  
  
        // 测试step4，修改cow的最后几个字节,再次触发copy on write  
        cow.position(8192);  
        cow.put("COW".getBytes());  
        Assert. assertThat(  
                dumpBuffer(rw),  
                Is.is("RWis is the file content|[8168 nulls]|RWis is more file content"));  
        Assert. assertThat(  
                dumpBuffer(cow),  
                Is.is("COWs is the file content|[8168 nulls]|COWs is more file content"));  
  
        // 测试step5，再次修改rw的前后几个字节,对cow没有了影响  
        rw.position(0);  
        rw.put("RW2".getBytes());  
        rw.position(8192);  
        rw.put("RW2".getBytes());  
        rw.force();  
        Assert. assertThat(  
                dumpBuffer(rw),  
                Is.is("RW2s is the file content|[8168 nulls]|RW2s is more file content"));  
        Assert.assertThat(  
                dumpBuffer(cow),  
                Is.is("COWs is the file content|[8168 nulls]|COWs is more file content"));  
  
        // cleanup  
        // channel.close();  
  
    }  
}
