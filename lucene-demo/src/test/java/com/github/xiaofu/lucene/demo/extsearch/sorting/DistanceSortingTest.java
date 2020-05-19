package com.github.xiaofu.lucene.demo.extsearch.sorting;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;

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
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FieldDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

// From chapter 6
public class DistanceSortingTest  {
  private RAMDirectory directory;
  private IndexSearcher searcher;
  private Query query;

  @Before
  public void setUp() throws Exception {
    directory = new RAMDirectory();
    IndexWriter writer = new IndexWriter(directory,new IndexWriterConfig(new WhitespaceAnalyzer()));
    addPoint(writer, "El Charro", "restaurant", 1, 2);
    addPoint(writer, "Cafe Poca Cosa", "restaurant", 5, 9);
    addPoint(writer, "Los Betos", "restaurant", 9, 6);
    addPoint(writer, "Nico's Taco Shop", "restaurant", 3, 8);
    writer.close();
    searcher = new IndexSearcher(DirectoryReader.open(directory));

    query = new TermQuery(new Term("type", "restaurant"));
  }

  private void addPoint(IndexWriter writer,
                        String name, String type, int x, int y)
      throws IOException {
    Document doc = new Document();
    doc.add(new StringField("name", name, Field.Store.YES));
    doc.add(new StringField("type", type, Field.Store.YES));
    doc.add(new IntPoint("x", x));
    doc.add(new StoredField("x", x));
    doc.add(new NumericDocValuesField("x", x));
    doc.add(new IntPoint("y", y));
    doc.add(new NumericDocValuesField("y", y));
    doc.add(new StoredField("y", y));
    writer.addDocument(doc);
  }

  @Test
  public void testNearestRestaurantToHome() throws Exception {
    Sort sort = new Sort(new SortField("unused",
        new DistanceComparatorSource(0, 0)));

    TopDocs hits = searcher.search(query, 10, sort);

    assertEquals("closest",
                 "El Charro", searcher.doc(hits.scoreDocs[0].doc).get("name"));
    assertEquals("furthest",
                 "Los Betos", searcher.doc(hits.scoreDocs[3].doc).get("name"));
  }
  
  @Test
  public void testNeareastRestaurantToWork() throws Exception {
    Sort sort = new Sort(new SortField("unused",
        new DistanceComparatorSource(10, 10)));

    TopFieldDocs docs = searcher.search(query, 3, sort);  // #1

    assertEquals(4, docs.totalHits.value);              // #2
    assertEquals(3, docs.scoreDocs.length);       // #3

    FieldDoc fieldDoc = (FieldDoc) docs.scoreDocs[0];     // #4

    assertEquals("(10,10) -> (9,6) = sqrt(17)",
        new Float(Math.sqrt(17)),
        fieldDoc.fields[0]);                         // #5

    Document document = searcher.doc(fieldDoc.doc);  // #6
    assertEquals("Los Betos", document.get("name"));

    dumpDocs(sort, docs);
  }
  /*
#1 Specify maximum hits returned
#2 Total number of hits
#3 Return total number of documents
#4 Get sorting values
#5 Give value of first computation
#6 Get Document
  */

  private void dumpDocs(Sort sort, TopFieldDocs docs)
      throws IOException {
    System.out.println("Sorted by: " + sort);
    ScoreDoc[] scoreDocs = docs.scoreDocs;
    for (int i = 0; i < scoreDocs.length; i++) {
      FieldDoc fieldDoc = (FieldDoc) scoreDocs[i];
      Float distance = (Float) fieldDoc.fields[0];
      Document doc = searcher.doc(fieldDoc.doc);
      System.out.println("   " + doc.get("name") +
                         " @ (" + doc.get("x") + "," + doc.get("y") + ") -> " + distance);
    }
  }
}
