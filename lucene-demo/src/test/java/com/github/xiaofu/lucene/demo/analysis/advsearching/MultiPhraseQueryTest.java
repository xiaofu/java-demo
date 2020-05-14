package com.github.xiaofu.lucene.demo.analysis.advsearching;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;

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

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

import com.github.xiaofu.lucene.demo.analysis.synonym.SynonymAnalyzer;
import com.github.xiaofu.lucene.demo.analysis.synonym.SynonymEngine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

// From chapter 5
public class MultiPhraseQueryTest {
	private IndexSearcher searcher;

	@Before
	public void setUp() throws Exception {
		Directory directory = new RAMDirectory();
		IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new WhitespaceAnalyzer()));
		Document doc1 = new Document();
		doc1.add(new TextField("field", "the quick brown fox jumped over the lazy dog", Field.Store.YES));
		writer.addDocument(doc1);
		Document doc2 = new Document();
		doc2.add(new TextField("field", "the fast fox hopped over the hound", Field.Store.YES));
		writer.addDocument(doc2);
		writer.close();

		searcher = new IndexSearcher(DirectoryReader.open(directory));
	}

	@Test
	public void testBasic() throws Exception {
		MultiPhraseQuery.Builder queryBuiler = new MultiPhraseQuery.Builder().add(new Term[] { // #A
				new Term("field", "quick"), // #A
				new Term("field", "fast") // #A
		}).add(new Term("field", "fox")); // #B
		System.out.println(queryBuiler.build());
		TopDocs hits = searcher.search(queryBuiler.build(), 10);
		assertEquals("fast fox match", 1, hits.totalHits.value);
		queryBuiler.setSlop(1);
		System.out.println(queryBuiler.build());
		hits = searcher.search(queryBuiler.build(), 10);
		assertEquals("both match", 2, hits.totalHits.value);
	}
	/*
	 * #A Any of these terms may be in first position to match #B Only one in second
	 * position
	 */
	@Test
	public void testAgainstOR() throws Exception {
		PhraseQuery quickFox = new PhraseQuery(1, "field", "quick", "fox");

		PhraseQuery fastFox = new PhraseQuery("field", "fast", "fox");

		BooleanQuery query = new BooleanQuery.Builder().add(quickFox, BooleanClause.Occur.SHOULD)
				.add(fastFox, BooleanClause.Occur.SHOULD).build();

		TopDocs hits = searcher.search(query, 10);
		assertEquals(2, hits.totalHits);
	}
	@Test
	public void testQueryParser() throws Exception {
		SynonymEngine engine = new SynonymEngine() {
			public String[] getSynonyms(String s) {
				if (s.equals("quick"))
					return new String[] { "fast" };
				else
					return null;
			}
		};

		Query q = new QueryParser("field", new SynonymAnalyzer(engine)).parse("\"quick fox\"");

		assertEquals("analyzed", "field:\"(quick fast) fox\"", q.toString());
		assertTrue("parsed as MultiPhraseQuery", q instanceof MultiPhraseQuery);
	}

	private void debug(TopDocs hits) throws IOException {
		for (ScoreDoc sd : hits.scoreDocs) {
			Document doc = searcher.doc(sd.doc);
			System.out.println(sd.score + ": " + doc.get("field"));
		}

	}
}