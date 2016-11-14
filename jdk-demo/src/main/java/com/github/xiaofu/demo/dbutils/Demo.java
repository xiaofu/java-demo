/**
 * 
 */
package com.github.xiaofu.demo.dbutils;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 * @author home
 *
 */
public class Demo {

	 static String QUERY_SQL="select * from test";
	 
	 static String INSERT_SQL="insert into test(t4,t5,t6,t7) values(?,?,?,?)";
	/**
	 * 字段名大小和属性名大小是没有问题的，DBUTILS会进行忽略大小的比较
	 * @param args
	 * @throws SQLException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public static void main(String[] args) throws SQLException, IllegalAccessException, InvocationTargetException {
		
		/*QueryRunner runner=new QueryRunner(DBCPUtils.getDataSource());
		List<TestModel> lists= runner.query(QUERY_SQL, new BeanListHandler<TestModel>(TestModel.class));
		System.out.println(lists.size());
		TestModel destModel=new TestModel();
		for (TestModel model : lists) {
			BeanUtils.copyProperties(destModel, model);
			System.out.println(model.getT3());
		}
		//空字符串插入DATE,DATETIME,INT,FLOAT
		runner.update(INSERT_SQL,"","","","");*/
		testType();
	}
	
	public static void batchTest() throws SQLException
	{
		QueryRunner runner=new QueryRunner();
		Connection conn= DBCPUtils.getDataSource().getConnection();
		conn.setAutoCommit(false);
		for (int i = 0; i < 100; i++) {
			Object[][] params=new Object[1][4];
			params[0][0]="";
			params[0][1]="";
			params[0][2]="";
			params[0][3]="";
			runner.batch(conn, INSERT_SQL, params);
		}
		//conn.rollback();
	}
	
	public static void testType() throws SQLException
	{
		Connection conn= DBCPUtils.getDataSource().getConnection();
		Statement   statement=	conn.createStatement();
		ResultSet resultSet= statement.executeQuery("select * from test");
		while(resultSet.next())
		{
			System.out.println(resultSet.getString("t1"));
			System.out.println(resultSet.getObject ("t1"));
			
			System.out.println("===============");
			System.out.println(resultSet.getString("t2"));
			System.out.println(resultSet.getObject ("t2"));
			System.out.println("===============");
			System.out.println(resultSet.getString("t3"));
			System.out.println(resultSet.getObject ("t3"));
		}
	}

}
