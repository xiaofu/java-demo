package com.github.xiaofu.demo.spring.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;

public class StudentMappingQuery  extends MappingSqlQuery<Student> {
	public StudentMappingQuery(DataSource ds) {
        super(ds, "select id, name, age from student where id = ?");
        super.declareParameter(new SqlParameter("id", Types.INTEGER));
        compile();
    }

    @Override
    protected Student mapRow(ResultSet rs, int rowNumber) throws SQLException {
    	Student actor = new Student();
        actor.setId(rs.getInt("id"));
        actor.setName(rs.getString("name"));
        actor.setAge(rs.getInt("age"));
        return actor;
    }
}
