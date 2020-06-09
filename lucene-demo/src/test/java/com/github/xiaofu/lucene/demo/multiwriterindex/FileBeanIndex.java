package com.github.xiaofu.lucene.demo.multiwriterindex;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;

public class FileBeanIndex extends BaseIndex<FileBean> {

	public FileBeanIndex(IndexWriter writer, CountDownLatch countDownLatch1, CountDownLatch countDownLatch2,
			List<FileBean> list) {
		super(writer, countDownLatch1, countDownLatch2, list);
	}

	public FileBeanIndex(String parentIndexPath, int subIndex, CountDownLatch countDownLatch1,
			CountDownLatch countDownLatch2, List<FileBean> list) {
		super(parentIndexPath, subIndex, countDownLatch1, countDownLatch2, list);
	}

	@Override
	public void indexDoc(IndexWriter writer, FileBean t) throws Exception {
		Document doc = new Document();
		System.out.println(t.getPath());
		doc.add(new StringField("path", t.getPath(), Store.YES));
		doc.add(new LongPoint("modified", t.getModified()));
		doc.add(new StoredField("modified", t.getModified()));
		doc.add(new TextField("content", t.getContent(), Store.YES));
		if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
			writer.addDocument(doc);
		} else {
			writer.updateDocument(new Term("path", t.getPath()), doc);
		}
	}

}
