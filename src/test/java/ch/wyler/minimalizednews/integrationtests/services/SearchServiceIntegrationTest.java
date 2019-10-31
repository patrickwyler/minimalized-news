package ch.wyler.minimalizednews.integrationtests.services;

import ch.wyler.minimalizednews.models.ArticleModel;
import ch.wyler.minimalizednews.services.impl.SearchService;
import ch.wyler.minimalizednews.utils.TestdataCreator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@Getter(AccessLevel.PROTECTED)
@RunWith(SpringRunner.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchServiceIntegrationTest {

	private static final String ALL = StringUtils.EMPTY;
	private static final String SEARCH_TITLE = "Gadget Test";
	private static final String SEARCH_TEXT = "Ergebnisse";
	private static final String SEARCH_NO_HIT = "Autounfall";
	private static final String SEARCH_TAG = "Finanzen";
	private static final String SEARCH_TAG_PARENT = "Schweiz";

	@Autowired
	SearchService searchService;

	@Autowired
	TestdataCreator testdataCreator;

	@Test
	public void testSearchWithEmptyString() {
		// Arrange
		getTestdataCreator().createTestData();

		// Act
		final List<ArticleModel> result = getSearchService().search(ALL);

		// Assert
		assertThat("Only one result should be found", result.size(), Matchers.is(3));
	}

	@Test
	public void testSearchNoHit() {
		// Arrange
		getTestdataCreator().createTestData();

		// Act
		final List<ArticleModel> result = getSearchService().search(SEARCH_NO_HIT);

		// Assert
		assertThat("No result should be found", result.size(), Matchers.is(0));
	}

	@Test
	public void testSearchFullTextByTitle() {
		// Arrange
		getTestdataCreator().createTestData();

		// Act
		final List<ArticleModel> result = getSearchService().search(SEARCH_TITLE);

		// Assert
		assertThat("Only one result should be found", result.size(), Matchers.is(1));
		assertTrue("Title should contain the searched string", result.get(0).getTitle().contains(SEARCH_TITLE));
	}

	@Test
	public void testSearchFullTextByText() {
		// Arrange
		getTestdataCreator().createTestData();

		// Act
		final List<ArticleModel> result = getSearchService().search(SEARCH_TEXT);

		// Assert
		assertThat("Only one result should be found", result.size(), Matchers.is(1));
		assertTrue("Text should contain the searched string", result.get(0).getText().contains(SEARCH_TEXT));
	}

	@Test
	public void testSearchFullTextByTag() {
		// Arrange
		getTestdataCreator().createTestData();

		// Act
		final List<ArticleModel> result = getSearchService().search(SEARCH_TAG);

		// Assert
		assertThat("Only one result should be found", result.size(), Matchers.is(1));
	}

	@Test
	public void testSearchByTag_withoutChildTags() {
		// Arrange
		getTestdataCreator().createTestData();

		// Act
		final List<ArticleModel> result = getSearchService().searchByTag(SEARCH_TAG);

		// Assert
		assertThat("Only one result should be found", result.size(), Matchers.is(1));
	}

	@Test
	public void testSearchByTag_withArticlesFromChildTags() {
		// Arrange
		getTestdataCreator().createTestData();

		// Act
		final List<ArticleModel> result = getSearchService().searchByTag(SEARCH_TAG_PARENT);

		// Assert
		assertThat("Only one result should be found", result.size(), Matchers.is(1));
	}
}