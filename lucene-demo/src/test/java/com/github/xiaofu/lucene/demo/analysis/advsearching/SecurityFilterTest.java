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
 

import static org.junit.Assert.assertEquals;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;

import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import com.github.xiaofu.lucene.demo.TestUtil;

// From chapter 5
public class SecurityFilterTest   {

	private IndexSearcher searcher;

	@Before
	public void setUp() throws Exception {
		Directory directory = new RAMDirectory();
		IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new WhitespaceAnalyzer()));

		Document document = new Document(); // 1
		document.add(new StringField("owner", // 1
				"elwood", // 1
				Field.Store.YES)); // 1
		document.add(new TextField("keywords", // 1
				"elwood's sensitive info", // 1
				Field.Store.YES)); // 1
		writer.addDocument(document);

		document = new Document(); // 2
		document.add(new StringField("owner", // 2
				"jake", // 2
				Field.Store.YES)); // 2
		document.add(new TextField("keywords", // 2
				"jake's sensitive info", // 2
				Field.Store.YES)); // 2
		writer.addDocument(document);

		writer.close();
		searcher = new IndexSearcher(DirectoryReader.open(directory));
	}
	/*
	 * #1 Elwood #2 Jake
	 */

	@Test
	public void testSecurityFilter() throws Exception {
    TermQuery query = new TermQuery(                   //#1
                        new Term("keywords", "info")); //#1

    assertEquals("Both documents match",               //#2
                 2,                                    //#2
                 TestUtil.hitCount(searcher, query));  //#2

    TermQuery jakeFilter =           //#3
      new TermQuery(new Term("owner", "jake"));       //#3
    BooleanQuery.Builder queryBuilder=new BooleanQuery.Builder().add(query,Occur.MUST).add(jakeFilter,Occur.FILTER);
    System.out.println(queryBuilder.build().toString());
    TopDocs hits = searcher.search(queryBuilder.build(), 10);
    assertEquals(1, hits.totalHits.value);                   //#4
    assertEquals("elwood is safe",                     //#4
                 "jake's sensitive info",              //#4
        searcher.doc(hits.scoreDocs[0].doc)            //#4
                 .get("keywords"));                    //#4
  }
	/*
	 * #1 TermQuery for "info" #2 Returns documents containing "info" #3 Filter #4
	 * Same TermQuery, constrained results
	 */
}
