package com.github.xiaofu.lucene.demo;

import static org.junit.Assert.*;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class FuzzyAndWildcardTest {
	Directory directory;

	private void indexSingleFieldDocs(Field[] fields) throws Exception {
		directory = new RAMDirectory();
		IndexWriter writer = new IndexWriter(directory,new IndexWriterConfig(new WhitespaceAnalyzer()));
		for (Field f : fields) {
			Document doc = new Document();
			doc.add(f);
			writer.addDocument(doc);
		}
		writer.forceMergeDeletes();
		writer.close();
	}

	@Test
	public void testWildcard() throws Exception {
		indexSingleFieldDocs(new Field[] { new TextField("contents", "wild", Store.YES),
				new TextField("contents", "child", Store.YES), new TextField("contents", "mild", Store.YES),
				new TextField("contents", "mildew", Store.YES) });
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
		Query query = new WildcardQuery(new Term("contents", "?ild*"));
		TopDocs matches = searcher.search(query, 10);
		assertEquals("child no match", 3, matches.totalHits.value);
		assertEquals("score the same", matches.scoreDocs[0].score, matches.scoreDocs[1].score, 0.0);
		assertEquals("score the same", matches.scoreDocs[1].score, matches.scoreDocs[2].score, 0.0);

	}

	@Test
	public void testFuzzy() throws Exception {
		indexSingleFieldDocs(new Field[] { new TextField("contents", "fuzzy", Field.Store.YES),
				new TextField("contents", "wuzzy", Field.Store.YES) });
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
		Query query = new FuzzyQuery(new Term("contents", "wuzza"));
		 
		TopDocs matches = searcher.search(query, 10);
		assertEquals("both close enough", 2, matches.totalHits.value);
		assertTrue("wuzzy closer than fuzzy", matches.scoreDocs[0].score != matches.scoreDocs[1].score);
		Document doc = searcher.doc(matches.scoreDocs[0].doc);

		assertEquals("wuzza bear", "wuzzy", doc.get("contents"));

	}

}
