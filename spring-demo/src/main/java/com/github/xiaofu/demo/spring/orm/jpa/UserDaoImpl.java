package com.github.xiaofu.demo.spring.orm.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDaoImpl implements UserDao {
	@PersistenceContext
	private EntityManager em;

	@Transactional
	public long save(AccountInfo accountInfo) {
		em.persist(accountInfo);
		return accountInfo.getAccountId();
	}
}
