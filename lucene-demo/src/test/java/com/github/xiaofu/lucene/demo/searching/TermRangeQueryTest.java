package com.github.xiaofu.lucene.demo.searching;

import static org.junit.Assert.assertEquals;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;

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

import org.junit.Test;

import com.github.xiaofu.lucene.demo.TestUtil;

// From chapter 3
public class TermRangeQueryTest {

	@Test
	public void testTermRangeQuery() throws Exception {
		Directory dir = TestUtil.getBookIndexDirectory();
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(dir));
		TermRangeQuery query = new TermRangeQuery("title2", new BytesRef("d"), new BytesRef("j"), true, true);
		TopDocs matches = searcher.search(query, 100);

		for (int i = 0; i < matches.totalHits.value; i++) {
			System.out.println("match " + i + ": " + searcher.doc(matches.scoreDocs[i].doc).get("title2"));
		}

		assertEquals(3, matches.totalHits.value);

		dir.close();
	}
}
