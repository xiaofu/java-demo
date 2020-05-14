package com.github.xiaofu.lucene.demo.analysis.synonym;

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
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;

// From chapter 4
public class SynonymAnalyzer extends Analyzer {
	private SynonymEngine engine;

	public SynonymAnalyzer(SynonymEngine engine) {
		this.engine = engine;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		StandardTokenizer tokenizer = new StandardTokenizer();
		return new TokenStreamComponents(tokenizer, new SynonymFilter(
				new StopFilter(new LowerCaseFilter(tokenizer), EnglishAnalyzer.ENGLISH_STOP_WORDS_SET), engine));
	}
}
