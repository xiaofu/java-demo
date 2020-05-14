package com.github.xiaofu.lucene.demo.analysis.advsearching;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

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
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.ScoreMode;
import org.apache.lucene.search.MatchAllDocsQuery;

import org.apache.lucene.search.spans.SpanFirstQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanNotQuery;
import org.apache.lucene.search.spans.SpanOrQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.SpanWeight;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

// From chapter 5
public class SpanQueryTest {
	private RAMDirectory directory;
	private IndexSearcher searcher;
	private IndexReader reader;

	private SpanTermQuery quick;
	private SpanTermQuery brown;
	private SpanTermQuery red;
	private SpanTermQuery fox;
	private SpanTermQuery lazy;
	private SpanTermQuery sleepy;
	private SpanTermQuery dog;
	private SpanTermQuery cat;
	private Analyzer analyzer;

	@Before
	public void setUp() throws Exception {
		directory = new RAMDirectory();

		analyzer = new WhitespaceAnalyzer();
		IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(analyzer));

		Document doc = new Document();
		doc.add(new TextField("f", "the quick brown fox jumps over the lazy dog", Field.Store.YES));
		writer.addDocument(doc);

		doc = new Document();
		doc.add(new TextField("f", "the quick red fox jumps over the sleepy cat", Field.Store.YES));
		writer.addDocument(doc);

		writer.close();

		searcher = new IndexSearcher(DirectoryReader.open(directory));
		reader = searcher.getIndexReader();

