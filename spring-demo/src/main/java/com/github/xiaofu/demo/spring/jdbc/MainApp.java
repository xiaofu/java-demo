package com.github.xiaofu.demo.spring.jdbc;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MainApp {
   public static void main(String[] args) {
      ApplicationContext context = 
             new ClassPathXmlApplicationContext("jdbcSpring.xml");

      StudentDAO studentJDBCTemplate = 
      (StudentDAO)context.getBean("studentNameParamTemplate");
      
      System.out.println("------Records Creation--------" );
      if(studentJDBCTemplate instanceof StudentNameParamTemplate)
      {
    	  StudentNameParamTemplate StudentDao=(StudentNameParamTemplate)studentJDBCTemplate;
    	  Student model1=new Student();
    	  model1.setName("1111");
    	  model1.setAge(11);
    	  StudentDao.create(model1);
    	  model1=new Student();
    	  model1.setName("2222");
    	  model1.setAge(2);
    	  StudentDao.create(model1);
    	  model1=new Student();
    	  model1.setName("33333");
    	  model1.setAge(15);
    	  StudentDao.create(model1);
      }
      else
      {
      studentJDBCTemplate.create("Zara", 11);
      studentJDBCTemplate.create("Nuha", 2);
      studentJDBCTemplate.create("Ayan", 15);
      }
      System.out.println("------Listing Multiple Records--------" );
      List<Student> students = studentJDBCTemplate.listStudents();
      for (Student record : students) {
         System.out.print("ID : " + record.getId() );
         System.out.print(", Name : " + record.getName() );
         System.out.println(", Age : " + record.getAge());
      }

      System.out.println("----Updating Record with ID = 2 -----" );
      studentJDBCTemplate.update(22, 20);

      System.out.println("----Listing Record with ID = 2 -----" );
      Student student = studentJDBCTemplate.getStudent(22);
      System.out.print("ID : " + student.getId() );
      System.out.print(", Name : " + student.getName() );
      System.out.println(", Age : " + student.getAge());
	  
   }
}
