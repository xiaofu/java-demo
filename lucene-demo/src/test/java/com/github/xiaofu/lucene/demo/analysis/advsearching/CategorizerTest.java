package com.github.xiaofu.lucene.demo.analysis.advsearching;

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
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;
import org.junit.Before;
import org.junit.Test;

import com.github.xiaofu.lucene.demo.TestUtil;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

// From chapter 5
public class CategorizerTest {
	Map<String,Map<String,Integer>> categoryMap;

	@Before
	public void setUp() throws Exception {
		categoryMap = new TreeMap<String,Map<String,Integer>>();

		buildCategoryVectors();
		dumpCategoryVectors();
	}

	@Test
	public void testCategorization() throws Exception {
		assertEquals("/technology/computers/programming/methodology", getCategory("extreme agile methodology"));
		assertEquals("/education/pedagogy", getCategory("montessori education philosophy"));
	}

	private void dumpCategoryVectors() {
		Iterator categoryIterator = categoryMap.keySet().iterator();
		while (categoryIterator.hasNext()) {
			String category = (String) categoryIterator.next();
			System.out.println("Category " + category);

			Map<String,Integer> vectorMap =  categoryMap.get(category);
			Iterator vectorIterator = vectorMap.keySet().iterator();
			while (vectorIterator.hasNext()) {
				String term = (String) vectorIterator.next();
				System.out.println("    " + term + " = " + vectorMap.get(term));
			}
		}
	}

	private void buildCategoryVectors() throws IOException {
		IndexReader reader = DirectoryReader.open(TestUtil.getBookIndexDirectory());

		int maxDoc = reader.maxDoc();

		for (int i = 0; i < maxDoc; i++) {

			Document doc = reader.document(i);
			String category = doc.get("category");
			Map<String,Integer> vectorMap = categoryMap.get(category);
			if (vectorMap == null) {
				vectorMap = new TreeMap<String,Integer>();
				categoryMap.put(category, vectorMap);
			}

			Terms termFreqVector = reader.getTermVector(i, "subject");

			addTermFreqToMap(vectorMap, termFreqVector, i);
		}

	}

	private void addTermFreqToMap(Map<String,Integer> vectorMap, Terms termFreqVector, int docId) throws IOException {
		TermsEnum terms = termFreqVector.iterator();
		BytesRef term = terms.next();
		while (term != null) {
			System.out.println("term:"+term.utf8ToString());
			System.out.println("docFreq:"+terms.docFreq());//一定返回1，因为指定文档的词向量
			System.out.println("totalTermFreq:"+terms.totalTermFreq());
			int freq=(int)terms.totalTermFreq();//由于词向量是属于某个文档的某个字段的，所以这里的totalTermFreq方法返回的一定是当前文档的词向量
		 
			if (vectorMap.containsKey(term.utf8ToString())) {
				Integer value =vectorMap.get(term.utf8ToString());
				vectorMap.put(term.utf8ToString(), new Integer(value.intValue() + freq));
			} else {
				vectorMap.put(term.utf8ToString(), new Integer(freq));
			}
			term = terms.next();
		}

	}

	private String getCategory(String subject) {
		String[] words = subject.split(" ");

		Iterator categoryIterator = categoryMap.keySet().iterator();
		double bestAngle = Double.MAX_VALUE;
		String bestCategory = null;

		while (categoryIterator.hasNext()) {
			String category = (String) categoryIterator.next();
//      System.out.println(category);

			double angle = computeAngle(words, category);
//      System.out.println(" -> angle = " + angle + " (" + Math.toDegrees(angle) + ")");
			if (angle < bestAngle) {
				bestAngle = angle;
				bestCategory = category;
			}
		}

		return bestCategory;
	}

	private double computeAngle(String[] words, String category) {
		Map vectorMap = (Map) categoryMap.get(category);

		int dotProduct = 0;
		int sumOfSquares = 0;
		for (String word : words) {
			int categoryWordFreq = 0;

			if (vectorMap.containsKey(word)) {
				categoryWordFreq = ((Integer) vectorMap.get(word)).intValue();
			}

			dotProduct += categoryWordFreq; // #1
			sumOfSquares += categoryWordFreq * categoryWordFreq;
		}

		double denominator;
		if (sumOfSquares == words.length) {
			denominator = sumOfSquares; // #2
		} else {
			denominator = Math.sqrt(sumOfSquares) * Math.sqrt(words.length);
		}

		double ratio = dotProduct / denominator;

		return Math.acos(ratio);
	}
	/*
	 * #1 Assume each word has frequency 1 #2 Shortcut to prevent precision issue
	 */
}
