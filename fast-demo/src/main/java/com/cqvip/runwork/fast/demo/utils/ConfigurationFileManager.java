package com.cqvip.runwork.fast.demo.utils;

import java.util.Map;
import java.util.HashMap;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConfigurationFileManager
{
	private final static Logger LOG = LoggerFactory
			.getLogger(ConfigurationFileManager.class);
	private final static Map<String,Configuration> CONF_MAP = new HashMap<String, Configuration>();
	private final static Object OBJ_LOCK = new Object();
	 
	public final static String SERVICE_CONFIG_FILE = "service.properties";
	public final static String LOG4J_CONFIG_FILE = "log4j.properties";
	public final static String QUARTZ_CONFIG_FILE = "quartz.properties";
	public final static String JOBS_CONFIG_FILE = "jobs.xml";
	private ConfigurationFileManager()
	{
	}

	/**
	 * 根据指定的配置文件名返回指定的配置文件对象
	 * TODO:
	 * @param fileName
	 *            配置文件名
	 * @return 如果获取配置文件成功，返回配置文件对象，否则返回NULL
	 * */
	public static Configuration  getConfigInfo(String fileName)
	{
		if (CONF_MAP.containsKey(fileName))
		{
			return CONF_MAP.get(fileName);
		}
		synchronized (OBJ_LOCK)
		{
			if (CONF_MAP.containsKey(fileName))
			{
				return CONF_MAP.get(fileName);
			}
			FileConfiguration conf;
			try
			{
				 //调用构造函数的时候，
				//1.文件名是否是URL
				//2.文件名是不是绝对路径
				//3.文件名是否在当前目录
				//4.查找user.home目录的文件是否存在
				//5.查找classpath路径中是否存在配置文件
				 //配置中的分隔符把值切分为数组，如果读单值只会返回一个，所以需要重围分隔符
				 AbstractConfiguration.setDefaultListDelimiter('|');
				 conf = new PropertiesConfiguration(fileName);
				 conf.setReloadingStrategy(new
				 FileChangedReloadingStrategy());
				CONF_MAP.put(fileName, conf);
			}
			catch (ConfigurationException e)
			{
				LOG.error(
						String.format("%s configuration exception", fileName),
						e);
			}
		}
		return CONF_MAP.get(fileName);
	}
	

	public static Configuration getServiceConfig()
	{
		return getConfigInfo(Tools.getConfigFileAbsolutePath(SERVICE_CONFIG_FILE));
	}

 

  
 
}
