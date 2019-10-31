package ch.wyler.minimalizednews.integrationtests.repositories;

import ch.wyler.minimalizednews.entities.Article;
import ch.wyler.minimalizednews.repositories.ArticleRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@Getter(AccessLevel.PROTECTED)
@RunWith(SpringRunner.class)
@DataJpaTest
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleRepositoryIntegrationTest {

	private static final String UPDATE_1 = "Update 1";
	private static final String UPDATE_2 = "Update 2";
	private static final String UPDATE_3 = "Update 3";

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	ArticleRepository repository;

	@Before
	public void setUp() throws Exception {
		getRepository().save(Article.builder()
				.id(1L)
				.title(UPDATE_1)
				.rawText("Heute Abend kam es zu massiven Kursschwankungen im asiatischen Finanzmarkt.")
				.sourceName("News24")
				.sourceUrl("https://www.news24.com/test.html")
				.approved(true)
				.build());

		// wait between saves to have better timestamps
		Thread.sleep(1001);

		getRepository().save(Article.builder()
				.id(2L)
				.title(UPDATE_2)
				.rawText("Heute Abend kam es zu massiven Kursschwankungen im asiatischen Finanzmarkt.")
				.sourceName("News24")
				.sourceUrl("https://www.news24.com/test.html")
				.approved(true)
				.build());

		// wait between saves to have better timestamps
		Thread.sleep(1001);

		getRepository().save(Article.builder()
				.id(3L)
				.title(UPDATE_3)
				.rawText("Heute Abend kam es zu massiven Kursschwankungen im asiatischen Finanzmarkt.")
				.text("Heute Abend kam es zu massiven Kursschwankungen im asiatischen Finanzmarkt.")
				.sourceName("News24")
				.sourceUrl("https://www.news24.com/test.html")
				.approved(false)
				.build());
	}

	@Test
	public void testFindApprovedArticlesDesc() {
		// Act
		final List<Article> result = getRepository().findArticlesByApprovedIsTrueOrderByCreationDateDesc();

		// Assert
		assertThat("Amount not correct", result.size(), is(2));
		assertThat("Wrong first article", result.get(0).getTitle(), is(UPDATE_1));
		assertThat("Wrong second article", result.get(1).getTitle(), is(UPDATE_2));
	}

	@Test
	public void testFindReviewableArticlesDesc() {
		// Act
		final List<Article> result = getRepository().findArticlesByApprovedIsFalseAndTextIsNotNullOrderByCreationDateDesc();

		// Assert
		assertThat("Amount not correct", result.size(), is(1));
		assertThat("Wrong article", result.get(0).getTitle(), is(UPDATE_3));
	}
}
