package com.bueda.util.datatypes.trie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.bueda.util.PrefixMatch;

/**
 * Trie Data Structure
 * 
 * TrieNode is a hash-based node for the Trie data structure. There is no limit
 * on the number of children, but no data is actually stored in the node itself
 * - only its key may be used to determine its value. There is also no record of
 * the parent node, so backtracking is not possible in the current
 * implementation.
 *
 * The trie uses compressed tail storage to optimize both memory usage and
 * lookup performance. 
 * 
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
public class CharTrieNode implements Serializable {
	private static final long serialVersionUID = 8762843863706656966L;
    private static Logger sLogger = Logger.getLogger(
            CharTrieNode.class.getName());
	private Map<Character, CharTrieNode> mChildren;
	private int mOccurrences = 0;
	private String mCompressedTail;

	public CharTrieNode(String compressedTail) {
		mChildren = null;
		mCompressedTail = compressedTail;
        if(mCompressedTail != null && mCompressedTail.isEmpty()) {
            mCompressedTail = null;
        }
	}

	public CharTrieNode() {
		this(null);
	}

	public void addOccurrences(int occurrences) {
		mOccurrences += occurrences;
	}

	public int getOccurrences() {
		return mOccurrences;
	}

	/**
	 * Adds a word to this node. This method is called recursively and adds
	 * child nodes for each successive letter in the word, therefore recursive
	 * calls will be made with partial words.
	 * 
	 * @param word
	 *            the word to add
	 */
	protected boolean addWord(String word, int occurrences) {
		if (!word.isEmpty()) {
			final char firstChar = word.charAt(0);
			String tail = word.substring(1);

            sLogger.log(Level.FINE, "Adding {0} occurrences of {1}", 
                    new Object[]{occurrences, word});
			if (mCompressedTail != null) {
            sLogger.log(Level.FINE, "Found node with tail {0} when remaining word is {1}", 
                        new Object[]{mCompressedTail, word});
                if(word.equals(mCompressedTail)) {
                    sLogger.log(Level.FINE,
                            "Node tail {0} matched remaining word {1} "
                            + "-- adding occurrence",
                            new Object[]{mCompressedTail, word});
                    addOccurrences(occurrences);
                    sLogger.log(Level.FINE, "Number of occurrences is now {0}",
                            getOccurrences());
                    return false;
                } else {
                    splitTail();
                }
			}

			if (mChildren == null) {
                sLogger.log(Level.FINE,
                        "Node has no children, adding child for char {0}", 
                        firstChar);
				mChildren = Collections.singletonMap(firstChar,
						new CharTrieNode(tail));
				mChildren.get(firstChar).addOccurrences(occurrences);
				return true;
			} else if (!mChildren.containsKey(firstChar)) {
				if (mChildren.size() == 1) {
					CharTrieNode oldLeaf = mChildren.values().iterator().next();
					Character oldLeafKey = mChildren.keySet().iterator().next();
					mChildren = new HashMap<Character, CharTrieNode>();
					mChildren.put(oldLeafKey, oldLeaf);
				}
                sLogger.log(Level.FINE,
                        "Node has children, adding child for char {0}",
                        firstChar);
				mChildren.put(firstChar, new CharTrieNode(tail));
				mChildren.get(firstChar).addOccurrences(occurrences);
				return true;
			} else if (word.length() > 1) {
                CharTrieNode child = mChildren.get(firstChar);
                sLogger.log(Level.FINE, "Node has child {0} for char {1}",
                        new Object[]{child, firstChar});
				return child.addWord(tail, occurrences);
			} else {
                CharTrieNode child = mChildren.get(firstChar);
                sLogger.log(Level.FINE, "Existing child {0} for {1} found, "
                        + "and we're at the end of the added word --",
                        new Object[]{child, firstChar});
                mChildren.get(firstChar).splitTail();
				mChildren.get(firstChar).addOccurrences(occurrences);
				return mChildren.get(firstChar).getOccurrences() == 1;
			}
		}
		return false;
	}

    private void splitTail() {
        if(mCompressedTail != null) {
            String oldTail = mCompressedTail;
            int occurrences = mOccurrences;
            mCompressedTail = null;
            mOccurrences = 0;
            addWord(oldTail, occurrences);
        }
    }

	public ArrayList<PrefixMatch> matchAllPrefixes(String word) {
		return matchAllPrefixes(word, 0, new ArrayList<PrefixMatch>());
	}

	private ArrayList<PrefixMatch> matchAllPrefixes(String word, int length,
			ArrayList<PrefixMatch> results) {
		if (mOccurrences > 0) {
			// Prefix is a word at this point
            String match = word.substring(0, length);
            sLogger.log(Level.FINE, "Prefix {0} of word {1} is a match"
                    + " -- checking for existence of tail {2}",
                    new Object[]{match, word, mCompressedTail});
            if(mCompressedTail != null 
                    && word.substring(length).startsWith(mCompressedTail)) {
                sLogger.log(Level.FINE, "Node tail {0} matched word tail {1}",
                        new Object[]{mCompressedTail, word.substring(length)});
                match += mCompressedTail;
                length += mCompressedTail.length();
                results.add(new PrefixMatch(match, length, mOccurrences));
            } else if(mCompressedTail == null) {
                sLogger.log(Level.FINE,
                        "Node has no tail {0} adding prefix {1} to results {2}",
                        new Object[]{mCompressedTail, match, results});
                results.add(new PrefixMatch(match, length, mOccurrences));
            }
		}

		if (word.length() > length) {
            sLogger.log(Level.FINE,
                    "Word {0} has characters remaining after length {1}",
                    new Object[]{word, length});
            if (mChildren != null) {
                sLogger.log(Level.FINE, "Node has children {0}", mChildren);
                char nextChar = word.charAt(length);
                if (mChildren.containsKey(nextChar)) {
                    CharTrieNode child = mChildren.get(nextChar);
                    sLogger.log(Level.FINE, "Node has child {0} at char {1}",
                            new Object[]{child, nextChar});
                    child.matchAllPrefixes(word, length + 1, results);
                }
            }
        }
		return results;
	}

	protected ArrayList<String> getWords(ArrayList<String> list) {
		if (mOccurrences > 0) {
			list.add(toString());
		}

		if (mChildren != null) {
			final Collection<CharTrieNode> childNodes = mChildren.values();
			for (CharTrieNode node : childNodes) {
				node.getWords(list);
			}
		}
		return list;
	}

	public PrefixMatch matchEntireWord(String word) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
	public String toString()  {
        return "[mOccurrences = " + mOccurrences + ", mCompressedTail = "
            + mCompressedTail + ", mChildren = " + mChildren + "]";
    }
}
