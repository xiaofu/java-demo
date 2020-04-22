package com.github.xiaofu.lucene.demo;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class Searcher {

	public static void main(String[] args) throws Exception {

		String indexDir = "tempIndex";
		String q = "binary";
		search(indexDir, q);

	}

	public static void search(String indexDir, String q) throws IOException, ParseException, Exception {
		DirectoryReader  reader = DirectoryReader.open( FSDirectory.open(Paths.get(indexDir)));
		IndexSearcher is = new IndexSearcher(reader);
		QueryParser  parser = new QueryParser("contents", new StandardAnalyzer());
		Query query = parser.parse(q);
		long start = System.currentTimeMillis();
		TopDocs hits = is.search(query, 10);
		 
		long end = System.currentTimeMillis();
		System.err.println("Found " + hits.totalHits + " document(s) (in " + (end - start)
				+ " milliseconds) that matched query '" + q + "':");
		 
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			
			Document doc = is.doc(scoreDoc.doc);
			System.out.println(scoreDoc.score);
			System.out.println(doc.get("fullpath"));
		}
		reader.close();

		
	}

}
