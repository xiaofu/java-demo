package com.github.xiaofu.lucene.demo.extsearch.queryparser;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
 
import org.junit.Test;

// From chapter 6
public class AdvancedQueryParserTest {
	private Analyzer analyzer = new WhitespaceAnalyzer();

	@Test
	public void testCustomQueryParser() {
		CustomQueryParser parser = new CustomQueryParser("field", analyzer);
		try {
			parser.parse("a?t");
			fail("Wildcard queries should not be allowed");
		} catch (ParseException expected) {
			// 1
		}

		try {
			parser.parse("xunit~");
			fail("Fuzzy queries should not be allowed");
		} catch (ParseException expected) {
			// 1
		}
	}
	/*
	 * 1 Expected
	 */

	@Test
	public void testPhraseQuery() throws Exception {
		CustomQueryParser parser = new CustomQueryParser("field", analyzer);

		Query query = parser.parse("singleTerm");
		assertTrue("TermQuery", query instanceof TermQuery);

		query = parser.parse("\"a phrase\"");
		assertTrue("SpanNearQuery", query instanceof SpanNearQuery);
	}

}
