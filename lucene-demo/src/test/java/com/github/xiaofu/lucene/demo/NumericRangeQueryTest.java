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

import org.apache.lucene.search.TopDocs;

import static org.junit.Assert.assertEquals;

import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.junit.Test;

// From chapter 3
public class NumericRangeQueryTest {
	@Test
	public void testInclusive() throws Exception {
		Directory dir = TestUtil.getBookIndexDirectory();
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(dir));
		// pub date of TTC was September 2006
		Query query = IntPoint.newRangeQuery("pubmonth", 200605, 200609);
		TopDocs matches = searcher.search(query, 10);

		for (int i = 0; i < matches.totalHits.value; i++) {
			System.out.println("match " + i + ": " + searcher.doc(matches.scoreDocs[i].doc).get("author"));
		}

		assertEquals(1, matches.totalHits.value);

		dir.close();
	}

	@Test
	public void testExclusive() throws Exception {
		Directory dir = TestUtil.getBookIndexDirectory();
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(dir));
		// pub date of TTC was September 2006
		Query query = IntPoint.newRangeQuery("pubmonth",Math.addExact(200605,1),Math.subtractExact(200609,1));
		TopDocs matches = searcher.search(query, 10);
		assertEquals(0, matches.totalHits.value);

		dir.close();
	}

}
