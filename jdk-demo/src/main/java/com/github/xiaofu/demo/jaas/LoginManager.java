package com.github.xiaofu.demo.jaas;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class LoginManager {
	public static void main(String[] args) {
		System.setProperty("java.security.auth.login.config", "jaas.conf");
		try {
			String username = "aa";
			String password = "bb";
			// 此处指定了使用配置文件的“Sample”验证模块，对应的实现类为SampleLoginModule
			LoginContext lc = new LoginContext("Sample", new SampleCallbackHandler(username, password));
			lc.login();// 如果验证失败会抛出异常

		} catch (LoginException e) {
			e.printStackTrace();

		} catch (SecurityException e) {
			e.printStackTrace();

		}
	}
}
