
package com.github.xiaofu.lucene.demo.tools;

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

import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.spell.LevenshteinDistance;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.Directory;

// From chapter 8
public class SpellCheckerExample {

  public static void main(String[] args) throws IOException {

		/*
		 * if (args.length != 2) { System.out.
		 * println("Usage: java lia.tools.SpellCheckerTest SpellCheckerIndexDir wordToRespell"
		 * ); System.exit(1); }
		 */

    String spellCheckDir = "spellCheckDir";
    String wordToRespell = "unit";

    Directory dir = FSDirectory.open(Paths.get(spellCheckDir));
		/*
		 * if (!IndexReader.indexExists(dir)) {
		 * System.out.println("\nERROR: No spellchecker index at path \"" +
		 * spellCheckDir + "\"; please run CreateSpellCheckerIndex first\n");
		 * System.exit(1); }
		 */
    SpellChecker spell = new SpellChecker(dir);  //#A

    spell.setStringDistance(new LevenshteinDistance());  //#B
    //spell.setStringDistance(new JaroWinklerDistance());

    String[] suggestions = spell.suggestSimilar(wordToRespell, 5); //#C
    System.out.println(suggestions.length + " suggestions for '" + wordToRespell + "':");
    for (String suggestion : suggestions)
      System.out.println("  " + suggestion);
  }
}
/*
  #A Create SpellCheck from existing spell check index
  #B Sets the string distance metric used to rank the suggestions
  #C Generate respelled candidates
*/

