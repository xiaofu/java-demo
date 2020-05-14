package com.github.xiaofu.lucene.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PointRangeQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.store.Directory;
 
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// From chapter 3
public class QueryParserTest {

	private Analyzer analyzer;
	private Directory dir;
	private IndexSearcher searcher;

	@Before
	public void setUp() throws Exception {
		analyzer = new StandardAnalyzer();
		dir = TestUtil.getBookIndexDirectory();
		searcher = new IndexSearcher(DirectoryReader.open(dir));
	}

	@After
	public void tearDown() throws Exception {

		dir.close();
	}

	@Test
	public void testToString() throws Exception {
		BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
		queryBuilder.add(new FuzzyQuery(new Term("field", "kountry")), BooleanClause.Occur.MUST);
		queryBuilder.add(new TermQuery(new Term("title", "western")), BooleanClause.Occur.SHOULD);
		System.out.println(queryBuilder.build().toString());
		assertEquals("both kinds", "+kountry~2 title:western", queryBuilder.build().toString("field"));
	}

	@Test
	public void testPrefixQuery() throws Exception {
		QueryParser parser = new QueryParser("category", new StandardAnalyzer());
		// parser.setLowercaseExpandedTerms(false);
		System.out.println(parser.parse("/Computers/technology*").toString("category"));
	}

	@Test
	public void testFuzzyQuery() throws Exception {
		QueryParser parser = new QueryParser("subject", analyzer);
	 
		Query query = parser.parse("kountry~");
		assertTrue(query instanceof FuzzyQuery );
		System.out.println("fuzzy: " + query);

		query = parser.parse("kountry~0");
		System.out.println("fuzzy 2: " + query);
	}

	@Test
	public void testGrouping() throws Exception {
		Query query = new QueryParser(

				"subject", analyzer).parse("(agile OR extreme) AND methodology");
		TopDocs matches = searcher.search(query, 10);

		assertTrue(TestUtil.hitsIncludeTitle(searcher, matches, "Extreme Programming Explained"));
		assertTrue(TestUtil.hitsIncludeTitle(searcher, matches, "The Pragmatic Programmer"));
	}

	@Test
	public void testTermQuery() throws Exception {
		QueryParser parser = new QueryParser("subject", analyzer);
		Query query = parser.parse("computers");
		System.out.println("term: " + query);
	}

	@Test
	public void testTermRangeQuery() throws Exception {
		Query query = new QueryParser( // A
				"subject", analyzer).parse("title2:[Q TO V]"); // A
		assertTrue(query instanceof TermRangeQuery);
		 System.out.println(query.toString());
		TopDocs matches = searcher.search(query, 10);
		assertTrue(TestUtil.hitsIncludeTitle(searcher, matches, "Tapestry in Action"));

		query = new QueryParser("subject", analyzer) // B
				.parse("title2:{Q TO \"Tapestry in Action\"}"); // B
		System.out.println(query.toString());
		matches = searcher.search(query, 10);
		assertFalse(TestUtil.hitsIncludeTitle(searcher, matches, // C
				"Tapestry in Action"));
	}
	
	
	@Test
	public void testNumericRangeQuery() throws Exception {
		//认不了数字范围查询
		Query query = new QueryParser( // A
				"subject", analyzer).parse("pubmonth:[200605 TO 200609]"); // A
		assertFalse(query instanceof PointRangeQuery);
		 System.out.println(query.toString());
		TopDocs matches = searcher.search(query, 1);
		System.out.println(matches.totalHits.value);
		//assertTrue(TestUtil.hitsIncludeTitle(searcher, matches, "Tapestry in Action"));

		 
	}

	/*
	 * #A Verify inclusive range #B Verify exclusive range #C Exclude Mindstorms
	 * book
	 */
	@Test
	public void testPhraseQuery() throws Exception {
		//标准分析器不带停用词了
		Query q = new QueryParser("field", new StandardAnalyzer()).parse("\"This is Some Phrase*\"");
		assertEquals("analyzed", "\"this is some phrase\"", q.toString("field"));

		q = new QueryParser("field", analyzer).parse("\"term\"");
		assertTrue("reduced to TermQuery", q instanceof TermQuery);
	}

	@Test
	public void testSlop() throws Exception {
		Query q = new QueryParser("field", analyzer).parse("\"exact phrase\"");
		assertEquals("zero slop", "\"exact phrase\"", q.toString("field"));

		QueryParser qp = new QueryParser("field", analyzer);
		qp.setPhraseSlop(5);
		q = qp.parse("\"sloppy phrase\"");
		assertEquals("sloppy, implicitly", "\"sloppy phrase\"~5", q.toString("field"));
	}

	@Test
	public void testLowercasing() throws Exception {
		Query q = new QueryParser("field", analyzer).parse("PrefixQuery*");
		assertEquals("lowercased", "prefixquery*", q.toString("field"));
		assertTrue(q instanceof PrefixQuery);
		QueryParser qp = new QueryParser("field", analyzer);
		//qp.setLowercaseExpandedTerms(false);//不支持，可以使用自定义分析器来处理
		q = qp.parse("PrefixQuery?");
		assertTrue(q instanceof WildcardQuery);
		q = qp.parse("Prefix*Query");
		assertTrue(q instanceof WildcardQuery);
		q = qp.parse("PrefixQuery*");
		assertTrue(q instanceof PrefixQuery);
		//assertEquals("not lowercased", "PrefixQuery*", q.toString("field"));
	}

	@Test
	public void testWildcard() {
		try {
			new QueryParser("field", analyzer).parse("*xyz");
			fail("Leading wildcard character should not be allowed");
		} catch (ParseException expected) {
			assertTrue(true);
		}
	}

	@Test
	public void testBoost() throws Exception {
		Query q = new QueryParser("field", analyzer).parse("term^2");
		System.out.println(q.toString());
		assertEquals("(term)^2.0", q.toString("field"));
	}

	@Test
	public void testParseException() {
		try {
			new QueryParser("contents", analyzer).parse("^&#");
		} catch (ParseException expected) {
			// expression is invalid, as expected
			assertTrue(true);
			return;
		}

		fail("ParseException expected, but not thrown");
	}
}
