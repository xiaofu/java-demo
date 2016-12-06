/**
 * 
 */
package com.github.xiaofu.demo.inherit;

/**
 * @author xiaofu
 *
 */
class A {
	static String A_STR="a_str";
	private int a_t1=0;
	static
	{
		System.out.println(A_STR);
		System.out.println("A static!!!");
	}
	A()
	{
		a_t1=1;	
	}
	void test1() {
		System.out.println(a_t1);
		System.out.println("test1 A");

		this.private_test2();
		this.non_private_test2();
	}
	/**
	 * here is private method,so will be called
	 */
	private void private_test2() {
		System.out.println("private_test2 A");
	}

	void non_private_test2() {
		System.out.println("non_private_test2 A");
	}
}

class B extends A {
	static String B_STR="b_str";
	private int b_t1=0;
	static
	{
		System.out.println(B_STR);
		System.out.println("B static!!!");
	}
	B()
	{
		b_t1=2;
	}
	@Override
	void test1() {
		System.out.println(b_t1);
		System.out.println("test1 B");
		//super.test1();

	}
	
	void private_test2() {
		System.out.println("private_test2 B");
	}
	@Override
	void non_private_test2() {
		System.out.println("non_private_test2 B");
	}
}

public class Demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new B().test1();

	}

}
