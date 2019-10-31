package ch.wyler.minimalizednews.services.impl;

import ch.wyler.minimalizednews.models.ArticleModel;
import ch.wyler.minimalizednews.pojos.ExternalArticleResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Getter(value = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportService {

	private static final String URL = "http://localhost:9090";
	private static final String SOURCE_NAME_DEFAULT = "External Newspage";
	private static final String SOURCE_URL_DEFAULT = "http://localhost:9090/nolink";
	private static final int RATE = 1000 * 30; // 30 seconds
	private static final int DELAY = 1000 * 15; // 15 seconds

	ArticleService articleService;
	RestTemplate restTemplate;

	@Autowired
	public ImportService(final ArticleService articleService, final RestTemplateBuilder restTemplateBuilder) {
		this.articleService = articleService;
		this.restTemplate = restTemplateBuilder.build();
	}

	/**
	 * wait n seconds and then perform all n second a new request
	 */
	@Scheduled(fixedRate = RATE, initialDelay = DELAY)
	public void fetchData() {
		// get data from mocked endpoint
		final ExternalArticleResponse externalArticleResponse = getRestTemplate().getForObject(URL, ExternalArticleResponse.class);

		// create article
		final ArticleModel articleModel = getArticleService().create(ArticleModel.builder()
				.title(externalArticleResponse.getTitle())
				.rawText(externalArticleResponse.getText())
				.sourceName(SOURCE_NAME_DEFAULT)
				.sourceUrl(SOURCE_URL_DEFAULT)
				.build());

		// create tags for article
		getArticleService().setRelatedTags(articleModel);

		// summarize
		getArticleService().summarizeById(articleModel.getId());
	}
}
