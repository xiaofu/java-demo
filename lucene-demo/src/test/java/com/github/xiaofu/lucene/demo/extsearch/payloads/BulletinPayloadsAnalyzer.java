package com.github.xiaofu.lucene.demo.extsearch.payloads;

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
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;

// From chapter 6
public class BulletinPayloadsAnalyzer extends Analyzer {
	private boolean isBulletin;
	private float boost;

	BulletinPayloadsAnalyzer(float boost) {
		super(new ReuseStrategy() {

			@Override
			public TokenStreamComponents getReusableComponents(Analyzer analyzer, String fieldName) {
				return null;
			}

			@Override
			public void setReusableComponents(Analyzer analyzer, String fieldName, TokenStreamComponents components) {
				 
				
			}});
		this.boost = boost;
	}

	void setIsBulletin(boolean v) {
		isBulletin = v;
	}
	
	
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		  ClassicTokenizer src = new ClassicTokenizer();
		src.setMaxTokenLength(ClassicAnalyzer.DEFAULT_MAX_TOKEN_LENGTH);
		TokenStream tok = new ClassicFilter(src);
		tok = new LowerCaseFilter(tok);
		tok = new StopFilter(tok, ClassicAnalyzer.STOP_WORDS_SET);
		BulletinPayloadsFilter stream = new BulletinPayloadsFilter(tok, boost);
		stream.setIsBulletin(isBulletin);
		return new TokenStreamComponents(src, stream);

	}
}
