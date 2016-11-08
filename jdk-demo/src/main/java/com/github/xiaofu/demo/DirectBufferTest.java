package com.github.xiaofu.demo;

import java.nio.ByteBuffer;

import sun.nio.ch.DirectBuffer;

public class DirectBufferTest {

	 public static void directOOM(){
	        //直接OOM
	        ByteBuffer.allocateDirect(257 * 1024 * 1024);
	    }
	    public static void fullGC(){
	        //先申请256M，再申请1M，是不会OOM的
	        ByteBuffer.allocateDirect(256 * 1024 * 1024);
	        //这里直接GC
	        ByteBuffer.allocateDirect(1024 * 1024);
	    }
	    public static void disableGCOOM(){
	        //加上-XX:+DisableExplicitGC则OOM
	        ByteBuffer.allocateDirect(256 * 1024 * 1024);
	        ByteBuffer.allocateDirect(1024 * 1024);
	    }
	    public static void gcReference(){
	        //增加引用，则直接OOM
	        //Exception in thread "main" java.lang.OutOfMemoryError: Direct buffer memory
	        //DirectBuffer的GC规则与堆对象的回收规则一样，只有垃圾对象才被回收，而判定是否为垃圾对象
	        //依据引用树中的存活节点来判定
	        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(256 * 1024 * 1024);
	        ByteBuffer.allocateDirect(1024 * 1024);
	    }
	    public static void manualClear(){
	        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(256 * 1024 * 1024);
	        //手工释放，无需等到GC才释放，这个时候可以直接禁用显示GC
	        ((DirectBuffer)byteBuffer).cleaner().clean();
	        ByteBuffer.allocateDirect(1024 * 1024);
	    }
	    
	public static void main(String[] args)
	{
		gcReference();
	}
}
