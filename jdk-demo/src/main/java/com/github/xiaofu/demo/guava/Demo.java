package com.github.xiaofu.demo.guava;

import java.lang.ref.ReferenceQueue;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

 



import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

enum Type
{
	T1, T2
}

public class Demo
{
	public static void main(String[] args)
	{
		Multimap<String, String> mulHashMap = HashMultimap.create();
		mulHashMap = LinkedHashMultimap.create();
		mulHashMap = ArrayListMultimap.create();
		mulHashMap = TreeMultimap.create();
		mulHashMap = LinkedListMultimap.create();
		mulHashMap.put("key1", "12");
		mulHashMap.put("key2", "13");
		mulHashMap.put("key1", "14");
		for (String string : mulHashMap.keys())
		{
			System.out.println("key:" + string);
		}
		for (Entry<String, String> entry : mulHashMap.entries())
		{
			System.out.println(entry.getKey() + ":" + entry.getValue());

		}
		Multiset<String> mulSet = HashMultiset.create();
		mulSet.add("a");
		mulSet.add("a");
		for (String item : mulSet)
		{
			System.out.println(item);
		}
		Multiset<Type> mulSetType = EnumMultiset.create(Type.class);
		mulSet = LinkedHashMultiset.create();
		mulSet = TreeMultiset.create();
	}

	private static void Cache()
	{
		 
		final ListeningExecutorService backgroundRefreshPools = MoreExecutors
				.listeningDecorator(Executors.newFixedThreadPool(20));
		LoadingCache<String, Object> caches = CacheBuilder.newBuilder()
				.maximumSize(100).refreshAfterWrite(10, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Object>()
				{
					@Override
					public Object load(String key) throws Exception
					{
						return new Object();
					}

					@Override
					public ListenableFuture<Object> reload(String key,
							Object oldValue) throws Exception
					{
						return backgroundRefreshPools
								.submit(new Callable<Object>()
								{
									@Override
									public Object call() throws Exception
									{
										return new Object();
									}
								});
					}
				});
		try
		{
			System.out.println(caches.get("key-zorro"));
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
		}

	}
}
