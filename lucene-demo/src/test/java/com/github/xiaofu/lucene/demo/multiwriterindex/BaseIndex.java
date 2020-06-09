package com.github.xiaofu.lucene.demo.multiwriterindex;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.lucene.index.IndexWriter;

public abstract class BaseIndex<T> implements Runnable {
	/**
	 * 父级索引路径
	 */
	private String parentIndexPath;
	/**
	 * 索引编写器
	 */
	private IndexWriter writer;
	private int subIndex;
	/**
	 * 主线程
	 */
	private final CountDownLatch countDownLatch1;
	/**
	 * 工作线程
	 */
	private final CountDownLatch countDownLatch2;
	/**
	 * 对象列表
	 */
	private List<T> list;

	public BaseIndex(IndexWriter writer, CountDownLatch countDownLatch1, CountDownLatch countDownLatch2, List<T> list) {
		super();
		this.writer = writer;
		this.countDownLatch1 = countDownLatch1;
		this.countDownLatch2 = countDownLatch2;
		this.list = list;
	}

	public BaseIndex(String parentIndexPath, int subIndex, CountDownLatch countDownLatch1,
			CountDownLatch countDownLatch2, List<T> list) {
		super();
		this.parentIndexPath = parentIndexPath;
		this.subIndex = subIndex;
		try {
			// 多目录索引创建
			File file = new File(parentIndexPath + "/index" + subIndex);
			if (!file.exists()) {
				file.mkdir();
			}
			this.writer = IndexUtil.getIndexWriter(parentIndexPath + "/index" + subIndex, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.subIndex = subIndex;
		this.countDownLatch1 = countDownLatch1;
		this.countDownLatch2 = countDownLatch2;
		this.list = list;
	}

	public BaseIndex(String path, CountDownLatch countDownLatch1, CountDownLatch countDownLatch2, List<T> list) {
		super();
		try {
			// 单目录索引创建
			File file = new File(path);
			if (!file.exists()) {
				file.mkdir();
			}
			this.writer = IndexUtil.getIndexWriter(path, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.countDownLatch1 = countDownLatch1;
		this.countDownLatch2 = countDownLatch2;
		this.list = list;
	}

	/**
	 * 创建索引
	 * 
	 * @param writer
	 * @param carSource
	 * @param create
	 * @throws IOException
	 * @throws ParseException
	 */
	public abstract void indexDoc(IndexWriter writer, T t) throws Exception;

	/**
	 * 批量索引创建
	 * 
	 * @param writer
	 * @param t
	 * @throws Exception
	 */
	public void indexDocs(IndexWriter writer, List<T> t) throws Exception {
		for (T t2 : t) {
			indexDoc(writer, t2);
		}
	}

	@Override
	public void run() {
		try {
			countDownLatch1.await();
			System.out.println(writer);
			indexDocs(writer, list);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			countDownLatch2.countDown();
			try {
				writer.commit();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
