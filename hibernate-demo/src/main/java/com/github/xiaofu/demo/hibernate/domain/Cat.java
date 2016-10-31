package com.github.xiaofu.demo.hibernate.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.persistence.InheritanceType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "CATS")
@Inheritance(strategy = InheritanceType.JOINED)
public class Cat implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4728171507787733814L;
	private String id;
	@Id
	@GeneratedValue(generator = "cat-uuid")
	@GenericGenerator(name = "cat-uuid", strategy = "uuid")
	String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	

}