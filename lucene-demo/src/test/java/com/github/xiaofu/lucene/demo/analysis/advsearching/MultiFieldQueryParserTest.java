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

import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import com.github.xiaofu.lucene.demo.TestUtil;

import org.apache.lucene.store.Directory;
import org.junit.Test;

// From chapter 5
public class MultiFieldQueryParserTest {
	
	@Test
	public void testDefaultOperator() throws Exception {
		Query query = new MultiFieldQueryParser(new String[] { "title", "subject" }, new SimpleAnalyzer())
				.parse("development");
		System.out.println(query.toString());
		Directory dir = TestUtil.getBookIndexDirectory();
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(dir));
		TopDocs hits = searcher.search(query, 10);

		assertTrue(TestUtil.hitsIncludeTitle(searcher, hits, "Ant in Action"));

		assertTrue(TestUtil.hitsIncludeTitle( // A
				searcher, // A
				hits, // A
				"Extreme Programming Explained")); // A

		dir.close();
	}
	
	@Test
	public void testSpecifiedOperator() throws Exception {
		Query query = MultiFieldQueryParser.parse("lucene", new String[] { "title", "subject" },
				new BooleanClause.Occur[] { BooleanClause.Occur.MUST, BooleanClause.Occur.MUST }, new SimpleAnalyzer());

		Directory dir = TestUtil.getBookIndexDirectory();
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(dir));
		TopDocs hits = searcher.search(query, 10);

		assertTrue(TestUtil.hitsIncludeTitle(searcher, hits, "Lucene in Action, Second Edition"));
		assertEquals("one and only one", 1, hits.scoreDocs.length);

		dir.close();
	}

	/*
	 * #A Has development in the subject field
	 */

}
