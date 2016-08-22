package com.github.xiaofu.demo.cxf.rs;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Book")
public class Book {
	private int id;
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
