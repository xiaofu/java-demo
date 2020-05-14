package com.github.xiaofu.lucene.demo.analysis.queryparser;

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

import org.apache.lucene.search.Query;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.junit.Test;

// From chapter 4
public class AnalysisParalysisTest {
	
	@Test
	public void testAnalyzer() throws Exception {
    Analyzer analyzer = new ClassicAnalyzer();
    String queryString = "category:/philosophy/eastern";

    Query query = new QueryParser(
                                  "contents",
                                  analyzer).parse(queryString);
    assertEquals("path got split, yikes!",
                 "category:\"philosophy eastern\"",
                 query.toString("contents"));
    Map<String, Analyzer> analyzerPerField = new HashMap<>();
	analyzerPerField.put("category", new WhitespaceAnalyzer());
    PerFieldAnalyzerWrapper perFieldAnalyzer =
                            new PerFieldAnalyzerWrapper(analyzer,analyzerPerField);
    
     
    query = new QueryParser(
                            "contents",
                            perFieldAnalyzer).parse(queryString);
    assertEquals("leave category field alone",
                 "category:/philosophy/eastern",
                 query.toString("contents"));
  }
}
