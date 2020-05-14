package com.github.xiaofu.lucene.demo.analysis.positional;

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
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;

 

// From chapter 4
public class PositionalPorterStopAnalyzer extends Analyzer {
	private CharArraySet stopWords;

	public PositionalPorterStopAnalyzer() {
		this(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);
	}

	public PositionalPorterStopAnalyzer(CharArraySet stopWords) {
		this.stopWords = stopWords;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer tokenizer = new LetterTokenizer();
		PorterStemFilter stopFilter = new PorterStemFilter(new StopFilter(new LowerCaseFilter(tokenizer), stopWords));
		return new TokenStreamComponents(tokenizer, stopFilter);
	}
}
