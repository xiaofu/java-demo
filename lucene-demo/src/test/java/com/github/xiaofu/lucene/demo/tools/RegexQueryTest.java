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

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.TopDocs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.junit.Test;

import com.github.xiaofu.lucene.demo.TestUtil;
 

// From chapter 8
public class RegexQueryTest   {

  @Test
  public void testRegexQuery() throws Exception {
    Directory directory = TestUtil.getBookIndexDirectory();
    IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
    Query q = new RegexpQuery(new Term("title", ".*st.*"));
    TopDocs hits = searcher.search(q, 10);
    assertEquals(2, hits.totalHits.value);
    assertTrue(TestUtil.hitsIncludeTitle(searcher, hits,
                                         "Tapestry in Action"));
    assertTrue(TestUtil.hitsIncludeTitle(searcher, hits,
                                         "Mindstorms: Children, Computers, And Powerful Ideas"));
    directory.close();
  }
}
