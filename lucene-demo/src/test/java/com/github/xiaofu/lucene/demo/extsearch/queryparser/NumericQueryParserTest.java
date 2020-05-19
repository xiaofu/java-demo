package com.github.xiaofu.lucene.demo.extsearch.queryparser;

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
import org.apache.lucene.search.Query;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.search.IndexSearcher;

import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.store.Directory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.xiaofu.lucene.demo.TestUtil;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;

import static org.junit.Assert.assertTrue;

import java.util.Locale;

// From chapter 6
public class NumericQueryParserTest {
	private Analyzer analyzer;
	private IndexSearcher searcher;
	private Directory dir;

	@Before
	public void setUp() throws Exception {
		analyzer = new WhitespaceAnalyzer();
		dir = TestUtil.getBookIndexDirectory();
		searcher = new IndexSearcher(DirectoryReader.open(dir));
	}

	@After
	public void tearDown() throws Exception {

		dir.close();
	}

	static class NumericRangeQueryParser extends QueryParser {
		public NumericRangeQueryParser(String field, Analyzer a) {
			super(field, a);
		}

		@Override
		protected Query getRangeQuery(String field, String part1, String part2, boolean startInclusive,
				boolean endInclusive) throws ParseException {
			TermRangeQuery query = (TermRangeQuery) // A
			super.getRangeQuery(field, part1, part2, // A
					startInclusive, endInclusive); // A
			if ("price".equals(field)) {
				double lowerValue=Double.parseDouble( // B
						query.getLowerTerm().utf8ToString());
				if(!startInclusive)
					lowerValue=DoublePoint.nextUp(lowerValue);
				double upperValue=Double.parseDouble( // B
						query.getUpperTerm().utf8ToString());
				if(!endInclusive)
					upperValue=DoublePoint.nextDown(upperValue);
				return DoublePoint.newRangeQuery( // B
						"price", // B
						lowerValue, // B
						upperValue); // B
			} else {
				return query; // C
			}
		}
	}

	/*
	 * #A Get super()'s default TermRangeQuery #B Create matching NumericRangeQuery
	 * #C Return default TermRangeQuery
	 */

	@Test
	public void testNumericRangeQuery() throws Exception {
		String expression = "price:[10 TO 20]";

		QueryParser parser = new NumericRangeQueryParser("subject", analyzer);

		Query query = parser.parse(expression);
		System.out.println(expression + " parsed to " + query);
	}

	public static class NumericDateRangeQueryParser extends QueryParser {
		public NumericDateRangeQueryParser(String field, Analyzer a) {
			super(field, a);
		}

		@Override
		protected Query getRangeQuery(String field, String part1, String part2, boolean startInclusive,
				boolean endInclusive) throws ParseException {
			TermRangeQuery query = (TermRangeQuery) super.getRangeQuery(field, part1, part2, startInclusive,
					endInclusive);

			if ("pubmonth".equals(field)) {
				int lowerValue=Integer.parseInt(query.getLowerTerm().utf8ToString());
				if(!startInclusive)
					lowerValue=Math.addExact(lowerValue, 1);//检测溢出
				int upperValue=Integer.parseInt(query.getUpperTerm().utf8ToString());
				if(!endInclusive)
					upperValue=Math.addExact(upperValue, -1);//检测溢出
				return IntPoint.newRangeQuery("pubmonth", lowerValue,upperValue);
			} else {
				return query;
			}
		}
	}

	@Test
	public void testDefaultDateRangeQuery() throws Exception {
		QueryParser parser = new QueryParser("subject", analyzer);
		Query query = parser.parse("pubmonth:[1/1/04 TO 12/31/04]");
		System.out.println("default date parsing: " + query);
	}

	@Test
	public void testDateRangeQuery() throws Exception {
		String expression = "pubmonth:[01/01/2010 TO 06/01/2010]";

		QueryParser parser = new NumericDateRangeQueryParser("subject", analyzer);

		parser.setDateResolution("pubmonth", DateTools.Resolution.MONTH); // 1
		parser.setLocale(Locale.US);

		Query query = parser.parse(expression);
		System.out.println(expression + " parsed to " + query);

		TopDocs matches = searcher.search(query, 10);
		assertTrue("expecting at least one result !", matches.totalHits.value > 0);
	}
	/*
	 * 1 Tell QueryParser date resolution
	 */
}
