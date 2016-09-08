package com.github.xiaofu.demo.spring.orm.jpa;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "t_accountinfo")
@NamedQuery(name = "AccountInfo.findByAccountId",
query = "select u from AccountInfo as u where u.accountId = ?1 and 1=1")
public class AccountInfo implements Serializable {

	private static final long serialVersionUID = -2756264350039887233L;
	@Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	private Long accountId;
	private Integer balance;
	@Lob
	private UserInfo userInfo;
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

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

}
