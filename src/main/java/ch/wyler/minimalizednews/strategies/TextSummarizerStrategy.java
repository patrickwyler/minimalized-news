package ch.wyler.minimalizednews.strategies;

/**
 * Text summarizer strategy interface
 *
 * Could be that we need different algorithms
 */
public interface TextSummarizerStrategy {

	/**
	 * Summarize text
	 *
	 * @param text raw text to summarize
	 */
	String summarize(final String text);
}