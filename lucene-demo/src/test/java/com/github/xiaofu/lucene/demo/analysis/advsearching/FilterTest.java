package com.github.xiaofu.lucene.demo.analysis.advsearching;

/**
 * Copyright Manning Publications Co.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific lan      
*/

import junit.framework.TestCase;

import static org.junit.Assert.assertEquals;

import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.DocValuesTermsQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.LRUQueryCache;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.xiaofu.lucene.demo.TestUtil;

/**
 * lucene 5.x 以上抛弃 ，6.x 以上移出filter，使用 ConstantScoreQuery or in a
 * BooleanClause.Occur.FILTER clause代替
 * 
 * @author fulaihua
 *
 */
// From chapter 5
public class FilterTest {

	private IndexSearcher searcher;
	private Directory dir;
	BooleanQuery.Builder allBooksBuilder;

	@Before
	public void setUp() throws Exception { // #1
		allBooksBuilder = new BooleanQuery.Builder();
		allBooksBuilder.add(new MatchAllDocsQuery(), Occur.MUST);
		dir = TestUtil.getBookIndexDirectory();
		searcher = new IndexSearcher(DirectoryReader.open(dir));
	}

	@After
	public void tearDown() throws Exception {

		dir.close();
	}

	@Test
	public void testTermRangeFilter() throws Exception {
		Query filter = TermRangeQuery.newStringRange("title2", "d", "j", true, true);
		allBooksBuilder.add(filter, Occur.FILTER);
		assertEquals(3, TestUtil.hitCount(searcher, allBooksBuilder.build()));
	}

	/*
	 * #1 setUp() establishes baseline book count
	 */
	@Test
	public void testNumericDateFilter() throws Exception {
		// pub date of Lucene in Action, Second Edition and
		// JUnit in ACtion, Second Edition is May 2010
		Query filter = IntPoint.newRangeQuery("pubmonth", 201001, 201006);
		allBooksBuilder.add(filter, Occur.FILTER);
		assertEquals(2, TestUtil.hitCount(searcher, allBooksBuilder.build()));
	}

	@Test
	public void testFieldCacheRangeFilter() throws Exception {
		//建议使用IndexOrDocValuesQuery替代
		Query filter = SortedDocValuesField.newSlowRangeQuery("title2", new BytesRef("d"), new BytesRef("j"), true,
				true);
		allBooksBuilder.add(filter, Occur.FILTER);
		assertEquals(3, TestUtil.hitCount(searcher, allBooksBuilder.build()));
		//建议使用IndexOrDocValuesQuery替代
		filter = NumericDocValuesField.newSlowRangeQuery("pubmonth", 201001, 201006);
		allBooksBuilder = new BooleanQuery.Builder();
		allBooksBuilder.add(new MatchAllDocsQuery(), Occur.MUST);
		allBooksBuilder.add(filter, Occur.FILTER);
		assertEquals(2, TestUtil.hitCount(searcher, allBooksBuilder.build()));
	}

	@Test
	public void testFieldCacheTermsFilter() throws Exception {
		//功能和TermInSetQuery类似，在某些情况快，某些情况更慢
		Query filter = new DocValuesTermsQuery("category", new String[] { "/health/alternative/chinese",
				"/technology/computers/ai", "/technology/computers/programming" });
		allBooksBuilder.add(filter, Occur.FILTER);
		assertEquals("expected 7 hits", 7, TestUtil.hitCount(searcher, allBooksBuilder.build()));
	}
	/**
	 * 没有找到替代类
	 * @throws Exception
	 */
	@Test
	public void testQueryWrapperFilter() throws Exception {
		TermQuery categoryQuery = new TermQuery(new Term("category", "/philosophy/eastern"));

		//Query categoryFilter = new ConstantScoreQuery(categoryQuery);//???貌似不对
		allBooksBuilder.add(categoryQuery, Occur.FILTER);
		assertEquals("only tao te ching", 1, TestUtil.hitCount(searcher, allBooksBuilder.build()));
	}
	/**
	 * 此方法过不了，没有添加位置信息
	 * @throws Exception
	 */
	@Test
	public void testSpanQueryFilter() throws Exception {
		SpanQuery categoryQuery = new SpanTermQuery(new Term("category", "/philosophy/eastern"));

		allBooksBuilder.add(categoryQuery, Occur.FILTER);
		assertEquals("only tao te ching", 1, TestUtil.hitCount(searcher, allBooksBuilder.build()));
	}

	@Test
	public void testFilterAlternative() throws Exception {
		TermQuery categoryQuery = new TermQuery(new Term("category", "/philosophy/eastern"));

		BooleanQuery constrainedQuery = new BooleanQuery.Builder().add(new MatchAllDocsQuery(), Occur.MUST)
				.add(categoryQuery, BooleanClause.Occur.MUST).build();

		assertEquals("only tao te ching", 1, TestUtil.hitCount(searcher, constrainedQuery));
	}

	@Test
	public void testPrefixFilter() throws Exception {
		Query prefixFilter = new PrefixQuery(new Term("category", "/technology/computers"));
		allBooksBuilder.add(prefixFilter,Occur.FILTER);
		assertEquals("only /technology/computers/* books", 8, TestUtil.hitCount(searcher, allBooksBuilder.build()));
	}

	@Test
	public void testCachingWrapper() throws Exception {
		Query filter =   TermRangeQuery.newStringRange("title2", "d", "j", true, true);
		allBooksBuilder.add(filter, Occur.FILTER);
		//默认已经有了查询缓存了，不再需要设置
		//searcher.setQueryCache(new LRUQueryCache(maxSize, maxRamBytesUsed));
		assertEquals(3, TestUtil.hitCount(searcher, allBooksBuilder.build()));
	}
}
