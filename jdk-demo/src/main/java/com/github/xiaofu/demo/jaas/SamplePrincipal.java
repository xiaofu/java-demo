package com.github.xiaofu.demo.jaas;

import java.security.Principal;

public final class SamplePrincipal implements Principal {

	  private String name;

	  public SamplePrincipal(String name) {
	    this.name = name;
	  }

	  public String getName() {
	    return name;
	  }

	  public boolean equals(Object o) {
	    return (o instanceof SamplePrincipal)
	        && this.name.equalsIgnoreCase(((SamplePrincipal) o).name);
	  }

	  public int hashCode() {
	    return name.toUpperCase().hashCode();
	  }

	}