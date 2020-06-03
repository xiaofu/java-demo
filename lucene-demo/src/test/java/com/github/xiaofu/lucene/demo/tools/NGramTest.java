
package com.github.xiaofu.lucene.demo.tools;

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

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;

import com.github.xiaofu.lucene.demo.analysis.AnalyzerUtils;

import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.junit.Test;

// From chapter 8
public class NGramTest {

	private static class NGramAnalyzer extends Analyzer {

		@Override
		protected TokenStreamComponents createComponents(String fieldName) {
			Tokenizer tok = new KeywordTokenizer();
			return new TokenStreamComponents(tok, new NGramTokenFilter(tok,2,4,true));
		}
	}

	private static class FrontEdgeNGramAnalyzer extends Analyzer {

		@Override
		protected TokenStreamComponents createComponents(String fieldName) {
			Tokenizer tok = new KeywordTokenizer();
			return new TokenStreamComponents(tok, new EdgeNGramTokenFilter(tok, 1, 4, true));
		}
	}

	private static class BackEdgeNGramAnalyzer extends Analyzer {

		@Override
		protected TokenStreamComponents createComponents(String fieldName) {
			Tokenizer tok = new KeywordTokenizer();
			return new TokenStreamComponents(tok, new EdgeNGramTokenFilter(tok, 1, 4, false));

		}
	}
	
	private static class ShingleAnalyzer extends Analyzer {

		@Override
		protected TokenStreamComponents createComponents(String fieldName) {
			Tokenizer tok = new WhitespaceTokenizer();
			return new TokenStreamComponents(tok, new ShingleFilter(tok,2,4));

		}
	}

	@Test
	public void testNGramTokenFilter24() throws IOException {
		AnalyzerUtils.displayTokensWithPositions(new NGramAnalyzer(), "lettuce");
	}
	
	@Test
	public void testEdgeNGramTokenFilterFront() throws IOException {
		AnalyzerUtils.displayTokensWithPositions(new FrontEdgeNGramAnalyzer(), "lettuce");
	}
	
	@Test
	public void testEdgeNGramTokenFilterBack() throws IOException {
		AnalyzerUtils.displayTokensWithPositions(new BackEdgeNGramAnalyzer(), "lettuce");
	}
	
	@Test
	public void testShingleAnalyzer() throws IOException {
		AnalyzerUtils.displayTokensWithPositions(new ShingleAnalyzer(), "please divide this sentence into shingles");
	}
}
