package com.github.xiaofu.lucene.demo.searching;

import static org.junit.Assert.*;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class NearRealTimeTest {

	@Test
	public void testNearRealTime() throws Exception {
		Directory dir = new RAMDirectory();
		IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig());
		for (int i = 0; i < 10; i++) {
			Document doc = new Document();
			doc.add(new StringField("id", "" + i,Store.NO));
			doc.add(new TextField("text", "aaa",Store.NO));
			writer.addDocument(doc);
		}
		IndexReader reader = DirectoryReader.open(writer);
		 
		IndexSearcher searcher = new IndexSearcher(reader);
		Query query = new TermQuery(new Term("text", "aaa"));
		TopDocs docs = searcher.search(query, 1);
		assertEquals(10, docs.totalHits.value);
		writer.deleteDocuments(new Term("id", "7"));
		Document doc = new Document();
		doc.add(new StringField("id", "11", Store.NO));
		doc.add(new TextField("text", "bbb", Store.NO));
		writer.addDocument(doc);
		IndexReader newReader =DirectoryReader.open(writer);
		assertFalse(reader == newReader);
		reader.close();
		searcher = new IndexSearcher(newReader);
		TopDocs hits = searcher.search(query, 10);
		assertEquals(9, hits.totalHits.value);

		query = new TermQuery(new Term("text", "bbb"));
		hits = searcher.search(query, 1);
		assertEquals(1, hits.totalHits.value);
		newReader.close();
		writer.close();
		 
	}

}
