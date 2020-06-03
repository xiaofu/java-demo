package com.github.xiaofu.lucene.demo.searching;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.LogDocMergePolicy;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class IndexerTest {

	protected String[] ids = { "1", "2" };
	protected String[] unindexed = { "Netherlands", "Italy" };
	protected String[] unstored = { "Amsterdam has lots of bridges", "Venice has lots of canals" };
	protected String[] text = { "Amsterdam", "Venice" };
	private Directory directory;

	@Before
	public void setUp() throws Exception {
		directory =   FSDirectory.open(Paths.get("tempIndex"));
		IndexWriter writer = getWriter();
		for (int i = 0; i < ids.length; i++) {
			Document doc = new Document();
			doc.add(new StringField("id", ids[i],Store.YES));
			doc.add(new StoredField("country", unindexed[i]));
			doc.add(new TextField("contents", unstored[i],Store.NO));
			doc.add(new TextField("city", text[i], Store.YES));
			writer.addDocument(doc);
		}
	    
		writer.close();
	}

	private IndexWriter getWriter() throws IOException {
		IndexWriterConfig conf = new IndexWriterConfig(new WhitespaceAnalyzer());
		conf.setMergePolicy(new LogDocMergePolicy());
		conf.setInfoStream(System.out);
		conf.setOpenMode(OpenMode.CREATE);
		return new IndexWriter(directory, conf);
	}

	protected int getHitCount(String fieldName, String searchString) throws IOException {
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
		Term t = new Term(fieldName, searchString);
		Query query = new TermQuery(t);
		int hitCount = (int) searcher.search(query, 1).totalHits.value;
		return hitCount;
	}

	@Test
	public void testIndexWriter() throws IOException {
		IndexWriter writer = getWriter();
  
		assertEquals(ids.length, writer.getDocStats().numDocs);
		writer.close();
	}

	@Test
	public void testIndexReader() throws IOException {
		IndexReader reader = DirectoryReader.open(directory);
 
		assertEquals(ids.length, reader.maxDoc());
		assertEquals(ids.length, reader.numDocs());
		reader.close();
	}

	@Test
	public void testDeleteBeforeOptimize() throws IOException {
		IndexWriter writer = getWriter();
		assertEquals(2, writer.getDocStats().maxDoc);
		writer.deleteDocuments(new Term("id", "1"));
		writer.commit();
		assertTrue(writer.hasDeletions());
		assertEquals(2, writer.getDocStats().maxDoc);
		assertEquals(1, writer.getDocStats().numDocs);
		writer.close();
	}

	@Test
	public void testDeleteAfterOptimize() throws IOException {
		IndexWriter writer = getWriter();
		assertEquals(2, writer.getDocStats().numDocs);
		writer.deleteDocuments(new Term("id", "1"));
		writer.forceMergeDeletes();
		writer.commit();

		assertFalse(writer.hasDeletions());
		assertEquals(1, writer.getDocStats().numDocs);
		assertEquals(1, writer.getDocStats().maxDoc);
		writer.close();
	}

	@Test
	public void testUpdate() throws IOException {
		assertEquals(1, getHitCount("city", "Amsterdam"));
		IndexWriter writer = getWriter();
		Document doc = new Document();
		doc.add(new StringField("id", "1", Store.YES));
		doc.add(new StoredField("country", "Netherlands"));
		doc.add(new TextField("contents", "Den Haag has a lot of museums", Store.NO));
		doc.add(new TextField("city", "Den Haag", Store.YES));
		 
		writer.updateDocument(new Term("id", "1"), doc);
		writer.close();
		assertEquals(0, getHitCount("city", "Amsterdam"));
		assertEquals(1, getHitCount("city", "Haag"));//OK
		//assertEquals(1, getHitCount("city", "Den Haag"));
		 
	}

}
