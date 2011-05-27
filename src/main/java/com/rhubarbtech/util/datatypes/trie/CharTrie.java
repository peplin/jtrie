package com.bueda.util.datatypes.trie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

import com.bueda.util.PrefixMatch;

/**
 * Trie Data Structure
 * 
 * Christopher Peplin (chris.peplin@rhubarbtech.com)
 * 
 * Copyright 2009 Christopher Peplin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
public class CharTrie implements Serializable {
    private static final long serialVersionUID = -4654213608476570586L;
    private static Logger sLogger = Logger.getLogger(CharTrie.class.getName());
    private int mSize = 0;
    private int mWordCount = 0;
    private final CharTrieNode mRoot;

    public CharTrie() {
        sLogger.info("Initializing new CharTrie");
        mRoot = new CharTrieNode();
    }

    public void add(String word) {
        sLogger.fine("Adding one occurrence of " + word);
        add(word, 1);
    }

    public void add(String word, int occurrences) {
        sLogger.fine("Adding " + occurrences + " occurrences of " 
                + word);
        if (mRoot.addWord(word.toLowerCase(), occurrences)) {
            sLogger.fine("Added previously unknown word " + word);
            mWordCount++;
        }
        mSize += occurrences;
    }

    public PrefixMatch match(String word) {
        sLogger.fine("Matching " + word);
        return mRoot.matchEntireWord(word);
    }

    public ArrayList<PrefixMatch> findAllPrefixMatches(String word) {
        sLogger.fine("Finding prefix matches for " + word);
        return mRoot.matchAllPrefixes(word);
    }

    public PrefixMatch findLongestPrefixMatch(String word) {
        sLogger.fine("Finding longest prefix match for " + word);
        ArrayList<PrefixMatch> result = findAllPrefixMatches(word);

        if (!result.isEmpty()) {
            Collections.sort(result);
            return result.get(result.size() - 1);
        }
        sLogger.fine(word + " is not in the trie");
        return null;
    }

    @Override
	public String toString() {
        return "[size = " + size() + ", word count = " + wordCount() + "]";
    }

    public int size() {
        return mSize;
    }

    public int wordCount() {
        return mWordCount;
    }

    public boolean isEmpty() {
        return mSize == 0;
    }
}
