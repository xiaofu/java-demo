package com.github.xiaofu.lucene.demo;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexCommit;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.NoDeletionPolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {

	public static void main(String[] args) throws Exception {
		 
		Path indexDir = Paths.get("firstTest");
		String dataDir = "E:\\open-source-projects\\big-data\\lucene\\lucene-8.4.1";
		long start = System.currentTimeMillis();
		Indexer indexer = new Indexer(indexDir);
		int numIndexed=0;
		try {
			for (int i = 0; i < 1; i++) {
				numIndexed = indexer.index(dataDir, new TextFilesFilter());
				//Thread.sleep(5*1000);
				System.out.println("commit:+"+indexer.commit());
			}
			
		} finally {
			indexer.close();
		}
		for (IndexCommit commit : DirectoryReader.listCommits( FSDirectory.open(indexDir))) {
			System.out.println(commit.getSegmentCount());
			System.out.println(commit.getGeneration());
			for (String path : commit.getFileNames()) {
				System.out.println(path);
			}
		}  
		long end = System.currentTimeMillis();
		System.out.println("Indexing " + numIndexed + " files took " + (end - start) + " milliseconds");
	}

	private   IndexWriter writer;

	public Indexer(Path indexDir) throws IOException {
		Directory dir = FSDirectory.open(indexDir);
		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		config.setOpenMode(OpenMode.CREATE);
		//config.setIndexDeletionPolicy(NoDeletionPolicy.INSTANCE);//不删除旧的提交
		writer = new IndexWriter(dir, config);
		
		 
	}
	
	public long commit() throws IOException
	{
		return writer.commit();
	}

	public void close() throws IOException {
		writer.close();
	}

	public int index(String dataDir, FileFilter filter) throws Exception {
		File[] files = new File(dataDir).listFiles();
		for (File f : files) {
			if (!f.isDirectory() && !f.isHidden() && f.exists() && f.canRead()
					&& (filter == null || filter.accept(f))) {
				indexFile(f);
			}
		}
		 
		return writer.numRamDocs();
	}

	private static class TextFilesFilter implements FileFilter {
		public boolean accept(File path) {
			return path.getName().toLowerCase().endsWith(".txt");
		}
	}

	protected Document getDocument(File f) throws Exception {
		Document doc = new Document();
		doc.add(new Field("contents", new FileReader(f), TextField.TYPE_NOT_STORED));
		doc.add(new Field("filename", f.getName(),StoredField.TYPE));
		doc.add(new Field("fullpath", f.getCanonicalPath(), StoredField.TYPE));
		return doc;
	}

	private void indexFile(File f) throws Exception {
		System.out.println("Indexing " + f.getCanonicalPath());
		Document doc = getDocument(f);
		writer.addDocument(doc);
		 
	}
}
