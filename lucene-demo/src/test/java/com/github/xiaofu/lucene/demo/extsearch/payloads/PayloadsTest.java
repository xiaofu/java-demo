package com.github.xiaofu.lucene.demo.extsearch.payloads;

import static org.junit.Assert.assertEquals;

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

import java.io.IOException;

import org.apache.lucene.analysis.payloads.PayloadHelper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.xiaofu.lucene.demo.TestUtil;

import org.apache.lucene.index.Term;
import org.apache.lucene.queries.payloads.AveragePayloadFunction;
import org.apache.lucene.queries.payloads.PayloadDecoder;
import org.apache.lucene.queries.payloads.PayloadScoreQuery;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.TermQuery;

// From chapter 6
public class PayloadsTest {

	Directory dir;
	IndexWriter writer;
	BulletinPayloadsAnalyzer analyzer;

	@Before
	public void setUp() throws Exception {

		dir = new RAMDirectory();
		analyzer = new BulletinPayloadsAnalyzer(5.0F); // #A
		writer = new IndexWriter(dir, new IndexWriterConfig(analyzer));
	}

	@After
	public void tearDown() throws Exception {

		writer.close();
	}

	void addDoc(String title, String contents) throws IOException {
		Document doc = new Document();
		doc.add(new StoredField("title", title));
		doc.add(new TextField("contents", contents, Field.Store.NO));
		//默认分析器会重用分词器，即createComponents方法只会被 创建一次，需要对重写分析器的的重用策略
		analyzer.setIsBulletin(contents.startsWith("Bulletin:"));
		writer.addDocument(doc);
	}

	@Test
	public void testPayloadTermQuery() throws Throwable {
		addDoc("Hurricane warning", "Bulletin: A hurricane warning was issued at " + "6 AM for the outer great banks");
		addDoc("Warning label maker",
				"The warning label maker is a delightful toy for " + "your precocious seven year old's warning needs");
		addDoc("Tornado warning", "Bulletin: There is a tornado warning for " + "Worcester county until 6 PM today");


		IndexReader reader=DirectoryReader.open(writer);
		IndexSearcher searcher = new IndexSearcher(reader);

		 //searcher.setSimilarity(new BoostingSimilarity());

		Term warning = new Term("contents", "warning");
		SpanQuery warningQuery = new SpanTermQuery(warning);
		Query query1 = new TermQuery(warning);
		System.out.println("\nTermQuery results:");
		TopDocs hits = searcher.search(query1, 10);
		TestUtil.dumpHits(searcher, hits);

		assertEquals("Warning label maker", // #B
				searcher.doc(hits.scoreDocs[0].doc).get("title")); // #B

		Query query2 = new PayloadScoreQuery(warningQuery, new AveragePayloadFunction(), new PayloadDecoder() {

			@Override
			public float computePayloadFactor(BytesRef payload) {
				//有的记录没有负载，所以返回null
				if(payload==null)
					return 1;
				return PayloadHelper.decodeFloat(payload.bytes);
			}
		});
		System.out.println("\nPayloadTermQuery results:");
		hits = searcher.search(query2, 10);
		TestUtil.dumpHits(searcher, hits);

		assertEquals("Warning label maker", // #C
				searcher.doc(hits.scoreDocs[2].doc).get("title")); // #C
		reader.close();

	}
}

/*
 * #A Boost by 5.0 #B Ranks first #C Ranks last after boosts
 */
