package com.cqvip.runwork.fast.demo.utils;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 
public final class Tools
{
	private Tools()
	{
	}
	
	private static String CONIF_DIR="config";
	
	public static String getConfigFileAbsolutePath(String fileName)
	{
		return getProjectPath()+SystemUtils.FILE_SEPARATOR+CONIF_DIR+SystemUtils.FILE_SEPARATOR+fileName;
	}
	
	public static String getProjectPath()
	{

		java.net.URL url = Tools.class.getProtectionDomain()
				.getCodeSource().getLocation();
		String filePath = null;
		try
		{
			filePath = java.net.URLDecoder.decode(url.getPath(), "utf-8");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (filePath.endsWith(".jar"))
			filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
		java.io.File file = new java.io.File(filePath);
		filePath = file.getAbsolutePath();
		return filePath;
	}
  
}
