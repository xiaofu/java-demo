package com.github.xiaofu.lucene.demo.searching;

import org.apache.lucene.analysis.core.SimpleAnalyzer;

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
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;

// From chapter 3
public class Explainer {
	
	@Test
	public   void testExplain() throws Exception {
		 
		String indexDir = "tempIndex";
		String queryExpression = "junit";

		Directory directory = FSDirectory.open(Paths.get(indexDir));
		QueryParser parser = new QueryParser("contents", new SimpleAnalyzer());
		Query query = parser.parse(queryExpression);

		System.out.println("Query: " + queryExpression);

		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
		TopDocs topDocs = searcher.search(query, 10);

		for (ScoreDoc match : topDocs.scoreDocs) {
			Explanation explanation = searcher.explain(query, match.doc); // #A

			System.out.println("----------");
			Document doc = searcher.doc(match.doc);
			System.out.println(doc.get("title"));
			System.out.println(explanation.toString()); // #B
		}
		 
		directory.close();
	}
}
/*
 * #A Generate Explanation #B Output Explanation
 */
