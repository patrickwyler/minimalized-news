package ch.wyler.minimalizednews.strategies.impl;

import ch.wyler.minimalizednews.config.Config;
import ch.wyler.minimalizednews.strategies.TextSummarizerStrategy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Basic text summarizing algorithm
 * <p>
 * The main code is from Karim Oumghar (https://github.com/karimo94/Text-Summarizer),
 * I changed a lot of it to match my use case and to support german texts.
 */
@Slf4j
@Getter(value = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class BasicTextSummarizerStrategy implements TextSummarizerStrategy {

	private static final int MAX_CHARACTERS_OF_SUMMARY_LENGTH = 255;
	private static final int SENTENCE_END_LENGTH = 2; // dot + space for next sentence
	private static final String REGEX_SPECIAL_CHARS = "[\\[\\]\\{\\}\\/,_\".!?:;)(]";
	private static final String LINE_BREAK_CASE_1 = "\n\n";
	private static final String LINE_BREAK_CASE_2 = "\n";
	private static final String LINE_BREAK_CASE_3 = "\r";
	private static final String REGEX_LETTER_ENUMERATION = "([A-Z])\\.";
	private static final String REGEX_NOT_SPLIT_DECIMAL_NUMBERS = "(?<!\\d)\\. (?!\\d)|(?<=\\d)\\. (?!\\d)|(?<!\\d)\\. (?=\\d)";

	Config config;

	@Autowired
	public BasicTextSummarizerStrategy(final Config config) {
		this.config = config;
	}

	@Override
	public String summarize(final String text) {
		if (isBlank(text)) {
			log.warn("No input text to summarize");
			return EMPTY;
		}

		// start with raw freqs
		final Map<String, Integer> wordFrequencies = getWordCounts(text);

		// filter
		final Map<String, Integer> filtered = filterStopWords(wordFrequencies);

		// sort
		final List<String> sorted = sortByFreqThenDropFreq(filtered);

		// split the sentences
		final String[] sentences = getSentences(text);

		// select up to maxSummarySize sentences
		final List<String> setSummarySentences = new ArrayList<>();
		for (String word : sorted) {
			String first_matching_sentence = search(sentences, word);
			setSummarySentences.add(first_matching_sentence); // add to summary list
			if (setSummarySentences.size() == 5) {
				break;
			}
		}

		final StringBuilder summary = new StringBuilder();

		for (String sentence : sentences)
		{
			if (setSummarySentences.contains(sentence)) {
				int countOfLetters = summary.length() + sentence.length();

				if (countOfLetters >= MAX_CHARACTERS_OF_SUMMARY_LENGTH - SENTENCE_END_LENGTH) {
					break;
				}

				summary.append(sentence).append(". ");
			}
		}

		return summary.toString();
	}

	/**
	 * scan entire text and record all words and word counts
	 * so if a word appears multiple times, increment the word count for that particular word
	 * if a word appears only once, add the new word to the Map
	 *
	 * @param text text to count words from
	 * @return all words as array
	 */
	private Map<String, Integer> getWordCounts(final String text) {
		final Map<String, Integer> allWords = new HashMap<>();

		int count;
		int singleIncrement = 0;
		final String[] words = text.split("\\s+");

		for (final String word : words) {
			count = 0;

			final String wordCleaned = word.replaceAll(REGEX_SPECIAL_CHARS, EMPTY);

			if (allWords.containsKey(wordCleaned)) {
				allWords.put(wordCleaned, singleIncrement += 1);
			} else {
				allWords.put(wordCleaned, count++);
			}
		}
		return allWords;
	}

	/**
	 * filter any stop words here, so remove from the dictionary collection
	 * return a dictionary, use the dictionary to store the frequency of a word and the word itself
	 *
	 * @param wordFrequencies used words
	 * @return filtered stop words
	 */
	private Map<String, Integer> filterStopWords(final Map<String, Integer> wordFrequencies) {
		final String[] stop_words = getConfig().getStopwords();

		for (final String stop_word : stop_words) {
			wordFrequencies.remove(stop_word);
		}

		return wordFrequencies;
	}

	private List<String> sortByFreqThenDropFreq(final Map<String, Integer> wordFrequencies) {
		// sort the dictionary, sort by frequency and drop counts ['code', language']
		// return a List<string>
		final List<String> sortedCollection = new ArrayList<>(wordFrequencies.keySet());
		Collections.sort(sortedCollection);
		Collections.reverse(sortedCollection); // largest to smallest
		return sortedCollection;
	}

	private String[] getSentences(final String text) {
		String textCleanedUp = text;

		// fix line breaks
		textCleanedUp = textCleanedUp
				.replace(LINE_BREAK_CASE_1, EMPTY)
				.replace(LINE_BREAK_CASE_2, EMPTY)
				.replace(LINE_BREAK_CASE_3, EMPTY);

		// fix all abbreviations
		for (Map.Entry<String, String> abbreviationEntry : getConfig().getAbbreviations().entrySet()) {
			textCleanedUp = textCleanedUp.replace(abbreviationEntry.getKey(), abbreviationEntry.getValue());
		}

		// fix alphabet letters like A. B. etc...use a regex
		textCleanedUp = textCleanedUp.replaceAll(REGEX_LETTER_ENUMERATION, "$1");

		// split using ., !, ?, and omit decimal numbers
		final Pattern pt = Pattern.compile(REGEX_NOT_SPLIT_DECIMAL_NUMBERS);

		return pt.split(textCleanedUp);
	}

	/**
	 * Search for a particular sentence containing a particular word this function will return the first
	 * matching sentence that has a value word
	 *
	 * @param sentences all sentences
	 * @param word      searched word
	 * @return first matching sentence that has a value word
	 */
	private String search(final String[] sentences, final String word) {
		String first_matching_sentence = null;

		for (final String sentence : sentences) {
			if (sentence.contains(word)) {
				first_matching_sentence = sentence;
			}
		}
		return first_matching_sentence;
	}
}
