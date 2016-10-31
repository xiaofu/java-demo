package com.github.xiaofu.demo.hibernate.domain;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "DOMESTIC_CATS")
@PrimaryKeyJoinColumn(name = "CAT")
public class DomesticCat extends Cat {

	private static final long serialVersionUID = -5175690224761085376L;
	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}