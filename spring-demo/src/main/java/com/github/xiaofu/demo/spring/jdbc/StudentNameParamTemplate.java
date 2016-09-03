package com.github.xiaofu.demo.spring.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class StudentNameParamTemplate implements StudentDAO {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public void setDataSource(DataSource dataSource) {

		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				dataSource);
	}
	public void create(Student student) {
		String SQL = "insert into Student (name, age) values (:name, :age)";
		
	    SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(student);
		namedParameterJdbcTemplate.update(SQL, namedParameters);
		System.out.println("Created Record Name = " + student.getName() + " Age = " + student.getAge());
		return;
	}
	public void create(String name, Integer age) {
		String SQL = "insert into Student (name, age) values (:name, :age)";
		 
		 Map<String, Object> namedParameters = new HashMap<String,Object>();
		 namedParameters.put("name", name);
		 namedParameters.put("age", age);
		namedParameterJdbcTemplate.update(SQL, namedParameters);
		System.out.println("Created Record Name = " + name + " Age = " + age);
		return;
	}

	public Student getStudent(Integer id) {
		String SQL = "select * from Student where id = :id";
		Map<String, Object> namedParameters = new HashMap<String,Object>();
		namedParameters.put("id", id);
		Student student = namedParameterJdbcTemplate.queryForObject(SQL,
				namedParameters, new StudentMapper());
		return student;
	}

	public List<Student> listStudents() {
		String SQL = "select * from Student";
		List<Student> students = namedParameterJdbcTemplate.query(SQL,
				new StudentMapper());
		return students;
	}

	public void delete(Integer id) {
		String SQL = "delete from Student where id = :id";
		Map<String, Object> namedParameters = new HashMap<String,Object>();
		namedParameters.put("id", id);
		namedParameterJdbcTemplate.update(SQL, namedParameters);
		System.out.println("Deleted Record with ID = " + id);
		return;
	}

	public void update(Integer id, Integer age) {
		String SQL = "update Student set age = :age where id = :id";
		Map<String, Object> namedParameters = new HashMap<String,Object>();
		namedParameters.put("id", id);
		namedParameters.put("age", age);
		namedParameterJdbcTemplate.update(SQL, namedParameters);
		System.out.println("Updated Record with ID = " + id);
		return;
	}

}
