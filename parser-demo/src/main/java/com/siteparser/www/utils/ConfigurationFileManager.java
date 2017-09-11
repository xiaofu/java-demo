package com.siteparser.www.utils;

import java.util.Map;
import java.util.HashMap;

import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConfigurationFileManager
{
	private final static Logger LOG = LoggerFactory
			.getLogger(ConfigurationFileManager.class);
	private final static Map<String, FileConfiguration> CONF_MAP = new HashMap<String, FileConfiguration>();
	private final static Object OBJ_LOCK = new Object();

	public final static String SERVICECONFIG_FILE = "service.properties";

	private ConfigurationFileManager()
	{
	}

	/**
	 * 根据指定的配置文件名返回指定的配置文件对象 TODO:
	 * 
	 * @param fileName
	 *            配置文件名
	 * @return 如果获取配置文件成功，返回配置文件对象，否则返回NULL
	 * */
	public static FileConfiguration getConfigInfo(String fileName)
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

	public static FileConfiguration getServiceConfigInfo()
	{
		return getConfigInfo(SERVICECONFIG_FILE);
	}

	 

}
