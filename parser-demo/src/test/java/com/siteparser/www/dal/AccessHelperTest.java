package com.siteparser.www.dal;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.LazyDynaMap;
import org.apache.commons.configuration.Configuration;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.siteparser.www.entities.RelationModel;
import com.siteparser.www.utils.ConfigurationFileManager;

public class AccessHelperTest {

	@Test
	public void test_Select() {
		Configuration conf=	 ConfigurationFileManager.getServiceConfigInfo();
		AccessHelper.init(conf);
		for (RelationModel item :  AccessHelper.selectRelationModel("ws")) {
			System.out.println(item.getTableName());
		}
	}
	@Test
	public void test_Insert() {
		Configuration conf=	 ConfigurationFileManager.getServiceConfigInfo();
		AccessHelper.init(conf);
		DynaBean bean=new LazyDynaMap();
		bean.set("code", "11111");
		bean.set("buyPrice", 12.43f);
		bean.set("sellPrice", 11.4325f);
		bean.set("tYear", 2017);
		bean.set("tMonth", 6);
		bean.set("tDay", 21);
		bean.set("currentTime", "2017-06-22 21:04:23");
		List<DynaBean> lists=  Lists.newArrayList();
		lists.add(bean);
		AccessHelper.insert( lists, "icbc");
	}
}
