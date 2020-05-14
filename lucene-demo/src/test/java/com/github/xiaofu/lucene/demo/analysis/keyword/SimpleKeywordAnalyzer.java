package com.github.xiaofu.lucene.demo.analysis.keyword;

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
import org.apache.lucene.analysis.util.CharTokenizer;

// From chapter 4

/**
 * CharTokenizer limits token width to 255 characters, though.
 * This implementation assumes keywords are 255 in length or less.
 */

public class SimpleKeywordAnalyzer extends Analyzer {

  

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		 
		return new TokenStreamComponents(new CharTokenizer() {
   
			@Override
			protected boolean isTokenChar(int c) {
				 
				return true;
			}
        });
	}

}