		quick = new SpanTermQuery(new Term("f", "quick"));
		brown = new SpanTermQuery(new Term("f", "brown"));
		red = new SpanTermQuery(new Term("f", "red"));
		fox = new SpanTermQuery(new Term("f", "fox"));
		lazy = new SpanTermQuery(new Term("f", "lazy"));
		sleepy = new SpanTermQuery(new Term("f", "sleepy"));
		dog = new SpanTermQuery(new Term("f", "dog"));
		cat = new SpanTermQuery(new Term("f", "cat"));
	}

	private void assertOnlyBrownFox(Query query) throws Exception {
		TopDocs hits = searcher.search(query, 10);
		assertEquals(1, hits.totalHits.value);
		assertEquals("wrong doc", 0, hits.scoreDocs[0].doc);
	}

	private void assertBothFoxes(Query query) throws Exception {
		TopDocs hits = searcher.search(query, 10);
		assertEquals(2, hits.totalHits.value);
	}

	private void assertNoMatches(Query query) throws Exception {
		TopDocs hits = searcher.search(query, 10);
		assertEquals(0, hits.totalHits.value);
	}

	@Test
	public void testSpanTermQuery() throws Exception {
		assertOnlyBrownFox(brown);
		dumpSpans(brown);
	}

	@Test
	public void testSpanFirstQuery() throws Exception {
		SpanFirstQuery sfq = new SpanFirstQuery(brown, 2);
		assertNoMatches(sfq);
		dumpSpans(sfq);

		sfq = new SpanFirstQuery(brown, 3);
		dumpSpans(sfq);
		assertOnlyBrownFox(sfq);
		
		sfq = new SpanFirstQuery(brown, 4);
		dumpSpans(sfq);
		assertOnlyBrownFox(sfq);
	}

	@Test
	public void testSpanNearQuery() throws Exception {
		SpanQuery[] quick_brown_dog = new SpanQuery[] { quick, brown, dog };
		SpanNearQuery snq = new SpanNearQuery(quick_brown_dog, 0, true); // #1
		assertNoMatches(snq);
		dumpSpans(snq);

		snq = new SpanNearQuery(quick_brown_dog, 4, true); // #2
		assertNoMatches(snq);
		dumpSpans(snq);

		snq = new SpanNearQuery(quick_brown_dog, 5, true); // #3
		assertOnlyBrownFox(snq);
		dumpSpans(snq);
		
		snq = new SpanNearQuery(quick_brown_dog, 6, true); // #3
		assertOnlyBrownFox(snq);
		dumpSpans(snq);
		// interesting - even a sloppy phrase query would require
		// more slop to match
		snq = new SpanNearQuery(new SpanQuery[] { lazy, fox }, 3, false);// #4
		assertOnlyBrownFox(snq);
		dumpSpans(snq);
		PhraseQuery pq = new PhraseQuery(4, "f", "lazy", "fox"); // #5
		assertNoMatches(pq);
		pq = new PhraseQuery(5, "f", "lazy", "fox");
		assertOnlyBrownFox(pq); // #6
	}

	/*
	 * #1 Query for three successive terms #2 Same terms, slop of 4 #3 SpanNearQuery
	 * matches #4 Nested SpanTermQuery objects in reverse order #5 Comparable
	 * PhraseQuery #6 PhraseQuery, slop of 5
	 */

	@Test
	public void testSpanQueryFilter() throws Exception {
		SpanQuery[] quick_brown_dog = new SpanQuery[] { quick, brown, dog };
		SpanQuery snq = new SpanNearQuery(quick_brown_dog, 5, true);
		dumpSpans(snq);
		// Filter filter = new SpanQueryFilter(snq);
		Query query = new MatchAllDocsQuery();
		BooleanQuery filter=new BooleanQuery.Builder().add(query,BooleanClause.Occur.SHOULD).add(snq,BooleanClause.Occur.FILTER).build();
		
		TopDocs hits = searcher.search(filter, 10);
		assertEquals(1, hits.totalHits.value);
		assertEquals("wrong doc", 0, hits.scoreDocs[0].doc);
	}

	@Test
	public void testSpanNotQuery() throws Exception {
		SpanNearQuery quick_fox = new SpanNearQuery(new SpanQuery[] { quick, fox }, 1, true);
		assertBothFoxes(quick_fox);
		dumpSpans(quick_fox);

		SpanNotQuery quick_fox_dog = new SpanNotQuery(quick_fox, dog);
		assertBothFoxes(quick_fox_dog);
		dumpSpans(quick_fox_dog);

		SpanNotQuery no_quick_red_fox = new SpanNotQuery(quick_fox, red);
		assertOnlyBrownFox(no_quick_red_fox);
		dumpSpans(no_quick_red_fox);
	}

	@Test
	public void testSpanOrQuery() throws Exception {
		SpanNearQuery quick_fox = new SpanNearQuery(new SpanQuery[] { quick, fox }, 1, true);

		SpanNearQuery lazy_dog = new SpanNearQuery(new SpanQuery[] { lazy, dog }, 0, true);

		SpanNearQuery sleepy_cat = new SpanNearQuery(new SpanQuery[] { sleepy, cat }, 0, true);

		SpanNearQuery qf_near_ld = new SpanNearQuery(new SpanQuery[] { quick_fox, lazy_dog }, 3, true);
		assertOnlyBrownFox(qf_near_ld);
		dumpSpans(qf_near_ld);

		SpanNearQuery qf_near_sc = new SpanNearQuery(new SpanQuery[] { quick_fox, sleepy_cat }, 3, true);
		dumpSpans(qf_near_sc);

		SpanOrQuery or = new SpanOrQuery(new SpanQuery[] { qf_near_ld, qf_near_sc });
		assertBothFoxes(or);
		dumpSpans(or);
	}

	@Test
	public void testPlay() throws Exception {
		SpanOrQuery or = new SpanOrQuery(new SpanQuery[] { quick, fox });
		dumpSpans(or);

		SpanNearQuery quick_fox = new SpanNearQuery(new SpanQuery[] { quick, fox }, 1, true);
		SpanFirstQuery sfq = new SpanFirstQuery(quick_fox, 4);
		dumpSpans(sfq);

		dumpSpans(new SpanTermQuery(new Term("f", "the")));

		SpanNearQuery quick_brown = new SpanNearQuery(new SpanQuery[] { quick, brown }, 0, false);
		dumpSpans(quick_brown);

	}

	private void dumpSpans(SpanQuery query) throws IOException {
		SpanWeight weight = query.createWeight(searcher, ScoreMode.TOP_SCORES, 1.0f);
		List<LeafReaderContext> leaves = searcher.getIndexReader().getContext().leaves();
		 
		for (LeafReaderContext leaf : leaves) {
			Spans spans = weight.getSpans(leaf, SpanWeight.Postings.POSITIONS);
			System.out.println(query + ":");
			int numSpans = 0;

			TopDocs hits = searcher.search(query, 10);
			float[] scores = new float[2];
			for (ScoreDoc sd : hits.scoreDocs) {
				scores[sd.doc] = sd.score;
			}

			while (spans.nextDoc()!= Spans.NO_MORE_DOCS) { // A
				numSpans++;
				
				int id = spans.docID();
				Document doc = reader.document(id); // B

				TokenStream stream = analyzer.tokenStream("contents", // C
						new StringReader(doc.get("f"))); // C
				CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);

				StringBuilder buffer = new StringBuilder();
				buffer.append("   ");
				int i = 0;
				spans.nextStartPosition();
				stream.reset();
				while (stream.incrementToken() ) { // D
					// spans.nextStartPosition();
					if (i == spans.startPosition()) { // E
						buffer.append("<"); // E
					} // E
					buffer.append(term.toString()); // E
					if (i + 1 == spans.endPosition()) { // E
						buffer.append(">"); // E
						spans.nextStartPosition();
					} // E
					buffer.append(" ");
					i++;
				}
				stream.end();
				stream.close();
				buffer.append("(").append(scores[id]).append(") ");
				System.out.println(buffer);
			}

			if (numSpans == 0) {
				System.out.println("   No spans");
			}
			System.out.println();
		}
	}
	// A Step through each span
	// B Retrieve document
	// C Re-analyze text
	// D Step through all tokens
	// E Print < and > around span
}
