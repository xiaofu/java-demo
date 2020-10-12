package com.github.xiaofu.lucene.demo.analysis.i18n;

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

 

import static org.junit.Assert.assertEquals;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.junit.Test;

import com.github.xiaofu.lucene.demo.TestUtil;

// From chapter 4
public class ChineseTest   {
 
  @Test
  public void testChinese() throws Exception {
    Directory dir = TestUtil.getBookIndexDirectory();
    IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(dir));
    Query query = new TermQuery(new Term("contents", "道"));
    assertEquals("tao", 1, TestUtil.hitCount(searcher, query));
  }
}