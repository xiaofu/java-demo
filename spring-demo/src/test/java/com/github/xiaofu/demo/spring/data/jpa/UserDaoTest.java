package com.github.xiaofu.demo.spring.data.jpa;

import static org.junit.Assert.*;

import java.util.List;

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
		for (int i = 0; i < 20; i++) {
			AccountInfo model=new AccountInfo();
			model.setBalance(22);
			model.setUserInfo(null);
			service.save(model);
		}
		
	}

	@Test
	public void testFindByAccountId() {
		 service.findByAccountId(22);
	}

	@Test
	public void testFindByAccountIdOrderByAccountIdAsc() {
		 
	}

	@Test
	public void testFindBybalance() {
		List<AccountInfo> result= service.findBybalance(22, new PageRequest(1,10));
		 
		   System.out.println(result.size());
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
	
	@Test
	public void testUpdate() {
		System.out.println(service.updateData( 33,2));
		
	}
}
