package com.github.xiaofu.demo.classloader;

import java.net.URL;

public class TestClassLoader {
	public  static void main(String[] args)
	{
		bootstrap();
	}
	
	private static void bootstrap()
	{
		URL[] urls=sun.misc.Launcher.getBootstrapClassPath().getURLs();
		   for (int i = 0; i < urls.length; i++) {
		     System.out.println(urls[i].toExternalForm());
		   }
	}
	
	private static void ext()
	{
		  
		   
	}
}
