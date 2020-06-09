package com.github.xiaofu.lucene.demo.admin;

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

import org.apache.lucene.index.*;
import org.apache.lucene.store.*;
import org.junit.Test;
import org.apache.lucene.analysis.*;
import org.apache.lucene.search.*;

import java.nio.file.Paths;
import java.util.Collection;

// From chapter 11

public class Fragments {
  
  @Test
  public void test() throws Exception {
    Directory dir = FSDirectory.open(Paths.get("tempIndex"));
    Directory snapshotIndex = FSDirectory.open(Paths.get("snapshotIndex"));
    // START
    IndexDeletionPolicy policy = new KeepOnlyLastCommitDeletionPolicy();
    PersistentSnapshotDeletionPolicy snapshotter = new PersistentSnapshotDeletionPolicy(policy,snapshotIndex);
    IndexWriterConfig config=new IndexWriterConfig();
    config.setIndexDeletionPolicy(snapshotter);
    IndexWriter writer = new IndexWriter(dir,config);
    // END
    IndexCommit commit=null;
    try {
        commit =  snapshotter.snapshot();
        System.out.println(snapshotter.getLastSaveFile());
      //Collection<String> fileNames = commit.getFileNames();
      /*<iterate over & copy files from fileNames>*/
    } finally {
     // snapshotter.release(commit);
    }
  }
}