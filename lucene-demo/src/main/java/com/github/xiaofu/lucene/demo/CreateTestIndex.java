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

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.ParseException;

public class CreateTestIndex {

	public static Document getDocument(String rootDir, File file) throws IOException {
		Properties props = new Properties();
		props.load(new FileInputStream(file));

		Document doc = new Document();

		// category comes from relative path below the base directory
		String category = file.getParent().substring(rootDir.length()); // 1
		category = category.replace(File.separatorChar, '/'); // 1

		String isbn = props.getProperty("isbn"); // 2
		String title = props.getProperty("title"); // 2
		String author = props.getProperty("author"); // 2
		String url = props.getProperty("url"); // 2
		String subject = props.getProperty("subject"); // 2

		String pubmonth = props.getProperty("pubmonth"); // 2

		System.out.println(title + "\n" + author + "\n" + subject + "\n" + pubmonth + "\n" + category + "\n---------");

		doc.add(new StringField("isbn", // 3
				isbn, // 3
				Field.Store.YES)); // 3
		doc.add(new StringField("category", // 3
				category, // 3
				Field.Store.YES)); // 3
		doc.add(new SortedDocValuesField("category", // 3
				new BytesRef(category))); // 3
		doc.add(new TextFieldWithTermVector("title", // 3
				title, // 3
				Field.Store.YES)); // 3
		doc.add(new StringFieldWithTermVector("title2", // 3
				title.toLowerCase(), // 3
				Field.Store.YES)); // 3
		doc.add(new SortedDocValuesField("title2", // 3
				new BytesRef(title.toLowerCase()))); // 3
		// split multiple authors into unique field instances
		String[] authors = author.split(","); // 3
		for (String a : authors) { // 3
			doc.add(new StringFieldWithTermVector("author", // 3
					a, // 3
					Field.Store.YES)); // 3
		}

		doc.add(new StringField("url", // 3
				url, // 3
				Field.Store.YES)); // 3
		doc.add(new SortedDocValuesField("url", // 3
				new BytesRef(url))); // 3
		doc.add(new TextFieldWithTermVector("subject", // 3 //4
				subject, // 3 //4
				Field.Store.YES)); // 3 //4

		doc.add(new IntPoint("pubmonth", Integer.parseInt(pubmonth))); // 3
		doc.add(new NumericDocValuesField("pubmonth", Integer.parseInt(pubmonth))); // 3
		doc.add(new StoredField("pubmonth", Integer.parseInt(pubmonth))); // 3
		Date d; // 3
		try { // 3
			d = DateTools.stringToDate(pubmonth); // 3
		} catch (ParseException pe) { // 3
			throw new RuntimeException(pe); // 3
		} // 3
		doc.add(new IntPoint("pubmonthAsDay", (int) (d.getTime() / (1000 * 3600 * 24)))); // 3
		doc.add(new NumericDocValuesField("pubmonthAsDay", (int) (d.getTime() / (1000 * 3600 * 24)))); // 3
		doc.add(new StoredField("pubmonthAsDay", (int) (d.getTime() / (1000 * 3600 * 24)))); // 3
		for (String text : new String[] { title, subject, author, category }) { // 3 // 5
			doc.add(new TextFieldWithTermVector("contents", text, Store.NO)); // 3 // 5
		}

		return doc;
	}

	private static String aggregate(String[] strings) {
		StringBuilder buffer = new StringBuilder();

		for (int i = 0; i < strings.length; i++) {
			buffer.append(strings[i]);
			buffer.append(" ");
		}

		return buffer.toString();
	}

	private static void findFiles(List<File> result, File dir) {
		for (File file : dir.listFiles()) {
			if (file.getName().endsWith(".properties")) {
				result.add(file);
			} else if (file.isDirectory()) {
				findFiles(result, file);
			}
		}
	}

	private static class MyStandardAnalyzer extends StopwordAnalyzerBase { // 6

		@Override
		public int getPositionIncrementGap(String field) { // 6
			if (field.equals("contents")) { // 6
				return 100; // 6
			} else { // 6
				return 0; // 6
			}
		}

		@Override
		protected TokenStreamComponents createComponents(String fieldName) {
			final ClassicTokenizer src = new ClassicTokenizer();
		    src.setMaxTokenLength(ClassicAnalyzer.DEFAULT_MAX_TOKEN_LENGTH);
		    TokenStream tok = new ClassicFilter(src);
		    tok = new LowerCaseFilter(tok);
		    tok = new StopFilter(tok, stopwords);
		    return new TokenStreamComponents(r -> {
		      src.setMaxTokenLength(ClassicAnalyzer.DEFAULT_MAX_TOKEN_LENGTH);
		      src.setReader(r);
		    }, tok);
		}

		@Override
		protected TokenStream normalize(String fieldName, TokenStream in) {
			return new LowerCaseFilter(in);
		}

		public static void main(String[] args) throws IOException {
			 
			String dataDir = "E:\\programs-documents\\big-data\\lucene-solr\\lia2e\\data";
			String indexDir = "tempIndex";
			List<File> results = new ArrayList<File>();
			findFiles(results, new File(dataDir));
			System.out.println(results.size() + " books to index");
			Directory dir = FSDirectory.open(Paths.get(indexDir));
			IndexWriterConfig config = new IndexWriterConfig(new MyStandardAnalyzer());
			config.setOpenMode(OpenMode.CREATE);
			//config.setUseCompoundFile(false);
			IndexWriter w = new IndexWriter(dir, config);
			 
			for (File file : results) {
				Document doc = getDocument(dataDir, file);
				w.addDocument(doc);
			}
			w.close();
			dir.close();
		}
	}
}

/*
 * #1 Get category #2 Pull fields #3 Add fields to Document instance #4 Flag
 * subject field #5 Add catch-all contents field #6 Custom analyzer to override
 * multi-valued position increment
 */
