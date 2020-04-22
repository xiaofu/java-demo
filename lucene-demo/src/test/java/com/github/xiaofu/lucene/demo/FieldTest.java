package com.github.xiaofu.lucene.demo;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.document.SortedDocValuesField;
import org.junit.Test;

public class FieldTest {

	public void addStringField(Document document, String name, String value) {
	    Field field = new StringField(name, value, Field.Store.YES);
	    document.add(field);
	    //如何不添加此字段，查询时指定此字段排序报错
	    field = new SortedDocValuesField(name, new BytesRef(value));
	    document.add(field);
	    
	}
	@Test
	public void testStringFieldSort() throws IOException {
	    Document document = new Document();
	    Directory directory = new RAMDirectory();
	    IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
	    addStringField(document, "stringValue", "1234");
	    indexWriter.addDocument(document);
	    document = new Document();
	    addStringField(document, "stringValue", "2345");
	    indexWriter.addDocument(document);
	    document = new Document();
	    addStringField(document, "stringValue", "12345");
	    indexWriter.addDocument(document);
	    indexWriter.close();
	    IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
	    SortField stringValue = new SortField("stringValue", SortField.Type.STRING, true);
	    TopFieldDocs search = indexSearcher.search(new MatchAllDocsQuery(), 10, new Sort(stringValue));
	    ScoreDoc[] scoreDocs = search.scoreDocs;
	    for (ScoreDoc scoreDoc : scoreDocs) {
	        System.out.println(indexSearcher.doc(scoreDoc.doc));
	    }
	}

}
