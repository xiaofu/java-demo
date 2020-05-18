/**
 * 
 */
package com.github.xiaofu.lucene.demo;

import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;

/**
 * @author fulaihua
 *
 */
public class TextFieldWithTermVector extends Field {

	  /** Indexed, tokenized, not stored. */
	  public static final FieldType TYPE_NOT_STORED = new FieldType();

	  /** Indexed, tokenized, stored. */
	  public static final FieldType TYPE_STORED = new FieldType();

	  static {
	    TYPE_NOT_STORED.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
	    TYPE_NOT_STORED.setTokenized(true);
	    TYPE_NOT_STORED.setStoreTermVectors(true);
	    TYPE_NOT_STORED.setStoreTermVectorOffsets(true);
	    TYPE_NOT_STORED.setStoreTermVectorPositions(true);
	    TYPE_NOT_STORED.freeze();

	    TYPE_STORED.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
	    TYPE_STORED.setTokenized(true);
	    TYPE_STORED.setStored(true);
	    TYPE_STORED.setStoreTermVectors(true);
	    TYPE_STORED.setStoreTermVectorOffsets(true);
	    TYPE_STORED.setStoreTermVectorPositions(true);
	    TYPE_STORED.freeze();
	  }

	  // TODO: add sugar for term vectors...?

	  /** Creates a new un-stored TextField with Reader value. 
	   * @param name field name
	   * @param reader reader value
	   * @throws IllegalArgumentException if the field name is null
	   * @throws NullPointerException if the reader is null
	   */
	  public TextFieldWithTermVector(String name, Reader reader) {
	    super(name, reader, TYPE_NOT_STORED);
	  }

	  /** Creates a new TextField with String value. 
	   * @param name field name
	   * @param value string value
	   * @param store Store.YES if the content should also be stored
	   * @throws IllegalArgumentException if the field name or value is null.
	   */
	  public TextFieldWithTermVector(String name, String value, Store store) {
	    super(name, value, store == Store.YES ? TYPE_STORED : TYPE_NOT_STORED);
	  }
	  
	  /** Creates a new un-stored TextField with TokenStream value. 
	   * @param name field name
	   * @param stream TokenStream value
	   * @throws IllegalArgumentException if the field name is null.
	   * @throws NullPointerException if the tokenStream is null
	   */
	  public TextFieldWithTermVector(String name, TokenStream stream) {
	    super(name, stream, TYPE_NOT_STORED);
	  }

}
