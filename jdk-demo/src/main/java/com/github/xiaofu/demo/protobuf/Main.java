package com.github.xiaofu.demo.protobuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.github.xiaofu.demo.proto.PersonProtos.AddressBook;
import com.github.xiaofu.demo.proto.PersonProtos.Person;
import com.github.xiaofu.demo.proto.PersonProtos.Person.PhoneNumber;
import com.github.xiaofu.demo.proto.PersonProtos.Person.PhoneType;
import com.github.xiaofu.demo.proto.PersonServiceProtocol;
public class Main {

	public static void main(String[] args) throws IOException {
	    
		//serialize();
		deserialize();
	}
	
	public static void  serialize() throws IOException
	{
		PhoneNumber phone=Person.PhoneNumber.newBuilder().setNumber("1234").setType(PhoneType.MOBILE).build();
	    Person person= Person.newBuilder().setId(123).setName("abc").setEmail("wfygowxf@163.com").addPhone(phone).build();
	    AddressBook addressBook=AddressBook.newBuilder().addPerson(person).build();
	    FileOutputStream outputStream=new FileOutputStream("tt.txt");
	    person.writeTo(outputStream);
	    outputStream.close();
	}
	
	public static void deserialize() throws IOException
	{
		FileInputStream inputStream=new FileInputStream("tt.txt");
		Person  person=  Person.parseFrom(inputStream);
	    System.out.println(person.getId());
	    System.out.println(person.getName());
	   // System.out.println(person.getAddress());
	    inputStream.close();
	}
	
	public static void service()
	{
		  
	}

}
