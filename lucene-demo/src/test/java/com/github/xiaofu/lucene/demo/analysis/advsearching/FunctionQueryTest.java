package com.github.xiaofu.lucene.demo.analysis.advsearching;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.function.LongToDoubleFunction;

import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleDocValuesField;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.queries.function.FunctionQuery;
import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.apache.lucene.queries.function.valuesource.DoubleFieldSource;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.DoubleValues;
import org.apache.lucene.search.DoubleValuesSource;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import com.github.xiaofu.lucene.demo.TestUtil;

 
// From chapter 5
public class FunctionQueryTest {

	IndexSearcher s;
	IndexWriter w;

	private void addDoc(double score, String content) throws Exception {
		Document doc = new Document();
		doc.add(new DoublePoint("score", score));
		doc.add(new DoubleDocValuesField("score", score));// 必须添加文档值字段，否则ValueSource无法获取值打分。
		doc.add(new TextField("content", content, Store.NO));
		w.addDocument(doc);
	}

	@Before
	public void setUp() throws Exception {
		Directory dir = new RAMDirectory();

		w = new IndexWriter(dir, new IndexWriterConfig());
		addDoc(7.0, "this hat is green");
		addDoc(42.0, "this hat is blue");
		w.close();

		s = new IndexSearcher(DirectoryReader.open(dir));
	}

	public void tearDown() throws Exception {

	}

	/**
	 * 直接使用字段的值来打分,此查询会匹配所有文档并进行打分。
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testFieldScoreQuery() throws Throwable {
		Query q = new FunctionQuery(new DoubleFieldSource("score"));
		TopDocs hits = s.search(q, 10);
		 
		assertEquals(2, hits.scoreDocs.length); // #1
		assertEquals(1, hits.scoreDocs[0].doc); // #2
		assertEquals(42, (int) hits.scoreDocs[0].score);
		assertEquals(0, hits.scoreDocs[1].doc);
		assertEquals(7, (int) hits.scoreDocs[1].score);

	}

	/*
	 * #1 All documents match #2 Doc 1 is first because its static score (42) is
	 * higher than doc 0's (7)
	 */
	@Test
	public void testCustomScoreQuery() throws Throwable {
		Query q = new QueryParser("content", new ClassicAnalyzer()).parse("the green hat");
		System.out.println(q.toString());
		// 直接使用文档的评分和指定字段的值相乘作为最终的评分
		// FunctionScoreQuery customQ = FunctionScoreQuery.boostByValue(q,
		// DoubleValuesSource.fromIntField("score")) ;
		// 直接使用指定字段值作为最终的评分，抛弃文档的评分
		FunctionScoreQuery customQ = new FunctionScoreQuery(q, DoubleValuesSource.fromDoubleField("score"));
		TopDocs hits = s.search(customQ, 10);
		assertEquals(2, hits.scoreDocs.length);
		assertEquals(1, hits.scoreDocs[0].doc); // #1
		assertEquals(0, hits.scoreDocs[1].doc);
		System.out.println("score:" + hits.scoreDocs[0].score);
		System.out.println("score:" + hits.scoreDocs[1].score);
	}

	/*
	 * #1 Even though document 0 is a better match to the original query, document 1
	 * gets a better score after multiplying in its score field
	 */

	private static class RecencyBoostingSource extends DoubleValuesSource {
		private double multiplier;
		private int today;
		private int maxDaysAgo;
		// String dayField;
		private static int MSEC_PER_DAY = 1000 * 3600 * 24;
		final String field;
		final LongToDoubleFunction decoder;

		public RecencyBoostingSource(double multiplier, int maxDaysAgo, String field, LongToDoubleFunction decoder) {
			today = (int) (new Date().getTime() / MSEC_PER_DAY);
			this.multiplier = multiplier;
			this.maxDaysAgo = maxDaysAgo;
			this.field = field;
			this.decoder = decoder;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			RecencyBoostingSource that = (RecencyBoostingSource) o;
			return Objects.equals(field, that.field) && Objects.equals(decoder, that.decoder);
		}

		@Override
		public String toString() {
			return "double(" + field + ")";
		}

		@Override
		public int hashCode() {
			return Objects.hash(field, decoder);
		}

		@Override
		public DoubleValues getValues(LeafReaderContext ctx, DoubleValues scores) throws IOException {
			final NumericDocValues values = DocValues.getNumeric(ctx.reader(), field);//#A
			return new DoubleValues() {
				@Override
				public double doubleValue() throws IOException {
					int daysAgo = today - (int) values.longValue();//#B
					if (daysAgo < maxDaysAgo) { // #C
						float boost = (float) (multiplier * // #D
						(maxDaysAgo - daysAgo) // #D
								/ maxDaysAgo); // #D
						return (scores.doubleValue() * (1.0 + boost));
					}
					return scores.doubleValue(); //#E
				}

				@Override
				public boolean advanceExact(int target) throws IOException {
					return values.advanceExact(target);
				}
			};
		}

		@Override
		public boolean needsScores() {
			return true;//true后打的分会高些？
		}

		@Override
		public boolean isCacheable(LeafReaderContext ctx) {
			return DocValues.isCacheable(ctx, field);
		}

		@Override
		public Explanation explain(LeafReaderContext ctx, int docId, Explanation scoreExplanation) throws IOException {
			DoubleValues values = getValues(ctx, null);
			if (values.advanceExact(docId))
				return Explanation.match(values.doubleValue(), this.toString());
			else
				return Explanation.noMatch(this.toString());
		}

		public DoubleValuesSource rewrite(IndexSearcher searcher) throws IOException {
			return this;
		}

	}

	/*
	 * #A Retrieve days from field cache 
	 * #B Compute elapsed days 
	 * #C Skip old books
	 * #D Compute simple linear boost 
	 * #E Return un-boosted score
	 */
	@Test
	public void testRecency() throws Throwable {
		Directory dir = TestUtil.getBookIndexDirectory();

		IndexSearcher s = new IndexSearcher(DirectoryReader.open(dir));
		// s.setDefaultFieldSortScoring(true, true);
	 
		QueryParser parser = new QueryParser("contents", new ClassicAnalyzer());
		Query q = parser.parse("java in action"); // #A
		System.out.println(q);
		FunctionScoreQuery customQ = new FunctionScoreQuery(q,
				new RecencyBoostingSource(2.0, 12 * 365, "pubmonthAsDay", Double::longBitsToDouble));
		Sort sort = new Sort(new SortField[] { SortField.FIELD_SCORE, new SortField("title2", SortField.Type.STRING) });
		TopDocs hits = s.search(customQ, 5, sort,true);

		for (int i = 0; i < hits.scoreDocs.length; i++) {
			Document doc = s.doc(hits.scoreDocs[i].doc);
			System.out.println((1 + i) + ": " + doc.get("title") + ": pubmonth=" + doc.get("pubmonth") + " score="
					+ hits.scoreDocs[i].score);
		}

		dir.close();
	}
	/*
	 * #A Parse query 
	 * #B Create recency boosting query
	 */
}
