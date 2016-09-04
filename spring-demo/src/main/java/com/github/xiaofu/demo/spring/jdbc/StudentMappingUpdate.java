package com.github.xiaofu.demo.spring.jdbc;

import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

public class StudentMappingUpdate extends SqlUpdate {
	  public StudentMappingUpdate(DataSource ds) {
	        setDataSource(ds);
	        setSql("update student set   age = ? where id =?");
	        declareParameter(new SqlParameter("age", Types.INTEGER));
	        declareParameter(new SqlParameter("id", Types.INTEGER));
	        compile();
	    }

	    /**
	     * @param id for the Customer to be updated
	     * @param rating the new value for credit rating
	     * @return number of rows updated
	     */
	    public int execute(int id,int age) {
	        return update(age, id);
	    }
}
