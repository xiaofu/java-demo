package com.github.xiaofu.demo.spring.data.jpa;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import com.github.xiaofu.demo.spring.orm.jpa.AccountInfo;

public class UserDaoTest {
	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
			"dataJpaSpring.xml");
	UserDao service = ctx.getBean(UserDao.class);
	@Test
	public void testSaveAccountInfo() {
		AccountInfo model=new AccountInfo();
		model.setBalance(22);
		model.setUserInfo(null);
		service.save(model);
	}

	@Test
	public void testFindByAccountId() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByAccountIdOrderByAccountIdAsc() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindBybalance() {
		  
	}

	@Test
	public void testQueryBybalance() {
		Slice<AccountInfo> result= service.queryBybalance(22, new PageRequest(1,10));
		 
		   System.out.println(result.getNumberOfElements());
	}

	@Test
	public void testReadBybalance() {
	
		Page<AccountInfo> result= service.readBybalance(22, new PageRequest(1,10));
	   System.out.println(result.getTotalPages());
	   System.out.println(result.getNumberOfElements());
	}

}
