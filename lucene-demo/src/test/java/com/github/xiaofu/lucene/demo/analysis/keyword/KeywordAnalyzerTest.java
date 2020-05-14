package com.github.xiaofu.lucene.demo.analysis.keyword;

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

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import org.junit.Before;
import org.junit.Test;

import com.github.xiaofu.lucene.demo.TestUtil;

// From chapter 4
public class KeywordAnalyzerTest {

	private IndexSearcher searcher;

	@Before
	public void setUp() throws Exception {
		Directory directory = new RAMDirectory();

		IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new SimpleAnalyzer()));

		Document doc = new Document();
		doc.add(new StringField("partnum", "Q36", Field.Store.NO)); // A
		doc.add(new TextField("description", "Illidium Space Modulator", Field.Store.YES));
		writer.addDocument(doc);

		writer.close();

		searcher = new IndexSearcher(DirectoryReader.open(directory));
	}

	@Test
	public void testTermQuery() throws Exception {
		Query query = new TermQuery(new Term("partnum", "Q36")); // B
		assertEquals(1, TestUtil.hitCount(searcher, query)); // C
	}

	@Test
	public void testBasicQueryParser() throws Exception {
		Query query = new QueryParser( // 1
				"description", // 1
				new SimpleAnalyzer()) // 1
						.parse("partnum:Q36 AND SPACE"); // 1
		assertEquals("note Q36 -> q", "+partnum:q +space", query.toString("description")); // 2
		assertEquals("doc not found :(", 0, TestUtil.hitCount(searcher, query));
	}

	/*
	 * #A Don't analyze field #B Don't analyze term #C Verify document matches #1
	 * QueryParser analyzes each term and phrase #2 toString() method
	 */
	@Test
	public void testPerFieldAnalyzer() throws Exception {
		Map<String, Analyzer> analyzerPerField = new HashMap<>();
		analyzerPerField.put("partnum", new KeywordAnalyzer());

		PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(new SimpleAnalyzer(),analyzerPerField);

		Query query = new QueryParser("description", analyzer).parse("partnum:Q36 AND SPACE");

		assertEquals("Q36 kept as-is", "+partnum:Q36 +space", query.toString("description"));
		assertEquals("doc found!", 1, TestUtil.hitCount(searcher, query));
	}
}
