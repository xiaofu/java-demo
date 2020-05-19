package com.github.xiaofu.lucene.demo;

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
import java.util.List;
import java.util.ArrayList;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.LeafCollector;
import org.apache.lucene.search.Scorable;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.ScoreMode;

// From chapter 6

/**
 * Gathers all documents from a search.
 */

public class AllDocCollector implements Collector {
	List<ScoreDoc> docs = new ArrayList<ScoreDoc>();
	private Scorable scorer;
	private int docBase;

 
	public void reset() {
		docs.clear();
	}

	public List<ScoreDoc> getHits() {
		return docs;
	}

	@Override
	public LeafCollector getLeafCollector(LeafReaderContext context) throws IOException {
		 docBase=context.docBase;
		return new LeafCollector() {
			@Override
			public void setScorer(Scorable scorer) throws IOException {
				AllDocCollector.this.scorer=scorer;
			}
			
			@Override
			public void collect(int doc) throws IOException {
				docs.add(new ScoreDoc(doc + docBase, // #A
						scorer.score())); // #B
				
			}
		};
	}

	@Override
	public ScoreMode scoreMode() {

		return ScoreMode.COMPLETE;
	}
}

/*
 * #A Create absolute docID #B Record score
 */
