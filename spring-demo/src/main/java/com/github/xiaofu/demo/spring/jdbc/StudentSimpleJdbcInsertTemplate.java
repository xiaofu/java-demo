package com.github.xiaofu.demo.spring.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class StudentSimpleJdbcInsertTemplate implements StudentDAO {
	
	private SimpleJdbcInsert jdbcTemplateObject;

	 private SimpleJdbcCall procReadActor;

	@Override
	public void setDataSource(DataSource ds) {
		this.jdbcTemplateObject = new SimpleJdbcInsert(ds)
				.withTableName("student");
		this.procReadActor = new SimpleJdbcCall(ds)
        .withProcedureName("read_actor");
	}
	
	@Override
	public void create(Student student) {
		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("name", student.getName());
		parameters.put("age", student.getAge());
		jdbcTemplateObject.execute(parameters);

	}
	
	@Override
	public void create(String name, Integer age) {
		

	}
 
	@Override
	public Student getStudent(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Student> listStudents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Integer id, Integer age) {
		// TODO Auto-generated method stub

	}

}
