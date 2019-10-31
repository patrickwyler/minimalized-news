package ch.wyler.minimalizednews.utils;

import ch.wyler.minimalizednews.config.Config;
import ch.wyler.minimalizednews.mappers.ArticleMapper;
import ch.wyler.minimalizednews.mappers.TagMapper;
import ch.wyler.minimalizednews.pojos.TestDataArticle;
import ch.wyler.minimalizednews.services.impl.ArticleService;
import ch.wyler.minimalizednews.services.impl.TagService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

import static org.mockserver.model.JsonBody.json;

/**
 * Mock server can be enabled/disabled with a config property. Should be mocking an external content partner.
 */
@Slf4j
@Component
@Getter(value = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MockServer {

	private static final int RATE = 1000 * 10; // 10 seconds
	private static final int DELAY = 1000 * 10; // 10 seconds
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String APPLICATION_JSON = "application/json";
	private static final int STATUS_CODE = 200;
	private static final String HTTP_METHOD = "GET";
	private static final String ROOT_PATH = "/";
	private static final int MOCK_SERVER_PORT = 9090;

	ArticleService articleService;
	ArticleMapper articleMapper;
	TagService tagService;
	TagMapper tagMapper;
	Config config;
	ClientAndServer mockServer;

	@Autowired
	public MockServer(final ArticleService articleService, final ArticleMapper articleMapper, final TagService tagService, final TagMapper tagMapper, final Config config) {
		this.articleService = articleService;
		this.articleMapper = articleMapper;
		this.tagService = tagService;
		this.tagMapper = tagMapper;
		this.config = config;
	}

	/**
	 * Add testdata automatically while starting up the app
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void setUp() {
		// check if testdata should be created or not
		if (!getConfig().isTestModeEnabled()) {
			return;
		}

		startupMockServer();
	}

	private void startupMockServer() {
		mockServer = ClientAndServer.startClientAndServer(MOCK_SERVER_PORT);
		switchMockServerResponse();
	}

	/**
	 * wait n seconds and then generate all n second a different response
	 */
	@Scheduled(fixedRate = RATE, initialDelay = DELAY)
	public void switchMockServerResponse() {
		// check if testdata should be created or not
		if (!getConfig().isTestModeEnabled()) {
			return;
		}

		final HttpRequest httpRequest = HttpRequest.request()
				.withMethod(HTTP_METHOD)
				.withPath(ROOT_PATH);

		final Random rand = new Random();

		mockServer.clear(httpRequest);
		mockServer.when(httpRequest)
				.respond(HttpResponse.response()
						.withStatusCode(STATUS_CODE)
						.withHeader(CONTENT_TYPE, APPLICATION_JSON)
						.withBody(json("{\"title\": \""
								+ getTestDataArticle(rand).getTitle()
								+ "\", \"text\": \""
								+ getTestDataArticle(rand).getText()
								+ "\"}")));
	}

	/**
	 * Get random article
	 *
	 * @param rand random object
	 * @return random article
	 */
	private TestDataArticle getTestDataArticle(final Random rand) {
		return getConfig().getTestdataarticles().get(rand.nextInt(getConfig().getTestdataarticles().size()));
	}
}