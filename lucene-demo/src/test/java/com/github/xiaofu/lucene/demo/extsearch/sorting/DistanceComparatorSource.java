package com.github.xiaofu.lucene.demo.extsearch.sorting;

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

import org.apache.lucene.search.FieldComparatorSource;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.FieldComparator.NumericComparator;
import java.io.IOException;

// From chapter 6
public class DistanceComparatorSource extends FieldComparatorSource { // #1
	private int x;
	private int y;

	public DistanceComparatorSource(int x, int y) { // #2
		this.x = x;
		this.y = y;
	}

	public FieldComparator<Float> newComparator(String fieldName, // #3
			int numHits, int sortPos, // #3
			boolean reversed) // #3
	{ // #3
		try {
			return new DistanceScoreDocLookupComparator(fieldName, numHits, null);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private class DistanceScoreDocLookupComparator // #4
			extends NumericComparator<Float> {
		// private int xDoc, yDoc; // #5
		private float[] values; // #6
		private float bottom; // #7
		private float top;
		String fieldName;
		NumericDocValues xReaderValues;
		NumericDocValues yReaderValues;

		public DistanceScoreDocLookupComparator(String fieldName, int numHits, Float missingValue) throws IOException {
			super(fieldName, missingValue != null ? missingValue : 0);
			values = new float[numHits];
			this.fieldName = fieldName;
		}

		@Override
		protected void doSetNextReader(LeafReaderContext context) throws IOException {
			xReaderValues = getNumericDocValues(context, "x");
			yReaderValues = getNumericDocValues(context, "y");
		}

		private float getXValueForDoc(int doc) throws IOException {
			if (xReaderValues.advanceExact(doc)) {
				return xReaderValues.longValue();
			} else {
				return missingValue;
			}
		}

		private float getYValueForDoc(int doc) throws IOException {
			if (yReaderValues.advanceExact(doc)) {
				return yReaderValues.longValue();
			} else {
				return missingValue;
			}
		}

		private float getDistance(int doc) throws IOException { // #9
			float deltax = getXValueForDoc(doc) - x; // #9
			float deltay = getYValueForDoc(doc) - y; // #9
			return (float) Math.sqrt(deltax * deltax + deltay * deltay); // #9
		}

		@Override
		public int compare(int slot1, int slot2) { // #10
			return Float.compare(values[slot1], values[slot2]);

		}

		@Override
		public void setBottom(int slot) { // #11
			bottom = values[slot];
		}

		@Override
		public int compareBottom(int doc) throws IOException { // #12
			float docDistance = getDistance(doc);
			return Float.compare(bottom, docDistance);
		}

		@Override
		public void copy(int slot, int doc) throws IOException { // #13
			values[slot] = getDistance(doc); // #13
		}

		@Override
		public int compareTop(int doc) throws IOException {
			float docDistance = getDistance(doc);
			return Float.compare(top, docDistance);
		}

		@Override
		public void setTopValue(Float value) {
			top = value;
		}

		@Override
		public Float value(int slot) {

			return values[slot];
		}
	}

	public String toString() {
		return "Distance from (" + x + "," + y + ")";
	}
}

/*
 * #1 Extend FieldComparatorSource #2 Give constructor base location #3 Create
 * comparator #4 FieldComparator implementation #5 Array of x, y per document #6
 * Distances for documents in the queue #7 Worst distance in the queue #8 Get x,
 * y values from field cache #9 Compute distance for one document #10 Compare
 * two docs in the top N #11 Record worst scoring doc in the top N #12 Compare
 * new doc to worst scoring doc #13 Insert new doc into top N #14 Extract value
 * from top N
 */
