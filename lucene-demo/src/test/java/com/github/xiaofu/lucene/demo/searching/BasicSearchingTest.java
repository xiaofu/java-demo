package com.github.xiaofu.lucene.demo.searching;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.github.xiaofu.lucene.demo.TestUtil;

public class BasicSearchingTest {

	@Test
	public void testTerm() throws Exception {
		Directory dir = TestUtil.getBookIndexDirectory();
		DirectoryReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		Term t = new Term("subject", "ant");
		Query query = new TermQuery(t);
		TopDocs docs = searcher.search(query, 10);
		assertEquals("Ant in Action", 1, docs.totalHits.value);
		t = new Term("subject", "junit");
		docs = searcher.search(new TermQuery(t), 10);
		assertEquals("Ant in Action, " + "JUnit in Action, Second Edition", 2, docs.totalHits.value);

		dir.close();
	}

	@Test
	public void testQueryParser() throws Exception {
		Directory dir = TestUtil.getBookIndexDirectory();
		DirectoryReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser("contents", new SimpleAnalyzer());
		Query query = parser.parse("+JUNIT +ANT -MOCK");
		TopDocs docs = searcher.search(query, 10);
		assertEquals(1, docs.totalHits.value);
		Document d = searcher.doc(docs.scoreDocs[0].doc);
		assertEquals("Ant in Action", d.get("title"));
		query = parser.parse("mock OR junit");
		docs = searcher.search(query, 10);
		assertEquals("Ant in Action, " + "JUnit in Action, Second Edition", 2, docs.totalHits.value);
		 
		dir.close();
	}

}
