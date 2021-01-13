package com.github.xiaofu.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCDemo {

	public static void main(String[] args) {
		showUser();

	}
	public static void showUser(){
	    //数据库连接
	    Connection connection = null;
	    //预编译的Statement，使用预编译的Statement提高数据库性能
	    PreparedStatement preparedStatement = null;
	    //结果 集
	    ResultSet resultSet = null;

	    try {
	        //加载数据库驱动
	        Class.forName("com.mysql.jdbc.Driver");

	        //通过驱动管理类获取数据库链接
	        connection =  DriverManager.getConnection("jdbc:mysql://localhost:3306/bookshop?useServerPrepStmts=true&cachePrepStmts=true", "root", "");

	        preparedStatement=connection.prepareStatement("select * from s_user where name like ?");
	        preparedStatement.setString(1, "%小明%");
	        resultSet =  preparedStatement.executeQuery();
	        //遍历查询结果集
	        while(resultSet.next()){
	            System.out.println(resultSet.getString("userid")+"  "+resultSet.getString("name"));
	        }
	        //注意这里必须要关闭当前PreparedStatement对象流，否则下次再次创建PreparedStatement对象的时候还是会再次预编译sql模板，使用PreparedStatement对象后不关闭当前PreparedStatement对象流是不会缓存预编译后的函数key的
	        resultSet.close();
	        preparedStatement.close();

	        preparedStatement=connection.prepareStatement("select * from s_user where name like ?");
	        preparedStatement.setString(1, "%三%");
	        resultSet =  preparedStatement.executeQuery();
	        //遍历查询结果集
	        while(resultSet.next()){
	            System.out.println(resultSet.getString("userid")+"  "+resultSet.getString("name"));
	        }

	        resultSet.close();
	        preparedStatement.close();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }finally{
	        //释放资源
	        if(resultSet!=null){
	            try {
	                resultSet.close();
	            } catch (SQLException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	        if(preparedStatement!=null){
	            try {
	                preparedStatement.close();
	            } catch (SQLException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	        if(connection!=null){
	            try {
	                connection.close();
	            } catch (SQLException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }

	    }
	}

}
