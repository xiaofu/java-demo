package com.github.xiaofu.demo.hibernate.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A320")
public class A320 extends Plane { 
	
	
	private String tt;
}   