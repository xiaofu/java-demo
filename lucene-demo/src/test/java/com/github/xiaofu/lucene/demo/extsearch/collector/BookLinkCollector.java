package com.github.xiaofu.lucene.demo.extsearch.collector;

import org.apache.lucene.index.DocValues;

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

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.SortedDocValues;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.LeafCollector;
import org.apache.lucene.search.Scorable;
import org.apache.lucene.search.ScoreMode;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// From chapter 6
public class BookLinkCollector implements Collector {
	private Map<String, String> documents = new HashMap<String, String>();
	private Scorable scorer;
	private SortedDocValues urls;
	private SortedDocValues titles;

	public Map<String, String> getLinks() {
		return Collections.unmodifiableMap(documents);
	}

	@Override
	public LeafCollector getLeafCollector(LeafReaderContext context) throws IOException {
		urls=DocValues.getSorted(context.reader(),"url");
		titles=DocValues.getSorted(context.reader(),"title2");
		return new LeafCollector() {
			
			@Override
			public void setScorer(Scorable scorer) throws IOException {
				BookLinkCollector.this.scorer=scorer;
				
			}
			
			@Override
			public void collect(int doc) throws IOException {
				 
				try {
					String url = getUrlsDocValues(doc); // #C
					String title = getTitlesDocValues(doc); // #C
					documents.put(url, title); // #C
					System.out.println(title + ":" + scorer.score());
				} catch (IOException e) {
					// ignore
				}
			}
		 
			private String getUrlsDocValues(int doc) throws IOException
			{
				if (urls.advanceExact(doc)) {
					return urls.binaryValue().utf8ToString();
				} else {
					return null;
				}
			}
			private String getTitlesDocValues(int doc) throws IOException
			{
				if (titles.advanceExact(doc)) {
					return titles.binaryValue().utf8ToString();
				} else {
					return null;
				}
			}
			
		};
	}
	@Override
	public ScoreMode scoreMode() {
		 
		return ScoreMode.COMPLETE;
	}
}

/*
 * #A Accept docIDs out of order #B Load FieldCache values #C Store details for
 * the match
 */
