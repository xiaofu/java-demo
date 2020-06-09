/**
 * 
 */
package com.github.xiaofu.lucene.demo.grouping;

import java.io.IOException;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;

/**
 * @author fulaihua
 *
 */
public class IndexHelper {
    private Document document;
    private Directory directory;
    private IndexWriter indexWriter;
    public Directory getDirectory() {
        directory = (directory == null) ? new RAMDirectory() : directory;
        return directory;
    }
    private IndexWriterConfig getConfig() {
        return new IndexWriterConfig(new WhitespaceAnalyzer());
    }
    private IndexWriter getIndexWriter() {
        try {
            return new IndexWriter(getDirectory(), getConfig());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public IndexSearcher getIndexSearcher() throws IOException {
        return new IndexSearcher(DirectoryReader.open(getDirectory()));
    }
    public void createIndexForGroup(int ID, String author, String content) {
        indexWriter = getIndexWriter();
        document = new Document();
        //IntPoint默认是不存储的
        document.add(new IntPoint("ID", ID));
        //如果想要在搜索结果中获取ID的值，需要加上下面语句
        document.add(new StoredField("ID", ID));
        document.add(new StringField("author", author, Store.YES));
        //需要使用特定的field存储分组，需要排序及分组的话，要加上下面语句，注意默认SortedDocValuesField也是不存储的
        document.add(new SortedDocValuesField("author", new BytesRef(author)));
        document.add(new StringField("content", content,Store.YES));
        try {
            indexWriter.addDocument(document);
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
