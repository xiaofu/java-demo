package com.github.xiaofu.demo;

public class Demo {
	 public static void main(String[] args) {
	        String s1 = new StringBuilder().append("ja").append("va1").toString();
	        String s2 = s1.intern();
	        System.out.println(s1==s2);
	        
	        String s5 = "dmz";
	        String s3 = new StringBuilder().append("d").append("mz").toString();
	        String s4 = s3.intern();
	        System.out.println(s3 == s4);

	        String s7 = new StringBuilder().append("s").append("pring").toString();
	        String s8 = s7.intern();
	         String s6 = "spring";//迷惑你的，不影响！
	        System.out.println(s7 == s8);
	    }
}
