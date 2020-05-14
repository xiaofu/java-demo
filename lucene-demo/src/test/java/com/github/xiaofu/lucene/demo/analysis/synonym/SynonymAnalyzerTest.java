package com.github.xiaofu.lucene.demo.analysis.synonym;

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

import java.io.StringReader;

import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.PhraseQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.xiaofu.lucene.demo.TestUtil;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

// From chapter 4
public class SynonymAnalyzerTest {
	private IndexSearcher searcher;
	private static SynonymAnalyzer synonymAnalyzer = new SynonymAnalyzer(new TestSynonymEngine());

	@Before
	public void setUp() throws Exception {
		RAMDirectory directory = new RAMDirectory();

		IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(synonymAnalyzer));
		Document doc = new Document();
		doc.add(new TextField("content", "The quick brown fox jumps over the lazy dog", Field.Store.YES)); // #2
		writer.addDocument(doc);

		writer.close();

		searcher = new IndexSearcher(DirectoryReader.open(directory));
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testJumps() throws Exception {
		TokenStream stream = synonymAnalyzer.tokenStream("contents", // #A
				new StringReader("jumps")); // #A
		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		PositionIncrementAttribute posIncr = stream.addAttribute(PositionIncrementAttribute.class);

		int i = 0;
		String[] expected = new String[] { "jumps", // #B
				"hops", // #B
				"leaps" }; // #B
		stream.reset();
		while (stream.incrementToken()) {
			assertEquals(expected[i], term.toString());

			int expectedPos; // #C
			if (i == 0) { // #C
				expectedPos = 1; // #C
			} else { // #C
				expectedPos = 0; // #C
			} // #C
			assertEquals(expectedPos, // #C
					posIncr.getPositionIncrement()); // #C
			i++;
		}
		stream.close();
		assertEquals(3, i);
	}

	/*
	 * #A Analyze with SynonymAnalyzer #B Check for correct synonyms #C Verify
	 * synonyms positions
	 */
	@Test
	public void testSearchByAPI() throws Exception {

		TermQuery tq = new TermQuery(new Term("content", "hops")); // #1
		assertEquals(1, TestUtil.hitCount(searcher, tq));

		PhraseQuery pq = new PhraseQuery("content", "fox", "hops"); // #2

		assertEquals(1, TestUtil.hitCount(searcher, pq));
	}

	/*
	 * #1 Search for "hops" #2 Search for "fox hops"
	 */
	@Test
	public void testWithQueryParser() throws Exception {
		Query query = new QueryParser( // 1
				"content", // 1
				synonymAnalyzer).parse("\"fox jumps\""); // 1
		assertEquals(1, TestUtil.hitCount(searcher, query)); // 1
		System.out.println("With SynonymAnalyzer, \"fox jumps\" parses to " + query.toString("content"));

		query = new QueryParser( // 2
				"content", // 2
				new ClassicAnalyzer()).parse("\"fox jumps\""); // B
		assertEquals(1, TestUtil.hitCount(searcher, query)); // 2
		System.out.println("With ClassicAnalyzer, \"fox jumps\" parses to " + query.toString("content"));
		query = new QueryParser( // 2
				"content", // 2
				new ClassicAnalyzer()).parse("\"fox leaps\""); // B
		assertEquals(1, TestUtil.hitCount(searcher, query)); // 2
		System.out.println("With ClassicAnalyzer, \"fox leaps\" parses to " + query.toString("content"));
	}

	/*
	 * #1 SynonymAnalyzer finds the document #2 StandardAnalyzer also finds document
	 */
}
