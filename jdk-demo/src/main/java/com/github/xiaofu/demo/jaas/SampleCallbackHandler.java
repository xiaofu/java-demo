package com.github.xiaofu.demo.jaas;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class SampleCallbackHandler implements CallbackHandler {
	  private String username;
	  private String password;

	  public SampleCallbackHandler(final String username, final String password) {
	    this.username = username;
	    this.password = password;
	  }

	  public void handle(Callback[] callbacks) throws IOException,
	      UnsupportedCallbackException {
	    for (int index = 0; index < callbacks.length; index++) {
	      if (callbacks[index] instanceof NameCallback) {
	        NameCallback ncb = (NameCallback) callbacks[index];
	        ncb.setName(username);
	      }
	      if (callbacks[index] instanceof PasswordCallback) {
	        PasswordCallback pcb = (PasswordCallback) callbacks[index];
	        pcb.setPassword(password.toCharArray());
	      }
	    }
	  }
	}
