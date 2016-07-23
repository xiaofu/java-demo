package com.github.xiaofu.demo.guava;

import java.util.Collection;
import java.util.Map.Entry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.EnumMultiset;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultimap;
import com.google.common.collect.TreeMultiset;

enum Type
{
	T1,T2
}
 

public class Demo {
	public static void main(String[] args)
	{
		Multimap<String,String> mulHashMap= HashMultimap.create();	
		mulHashMap=LinkedHashMultimap.create();
		mulHashMap=ArrayListMultimap.create();
		mulHashMap=TreeMultimap.create();
		mulHashMap=LinkedListMultimap.create();
		mulHashMap.put("key1", "12");
		mulHashMap.put("key2", "13");
		mulHashMap.put("key1", "14");
		for (String string : mulHashMap.keys()) {
		System.out.println("key:"+string);	
		}
		for (Entry<String, String> entry : mulHashMap.entries()) {
			System.out.println(entry.getKey()+":"+entry.getValue());
			 
		}
		Multiset<String> mulSet=HashMultiset.create();
		mulSet.add("a");
		mulSet.add("a");
		for (String item : mulSet) {
			System.out.println(item);
		}
		Multiset<Type> mulSetType=EnumMultiset.create(Type.class);
		mulSet=LinkedHashMultiset.create();
		mulSet=TreeMultiset.create();
	}
}
