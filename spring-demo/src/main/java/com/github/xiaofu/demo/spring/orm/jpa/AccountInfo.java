package com.github.xiaofu.demo.spring.orm.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.AbstractAuditable;

@Entity
@Table(name = "t_accountinfo")
@NamedQuery(name = "AccountInfo.findByAccountId",
query = "select u from AccountInfo as u where u.id = ?1 and 1=1")
public class AccountInfo extends AbstractAuditable<UserInfo,Long>  implements Serializable {

	private static final long serialVersionUID = -2756264350039887233L;
/*	@Id
	 @GeneratedValue(strategy = GenerationType.AUTO)*/
	private Long accountId;
	private Integer balance;
	 private String firstname;
	 private String lastname;
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	 

}
