package com.github.xiaofu.lucene.demo.analysis.advsearching;

import static org.junit.Assert.assertEquals;

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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;

import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

// From chapter 5
public class MultiSearcherTest {

	Directory aTOmDirectory;
	Directory nTOzDirectory;

	@Before
	public void setUp() throws Exception {
		String[] animals = { "aardvark", "beaver", "coati", "dog", "elephant", "frog", "gila monster", "horse",
				"iguana", "javelina", "kangaroo", "lemur", "moose", "nematode", "orca", "python", "quokka", "rat",
				"scorpion", "tarantula", "uromastyx", "vicuna", "walrus", "xiphias", "yak", "zebra" };

		Analyzer analyzer = new WhitespaceAnalyzer();

		aTOmDirectory = new RAMDirectory(); // #1
		nTOzDirectory = new RAMDirectory(); // #1

		IndexWriter aTOmWriter = new IndexWriter(aTOmDirectory, new IndexWriterConfig(analyzer));
		IndexWriter nTOzWriter = new IndexWriter(nTOzDirectory, new IndexWriterConfig(analyzer));

		for (int i = animals.length - 1; i >= 0; i--) {
			Document doc = new Document();
			String animal = animals[i];
			doc.add(new StringField("animal", animal, Field.Store.YES));
			if (animal.charAt(0) < 'n') {
				aTOmWriter.addDocument(doc); // #2
			} else {
				nTOzWriter.addDocument(doc); // #2
			}
		}

		aTOmWriter.close();
		nTOzWriter.close();

	}

	@Test
	public void testMulti() throws Exception {
		IndexReader reader1 = DirectoryReader.open(aTOmDirectory);
		IndexReader reader2 = DirectoryReader.open(nTOzDirectory);
		MultiReader reader = new MultiReader(reader1, reader2);
		IndexSearcher searcher = new IndexSearcher(reader);

		TermRangeQuery query = new TermRangeQuery("animal", new BytesRef("h"), new BytesRef("t"), true, true);// #3

		TopDocs hits = searcher.search(query, 10);
		assertEquals("tarantula not included", 12, hits.totalHits.value);
	}

	/*
	 * #1 Create two directories #2 Index halves of the alphabet #3 Search both
	 * indexes
	 */
}
