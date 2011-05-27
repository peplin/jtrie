package com.bueda.util;

import java.util.Comparator;

/**
 * PrefixMatch
 *
 * A prefix matching some search word. Used by CharTrie only for now.
 *
 * Christopher Peplin (chris.peplin@rhubarbtech.com)
 * Bueda, 2009
 */
public class PrefixMatch implements Comparable<PrefixMatch> {
    private String mWord;
    private int mOccurrences;
    private int mIndex;

    public PrefixMatch(String word, int index, int occurrences) {
        mWord = word;
        mIndex = index;
        mOccurrences = occurrences;
    }

    /**
     * Get the prefix that matches part of all of the original word.
     */
    public String getWord() {
        return mWord;
    }

    /**
     * Get the character index this prefix stops at in the original string.
     */
    public int getIndex() {
        return mIndex;
    }

    /**
     * Get the number of occurrences of this prefix in the index.
     */
    public int getOccurrences() {
        return mOccurrences;
    }

    public int compareTo(PrefixMatch other) {
        return Integer.valueOf(getIndex()).compareTo(
                Integer.valueOf(other.getIndex()));
    }

    /** 
     * Compare PrefixMatch instances based on their number of occurrences.
     * More occurrences, greater comparaison.
     */
	public static class PrefixMatchFrequencyComparator implements
			Comparator<PrefixMatch> {
		@Override
		public int compare(PrefixMatch first, PrefixMatch second) {
			return Integer.valueOf(first.getOccurrences()).compareTo(
					Integer.valueOf(second.getOccurrences()));
		}
	}
}
