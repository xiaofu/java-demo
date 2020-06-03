package com.github.xiaofu.lucene.demo.searching;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.junit.Test;

public class FieldTest {

	public void addStringField(Document document, String name, String value) {
		Field field = new StringField(name, value, Field.Store.YES);
		document.add(field);
		// 如何不添加此字段，查询时指定此字段排序报错
		field = new SortedDocValuesField(name, new BytesRef(value));
		document.add(field);
		//field = new StoredField(name, new BytesRef(value));//主要是为了解决数字值存储问题，字符串或文本型数据不能使用此字段。
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
			System.out.println(indexSearcher.doc(scoreDoc.doc).getField("stringValue").stringValue());
		}
	}

	 

	/**
	 * 测试longField排序
	 */
	@Test
	public void testLongFieldSort() {
		Document document = new Document();
		document.add(new LongPoint("longValue", 50L));
		document.add(new NumericDocValuesField("longValue", 50L));
		document.add(new StoredField("longValue", 50L));
		//document.add(new StoredField("longValue", 90L));//可以存储多个
		Document document1 = new Document();
		document1.add(new LongPoint("longValue", 80L));
		document1.add(new NumericDocValuesField("longValue", 80L));
		document1.add(new StoredField("longValue", 80l));
		IndexWriter writer = null;
		Directory directory = new RAMDirectory();
		try {
			
			writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
			writer.addDocument(document);
			writer.addDocument(document1);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.commit();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			 
			IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
			// 构建排序字段
			SortField[] sortField = new SortField[1];
			sortField[0] = new SortField("longValue", SortField.Type.LONG, true);
			Sort sort = new Sort(sortField);
			// 查询所有结果
			Query query = new MatchAllDocsQuery();
			TopFieldDocs docs = searcher.search(query, 2, sort);
			ScoreDoc[] scores = docs.scoreDocs;
			// 遍历结果
			for (ScoreDoc scoreDoc : scores) {
				 System.out.println(searcher.doc(scoreDoc.doc));;
				Document doc = searcher.doc(scoreDoc.doc);
				 
				System.out.println(doc.getField("longValue").numericValue());
			}
			// searcher.search(query, results);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
