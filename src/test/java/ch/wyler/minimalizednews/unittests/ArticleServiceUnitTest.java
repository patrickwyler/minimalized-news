package ch.wyler.minimalizednews.unittests;

import ch.wyler.minimalizednews.entities.Article;
import ch.wyler.minimalizednews.mappers.ArticleMapper;
import ch.wyler.minimalizednews.repositories.ArticleRepository;
import ch.wyler.minimalizednews.services.impl.ArticleService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Getter(AccessLevel.PROTECTED)
@RunWith(MockitoJUnitRunner.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleServiceUnitTest {

	Article ARTICLE;

	@InjectMocks
	ArticleService service;

	@Mock
	ArticleRepository repository;

	@Mock
	ArticleMapper mapper;

	@Before
	public void setUp() {
		ARTICLE = Article.builder()
				.id(1L)
				.title("Update 1")
				.rawText("Heute Abend kam es zu massiven Kursschwankungen im asiatischen Finanzmarkt.")
				.sourceName("News24")
				.sourceUrl("https://www.news24.com/test.html")
				.approved(true)
				.build();
	}

	@Test
	public void testGetAllArticles() {
		// Arrange
		final List<Article> results = new ArrayList<>();
		results.add(ARTICLE);
		when(getRepository().findAll()).thenReturn(results);

		// Act
		getService().getAll();

		// Assert
		verify(getMapper(), times(1)).articlesToArticleModels(results);
	}

	@Test
	public void testGetAllArticles_noResult() {
		// Arrange
		final List<Article> results = new ArrayList<>();
		when(getRepository().findAll()).thenReturn(results);

		// Act
		getService().getAll();

		// Assert
		verify(getMapper(), times(1)).articlesToArticleModels(results);
	}
}