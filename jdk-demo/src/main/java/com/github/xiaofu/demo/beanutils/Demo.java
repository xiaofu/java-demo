package com.github.xiaofu.demo.beanutils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.LazyDynaList;
import org.apache.commons.beanutils.LazyDynaMap;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.WrapDynaBean;

public class Demo {

	public static void standard() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Employee employee = new Employee();
		// simple property
		PropertyUtils.setSimpleProperty(employee, "firstName", "ok");
		System.out.println(PropertyUtils.getSimpleProperty(employee, "firstName"));
		// indexed property
		int index = 0;
		Employee subordinate = new Employee();
		subordinate.setAddress("home", new Employee().new Address());
		subordinate.setFirstName("sec");
		String name = "subordinate[" + index + "]";
		PropertyUtils.setIndexedProperty(employee, name, subordinate);
		PropertyUtils.setIndexedProperty(employee, "subordinate", 1, subordinate);
		subordinate = (Employee) PropertyUtils.getIndexedProperty(employee, name);
		subordinate = (Employee) PropertyUtils.getIndexedProperty(employee, "subordinate", 1);
		System.out.println(subordinate.getFirstName());
		// mapped property
		Employee.Address address = new Employee().new Address();
		PropertyUtils.setMappedProperty(employee, "address(home)", address);
		// Nested Property
		PropertyUtils.getNestedProperty(employee, "address(home).city");
		// 任何类型的属性操作
		PropertyUtils.getProperty(employee, "subordinate[0].address(home).city");
	}

	public static void BasicDynaBeanTest()
			throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
		DynaProperty[] props = new DynaProperty[] { new DynaProperty("address", java.util.Map.class),
				new DynaProperty("subordinate", Employee[].class), new DynaProperty("firstName", String.class),
				new DynaProperty("lastName", String.class) };
		BasicDynaClass dynaClass = new BasicDynaClass("employee", null, props);
		DynaBean employee = dynaClass.newInstance();
		employee.set("address", new HashMap());
		employee.set("subordinate", new Employee[0]);
		employee.set("firstName", "Fred");
		employee.set("lastName", "Flintstone");
		PropertyUtils.setProperty(employee, "firstName", "ok");
		System.out.println(PropertyUtils.getProperty(employee, "firstName"));
		// throw exception
		// System.out.println(PropertyUtils.getProperty(employee, "aa"));

	}

	public static void WrapDynaBeanTest() {
		Employee employee = new Employee();
		WrapDynaBean bean = new WrapDynaBean(employee);
		bean.set("firstName", "Fred");
		bean.set("lastName", "Flintstone");
		System.out.println(bean.get("lastName"));
		// throw exception
		System.out.println(bean.get("aa"));
	}

	public static void LazyDynaBeanTest() {
		LazyDynaBean dynaBean = new LazyDynaBean();

		dynaBean.set("foo", "bar"); // simple

		dynaBean.set("customer", "title", "Mr"); // mapped
		dynaBean.set("customer", "surname", "Smith"); // mapped

		dynaBean.set("address", 0, "addressLine1"); // indexed
		dynaBean.set("address", 1, "addressLine2"); // indexed
		dynaBean.set("address", 2, "addressLine3"); // indexed
	}
	/**
	 * 一般主要用来读写simple,mapped and indexed 不关心
	 */
	public static void LazyDynaMapTest()
	{
		LazyDynaMap dynaBean = new LazyDynaMap();        // create DynaBean

	     dynaBean.set("foo", "bar");                   // simple
	     dynaBean.set("customer", "title", "Mr");      // mapped
	     dynaBean.set("address", 0, "addressLine1");     // indexed

	     Map myMap = dynaBean.getMap()  ;              // retrieve the Map
	     
	     
	    // DynaBean dynaBean = new LazyDynaMap(myMap);  // wrap Map in DynaBean
	     // dynaBean.set("foo", "bar");                  // set properties
	}
	
	public static void LazyDynaListTest()
	{
		TreeMap[] myArray = new TreeMap[2]; // your Map[]
			    List lazyList = new LazyDynaList(myArray);
	}
	public static void main(String[] args)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		BasicDynaBeanTest();
		WrapDynaBeanTest();
	}
}
