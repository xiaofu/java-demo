package com.github.xiaofu.lucene.demo;

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

import static org.junit.Assert.assertTrue;

import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.store.Directory;
import org.junit.Test;

// From chapter 3
public class BooleanQueryTest {

	@Test
	public void testAnd() throws Exception {
		TermQuery searchingBooks = new TermQuery(new Term("subject", "search")); // #1

		Query books2010 = // #2
				IntPoint.newRangeQuery("pubmonth", 201001, // #2
						201012); // #2

		BooleanQuery searchingBooks2010 = new BooleanQuery.Builder().add(searchingBooks, BooleanClause.Occur.MUST)
				.add(books2010, BooleanClause.Occur.MUST).build(); // #3

		Directory dir = TestUtil.getBookIndexDirectory();
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(dir));
		TopDocs matches = searcher.search(searchingBooks2010, 10);

		assertTrue(TestUtil.hitsIncludeTitle(searcher, matches, "Lucene in Action, Second Edition"));

		dir.close();
	}

	/*
	 * #1 Match books with subject “search” #2 Match books in 2004 #3 Combines two
	 * queries
	 */
	@Test
	public void testOr() throws Exception {
		TermQuery methodologyBooks = new TermQuery( // #1
				new Term("category", // #1
						"/technology/computers/programming/methodology")); // #1

		TermQuery easternPhilosophyBooks = new TermQuery( // #2
				new Term("category", // #2
						"/philosophy/eastern")); // #2
		BooleanQuery enlightenmentBooks = new BooleanQuery.Builder().add(methodologyBooks, BooleanClause.Occur.SHOULD)
				.add(easternPhilosophyBooks, BooleanClause.Occur.SHOULD).build(); // #3
		Directory dir = TestUtil.getBookIndexDirectory();
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(dir));
		TopDocs matches = searcher.search(enlightenmentBooks, 10);
		System.out.println("or = " + enlightenmentBooks);

		assertTrue(TestUtil.hitsIncludeTitle(searcher, matches, "Extreme Programming Explained"));
		assertTrue(TestUtil.hitsIncludeTitle(searcher, matches, "Tao Te Ching \u9053\u5FB7\u7D93"));

		dir.close();
	}

	/*
	 * #1 Match 1st category #2 Match 2nd category #3 Combine
	 */
}
