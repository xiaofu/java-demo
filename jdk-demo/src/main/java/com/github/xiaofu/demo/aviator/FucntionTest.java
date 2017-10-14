package com.github.xiaofu.demo.aviator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

public class FucntionTest {
	
	static class SplitContains extends AbstractFunction {

		@Override
		public AviatorBoolean call(Map<String, Object> env, AviatorObject args1,
				AviatorObject args2, AviatorObject args3, AviatorObject args4) {

			String fieldName = (String) args1.getValue(env);
			String fieldSeparator = (String) args2.getValue(env);
			String testValue = (String) args3.getValue(env);
			String valueSeparator = (String) args4.getValue(env);
			@SuppressWarnings("unchecked")
			Map<String, String> xxxobj = (Map<String, String>) env
					.get("xxxobj");
			if (StringUtils.isEmpty(xxxobj.get(fieldName)))
				return AviatorBoolean.FALSE;
			if (StringUtils.isEmpty(valueSeparator)) {
				for (String item : StringUtils.split(xxxobj.get(fieldName),
						fieldSeparator)) {
					if(item.contains(testValue))
						return AviatorBoolean.TRUE;
				}
			} else {
				for (String orginal : StringUtils.split(xxxobj.get(fieldName),
						fieldSeparator)) {
					for (String test : StringUtils.split(testValue,
							valueSeparator)) {
						 if(orginal.contains(test))
							 return AviatorBoolean.TRUE;
					}
				}
			}
			return AviatorBoolean.FALSE;
		}

		public String getName() {
			return "splitContains";
		}

	}
	
	static class SplitStartsWith extends AbstractFunction {

		@Override
		public AviatorBoolean call(Map<String, Object> env, AviatorObject args1,
				AviatorObject args2, AviatorObject args3, AviatorObject args4) {

			String fieldName = (String) args1.getValue(env);
			String fieldSeparator = (String) args2.getValue(env);
			String testValue = (String) args3.getValue(env);
			String valueSeparator = (String) args4.getValue(env);
			@SuppressWarnings("unchecked")
			Map<String, String> xxxobj = (Map<String, String>) env
					.get("xxxobj");

			if (StringUtils.isEmpty(xxxobj.get(fieldName)))
				return AviatorBoolean.FALSE;
			if (StringUtils.isEmpty(valueSeparator)) {
				for (String item : StringUtils.split(xxxobj.get(fieldName),
						fieldSeparator)) {
					if(item.startsWith(testValue))
						return AviatorBoolean.TRUE;
				}
			} else {
				for (String orginal : StringUtils.split(xxxobj.get(fieldName),
						fieldSeparator)) {
					for (String test : StringUtils.split(testValue,
							valueSeparator)) {
						 if(orginal.startsWith(test))
							 return AviatorBoolean.TRUE;
					}
				}
			}
			return AviatorBoolean.FALSE;
		}

		public String getName() {
			return "splitStartsWith";
		}

	}
	static class SplitEquals extends AbstractFunction {

		@Override
		public AviatorBoolean call(Map<String, Object> env, AviatorObject args1,
				AviatorObject args2, AviatorObject args3, AviatorObject args4) {

			String fieldName = (String) args1.getValue(env);
			String fieldSeparator = (String) args2.getValue(env);
			String testValue = (String) args3.getValue(env);
			String valueSeparator = (String) args4.getValue(env);
			@SuppressWarnings("unchecked")
			Map<String, String> xxxobj = (Map<String, String>) env
					.get("xxxobj");

			if (StringUtils.isEmpty(xxxobj.get(fieldName)))
				return AviatorBoolean.FALSE;
			if (StringUtils.isEmpty(valueSeparator)) {
				for (String item : StringUtils.split(xxxobj.get(fieldName),
						fieldSeparator)) {
					if(item.equalsIgnoreCase(testValue))
						 return AviatorBoolean.TRUE;
					 
				}
			} else {
				for (String orginal : StringUtils.split(xxxobj.get(fieldName),
						fieldSeparator)) {
					for (String test : StringUtils.split(testValue,
							valueSeparator)) {
						 if(orginal.equalsIgnoreCase(test))
							 return AviatorBoolean.TRUE;
					}
				}
			}
			return AviatorBoolean.FALSE;
		}

		public String getName() {
			return "splitEquals";
		}

	}

	public static void main(String[] args) {
		String aa = " xxxobj.type=='1'  && splitStartsWith('class',' ','F8','') && (xxxobj.modify==nil || xxobj.modify!='JG' )";
	
		AviatorEvaluator.addFunction(new SplitEquals());
		AviatorEvaluator.addFunction(new SplitContains());
		AviatorEvaluator.addFunction(new SplitStartsWith());
		 
		Expression express = AviatorEvaluator.compile(aa,true);
	
		Map<String, Object> env = new HashMap<String, Object>();
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("class", "F8");
		obj.put("type", '1');
		env.put("xxxobj", obj);
		
	   System.out.println(express.execute(env));
	   //System.out.println(env.get("output"));
	}
}
