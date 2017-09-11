package com.siteparser.www.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.ucanaccess.jdbc.UcanaccessDataSource;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.siteparser.www.entities.RelationModel;
import com.siteparser.www.utils.ConfigKeys;

public class AccessHelper {
	
	private static final  String SELECT_RELATION_SQL="select showName,code,tableName from relation";
	private static String INSERT_ICBC_SQL;
	private static String INSERT_WS_SQL;
	private static volatile boolean INIITED=false;
	private static  QueryRunner RUNNER;
	private static final Log LOG=LogFactory.getLog(AccessHelper.class);
	private static final Map<String,String> TABLE_SQL_MAPS=  Maps.newHashMap();
	private static final Map<String,String[]> TABLE_FIELDS_MAPS=  Maps.newHashMap();
	private static  volatile List<RelationModel> cacheRelationModel;
	public synchronized static void init(Configuration conf)
	{
		if(!INIITED)
		{
			String icbcTable=conf.getString(ConfigKeys.SQL_ICBC_TABLE);
			String wsTable=conf.getString(ConfigKeys.SQL_WS_TABLE);
			UcanaccessDataSource dataSource=new UcanaccessDataSource();
			dataSource.setAccessPath(conf.getString(ConfigKeys.SQL_ACCESS_MDB_PATH));
			RUNNER=new QueryRunner(dataSource);
			String[] icbcFields=conf.getStringArray(ConfigKeys.SQL_ICBC_TABLE_FIELDS);
			String[] placeHolder=new String[icbcFields.length];
			Arrays.fill(placeHolder , "?");
			INSERT_ICBC_SQL = String.format("insert into "+icbcTable+" (%1$s) values(%2$s)", StringUtils.join(icbcFields, ","),StringUtils.join(placeHolder,","));
			
			String[] wsFields=conf.getStringArray(ConfigKeys.SQL_WS_TABLE_FIELDS);
			placeHolder=new String[wsFields.length];
			Arrays.fill(placeHolder , "?");
			INSERT_WS_SQL = String.format("insert into "+wsTable+" (%1$s) values(%2$s)", StringUtils.join(wsFields, ","),StringUtils.join(placeHolder,","));
			
			TABLE_SQL_MAPS.put(icbcTable, INSERT_ICBC_SQL);
			TABLE_SQL_MAPS.put(wsTable, INSERT_WS_SQL);
			TABLE_FIELDS_MAPS.put(icbcTable, icbcFields);
			TABLE_FIELDS_MAPS.put(wsTable, wsFields);
			INIITED=true;
		}
	}
	 
	public static boolean insert(List<DynaBean> lists,String tableName)
	{
		if(lists==null || lists.size()==0)
			return false;
		String[] fieldLists=TABLE_FIELDS_MAPS.get(tableName);
		if(fieldLists==null)
			return false;
		Object[][] params = new Object[lists.size()][fieldLists.length];
		try
		{
			 
			int i = 0;
			for (DynaBean bean : lists)
			{
				int j = 0;
				for (String field : fieldLists)
				{
					params[i][j] = getValueByType(field, bean);
					j++;
				}
				i++;
			}
			RUNNER.batch(TABLE_SQL_MAPS.get(tableName), params);
		}
		catch (Exception e)
		{
			LOG.warn("insert userlog failed",e);
		}
		return false;
	}
	
	private static Object getValueByType(String field,DynaBean obj) 
	{
	 
		Object value=obj.get(field);
		if(value==null)
		{
			value="";
		}
		return value;
	}
	
	public static synchronized List<RelationModel> selectRelationModel(String tableName) 
	{
		if(cacheRelationModel==null)
		try {
			cacheRelationModel=  RUNNER.query(SELECT_RELATION_SQL,new ResultSetHandler<List<RelationModel>>() {
				@Override
				public List<RelationModel> handle(ResultSet rs) throws SQLException {
					List<RelationModel> lists=Lists.newArrayList();
					while(rs.next())
					{
						RelationModel model=new RelationModel();
						model.setShowName(rs.getString("showName").trim());
						model.setCode(rs.getString("code").trim());
						model.setTableName(rs.getString("tableName").trim());
						lists.add(model);
					}
					return lists;
				}
			});
		} catch (SQLException e) {
			LOG.error("read relation table failed",e);
		}
		return cacheRelationModel.stream().filter(model->model.getTableName().equalsIgnoreCase(tableName)).collect(Collectors.toList());
	}
	 
}
