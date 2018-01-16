/**
 * @ProjectName: DataAnalysis-vip-3
 * @Copyright: 版权所有 Copyright © 2001-2012 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2013-5-14 下午04:45:11
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.example;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import org.apache.hadoop.io.Writable;

 

/**
 * <p>
 * </p>
 * 
 * @author xukaiqiang 2013-5-14 下午04:45:11
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2013-5-14
 * @modify by reason:{方法名}:{原因}
 */
public class XXXXObject implements Writable,Serializable
{

	public transient HashMap<String, String> data = new HashMap<String, String>();

	public void write(DataOutput out) throws IOException
	{
		VipcloudUtil.WriteHashMap(out, this.data);
	}

	public void readFields(DataInput in) throws IOException
	{
		VipcloudUtil.ReadHashMap(in, this.data);
	}
}
