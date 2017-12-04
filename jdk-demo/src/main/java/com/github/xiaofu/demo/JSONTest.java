/**
 * @ProjectName: jdk-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2017年12月4日 上午11:24:28
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;

/**
 * <p>
 * </p>
 * 
 * @author fulaihua 2017年12月4日 上午11:24:28
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2017年12月4日
 * @modify by reason:{方法名}:{原因}
 */
public class JSONTest
{
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper mapper=new ObjectMapper();
		Map<String,String> map=Maps.newHashMap();
		map.put("spark.t", "ok");
		map.put("spark.on.yarn", "yes");
		 
		 
		mapper.writeValue(System.out, map);
	}

}
