package com.bueda;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.bueda.util.datatypes.trie.CharTrie;
import com.bueda.util.PrefixMatch;

public class TrieTest {
	private static CharTrie trie;
	private static String[] testValues = { "the", "book", "bookshelf",
			"basketball", "basket", "the", "2", "don't" };
	private static String[] testBadValues = { "th", "boo", "bookshe",
			"basketbx", "basket", "tz", "22222", "don'" };
	private static String[] testBadValuesResults = { null, null, "book",
			"basket", "basket", null, "2", null };
	private static int[] testBadValuesIndexResults = { 0, 0, 4, 6, 6, 0, 1, 0 };
	private static String[] simpleValues = { "the", "book", "shelf", "ball",
			"basket", "2", "don't" };
	private static String[] compoundValues = { "bookshelf", "basketball",
			"theology", "22" };
	private static String[] simpleBadValues = { "th", "boo", "shel", "ba",
			"bask", "don'" };
	private static String[] compoundBadValues = { "booksh", "basketb", "theo",
			"2" };
	private static String[] compoundBadValuesResults = { "book", "basket",
			"the", "2" };

	@Before
	public void setUp() {
		trie = new CharTrie();

		for (int i = 0; i < testValues.length; i++) {
			trie.add(testValues[i]);
		}
	}

	@Test
	public void testSize() {
		assertEquals(trie.wordCount(), 7);
		assertEquals(trie.size(), 8);
		trie = new CharTrie();
		assertEquals(trie.wordCount(), 0);
	}

	@Test
	public void testIsEmpty() {
		assertFalse(trie.isEmpty());
		trie = new CharTrie();
		assertTrue(trie.isEmpty());
	}

	@Test
	public void testSimplePositivePrefixMatch() {
		for (int i = 0; i < testValues.length; i++) {
            PrefixMatch match = trie.findLongestPrefixMatch(testValues[i]);
            assertNotNull("Match of " + testValues[i] + " shouldn't be null", match);
			assertEquals(match.getWord(), testValues[i]);
		}
	}

	@Test
	public void testSimpleNegativePrefixMatch() {
		for (int i = 0; i < testValues.length; i++) {
			PrefixMatch result = trie
					.findLongestPrefixMatch(testBadValues[i]);
			if (result != null) {
				assertEquals(result.getWord(), testBadValuesResults[i]);
			} else {
				assertEquals(testBadValuesResults[i], null);
			}
		}
	}

	@Test
	public void testPositivePrefixMatchIndex() {
		for (int i = 0; i < testValues.length; i++) {
            PrefixMatch match = trie.findLongestPrefixMatch(testValues[i]);
            assertNotNull("Match of " + testValues[i] + " shouldn't be null", match);
			assertEquals(match.getIndex(), testValues[i].length());
		}
	}

	@Test
	public void testNegativePrefixMatchIndex() {
		for (int i = 0; i < testValues.length; i++) {
			PrefixMatch result = trie
					.findLongestPrefixMatch(testBadValues[i]);
			if (result != null) {
				assertEquals(result.getIndex(), testBadValuesIndexResults[i]);
			} else {
				assertEquals(testBadValuesIndexResults[i], 0);
			}
		}
	}

	@Test
	public void testBlankMatch() {
		assertEquals(trie.findLongestPrefixMatch(""), null);
	}

	@Test
	public void testNonexistentMatch() {
		assertEquals(trie.findLongestPrefixMatch("foobar"), null);
		assertEquals(trie.findLongestPrefixMatch("boo"), null);
	}

	@Test
	public void testSimplePositiveMatch() {
		for (String value : simpleValues) {
			trie.add(value);
		}

		for (int i = 0; i < simpleValues.length; i++) {
			assertEquals(
					trie.findLongestPrefixMatch(simpleValues[i]).getWord(),
					simpleValues[i]);
		}
	}

	@Test
	public void testSimpleNegativeMatch() {
		for (String value : simpleValues) {
			trie.add(value);
		}
		for (int i = 0; i < simpleBadValues.length; i++) {
			assertEquals(trie.findLongestPrefixMatch(simpleBadValues[i]), null);
		}
	}

	@Test
	public void testCompoundPositiveMatch() {
		for (String value : compoundValues) {
			trie.add(value);
		}
		for (int i = 0; i < compoundValues.length; i++) {
			assertEquals(trie.findLongestPrefixMatch(compoundValues[i])
					.getWord(), compoundValues[i]);
		}
	}

	@Test
	public void testCompoundNegativeMatch() {
		for (String value : compoundValues) {
			trie.add(value);
		}
		for (int i = 0; i < compoundBadValues.length; i++) {
            PrefixMatch match = 
                trie.findLongestPrefixMatch(compoundBadValues[i]);
            assertNotNull("Match of " + compoundBadValues[i] 
                    + " shouldn't have been null", match);
			assertEquals(match.getWord(), compoundBadValuesResults[i]);
		}
	}
}
