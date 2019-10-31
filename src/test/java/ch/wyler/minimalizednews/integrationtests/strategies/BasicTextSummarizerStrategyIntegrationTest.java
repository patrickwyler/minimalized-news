package ch.wyler.minimalizednews.integrationtests.strategies;

import ch.wyler.minimalizednews.strategies.impl.BasicTextSummarizerStrategy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertThat;

@SpringBootTest
@Getter(AccessLevel.PROTECTED)
@RunWith(SpringRunner.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BasicTextSummarizerStrategyIntegrationTest {

	private static final int MAX_CHARS_LIMIT = 255;
	private static final String RAW_TEXT = "Auf microspot.ch finden Sie zu jedem Artikel detaillierte Produkt- und " +
			"Anwendungsinformationen. Die Entscheidung für das richtige Produkt, welches Ihre Bedürfnisse am " +
			"besten abdeckt, ist bei der grossen Auswahl auf microspot.ch nicht immer einfach. Wenn Sie beim " +
			"Einkauf gerne professionelle, fundierte und persönliche Beratung zu einem bestimmten Produkt wünschen, " +
			"dann laden wir Sie herzlich in einen unserer Showrooms ein. Unsere Showroom-Mitarbeiter freuen sich " +
			"auf Ihren Besuch und beantworten gerne alle Ihre Fragen. Bitte beachten Sie, dass in den Showrooms nur " +
			"ausgewählte Produkte ausgestellt werden. Insbesondere im Showroom Zürich HB ist nur ein kleines Sortiment " +
			"vor Ort vorhanden. Sie können aber alle Produkte aus unserem Sortiment bestellen sowie alle Services im " +
			"Showroom nutzen.";

	@Autowired
	BasicTextSummarizerStrategy strategy;

	@Test
	public void testSummarizationNotMoreThan255Chars() {
		// Act
		final String result = getStrategy().summarize(RAW_TEXT);

		// Assert
		assertThat("Sum shouldn't be larger than the limit of 255 chars.", result.length() <= MAX_CHARS_LIMIT, Matchers.is(true));
	}

	@Test
	public void testSummarization_nullText() {
		// Act
		final String result = getStrategy().summarize(null);

		// Assert
		assertThat("Should return empty string", result, Matchers.is(EMPTY));
	}

	@Test
	public void testSummarization_emptyText() {
		// Act
		final String result = getStrategy().summarize(EMPTY);

		// Assert
		assertThat("Should return empty string", result, Matchers.is(EMPTY));
	}
}