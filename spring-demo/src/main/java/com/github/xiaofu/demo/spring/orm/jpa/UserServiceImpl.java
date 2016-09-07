package com.github.xiaofu.demo.spring.orm.jpa;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Transactional
	public long createNewAccount(String user, String pwd, Integer init) {
		// 封装域对象
		AccountInfo accountInfo = new AccountInfo();
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername(user);
		userInfo.setPassword(pwd);
		accountInfo.setBalance(init);
		accountInfo.setUserInfo(userInfo);
		// 调用持久层，完成数据的保存
		return userDao.save(accountInfo);
	}
}
