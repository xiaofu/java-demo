package com.github.xiaofu.demo.spring.jdbc;

import java.util.List;

import javax.sql.DataSource;
//TODO：产生大量的查询子类，意义不大!!!
public class StudentModelingJDBCTemplate  implements StudentDAO {


	StudentMappingQuery query;
	StudentMappingUpdate update ;
	@Override
	public void create(Student student) {
		 
		
	}

	@Override
	public void setDataSource(DataSource ds) {
		 this.query = new StudentMappingQuery(ds);
		
	}

	@Override
	public void create(String name, Integer age) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Student getStudent(Integer id) {
		return query.findObject(id);
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
		update.execute(age,id);
		
	}

	@Override
	public List<Student> listStudents(List<Object> idLists) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
