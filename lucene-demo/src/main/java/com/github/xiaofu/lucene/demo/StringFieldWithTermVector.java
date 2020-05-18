/**
 * 
 */
package com.github.xiaofu.lucene.demo;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.util.BytesRef;

/**
 * @author fulaihua
 *
 */
public final class StringFieldWithTermVector extends Field {
	/** Indexed, not tokenized, omits norms, indexes
	   *  DOCS_ONLY, not stored. */
	  public static final FieldType TYPE_NOT_STORED = new FieldType();

	  /** Indexed, not tokenized, omits norms, indexes
	   *  DOCS_ONLY, stored */
	  public static final FieldType TYPE_STORED = new FieldType();

	  static {
	    TYPE_NOT_STORED.setOmitNorms(true);
	    TYPE_NOT_STORED.setIndexOptions(IndexOptions.DOCS);
	    TYPE_NOT_STORED.setTokenized(false);
	    TYPE_NOT_STORED.setStoreTermVectors(true);
	    TYPE_NOT_STORED.setStoreTermVectorOffsets(true);
	    TYPE_NOT_STORED.setStoreTermVectorPositions(true);
	    TYPE_NOT_STORED.freeze();

	    TYPE_STORED.setOmitNorms(true);
	    TYPE_STORED.setIndexOptions(IndexOptions.DOCS);
	    TYPE_STORED.setStored(true);
	    TYPE_STORED.setTokenized(false);
	    TYPE_STORED.setStoreTermVectors(true);
	    TYPE_STORED.setStoreTermVectorOffsets(true);
	    TYPE_STORED.setStoreTermVectorPositions(true);
	    TYPE_STORED.freeze();
	  }

	  /** Creates a new textual StringField, indexing the provided String value
	   *  as a single token.
	   *
	   *  @param name field name
	   *  @param value String value
	   *  @param stored Store.YES if the content should also be stored
	   *  @throws IllegalArgumentException if the field name or value is null.
	   */
	  public StringFieldWithTermVector(String name, String value, Store stored) {
	    super(name, value, stored == Store.YES ? TYPE_STORED : TYPE_NOT_STORED);
	  }

	  /** Creates a new binary StringField, indexing the provided binary (BytesRef)
	   *  value as a single token.
	   *
	   *  @param name field name
	   *  @param value BytesRef value.  The provided value is not cloned so
	   *         you must not change it until the document(s) holding it
	   *         have been indexed.
	   *  @param stored Store.YES if the content should also be stored
	   *  @throws IllegalArgumentException if the field name or value is null.
	   */
	  public StringFieldWithTermVector(String name, BytesRef value, Store stored) {
	    super(name, value, stored == Store.YES ? TYPE_STORED : TYPE_NOT_STORED);
	  }
}
