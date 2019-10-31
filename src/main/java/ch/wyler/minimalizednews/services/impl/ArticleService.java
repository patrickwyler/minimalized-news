package ch.wyler.minimalizednews.services.impl;

import ch.wyler.minimalizednews.entities.Article;
import ch.wyler.minimalizednews.mappers.ArticleMapper;
import ch.wyler.minimalizednews.models.ArticleModel;
import ch.wyler.minimalizednews.repositories.ArticleRepository;
import ch.wyler.minimalizednews.services.CrudService;
import ch.wyler.minimalizednews.strategies.TextSummarizerStrategy;
import ch.wyler.minimalizednews.strategies.impl.BasicTextSummarizerStrategy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Getter(value = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleService implements CrudService<ArticleModel> {

	private static final String REGEX_SPECIAL_CHARS = "[\\[\\]\\{\\}\\/,_\".!?:;)(]";
	private static final String SPACES = "\\s+";

	ArticleRepository articleRepository;
	ArticleMapper articleMapper;
	TextSummarizerStrategy basicTextSummarizerStrategyStrategy;
	TagService tagService;

	@Autowired
	public ArticleService(final ArticleRepository articleRepository, final ArticleMapper articleMapper, final BasicTextSummarizerStrategy basicTextSummarizerStrategyStrategy, final TagService tagService) {
		this.articleRepository = articleRepository;
		this.articleMapper = articleMapper;
		this.basicTextSummarizerStrategyStrategy = basicTextSummarizerStrategyStrategy;
		this.tagService = tagService;
	}

	@Override
	public List<ArticleModel> getAll() {
		return getArticleMapper().articlesToArticleModels(getArticleRepository().findAll());
	}

	@Override
	public ArticleModel create(final ArticleModel articleModel) {
		final Article article = getArticleRepository().save(getArticleMapper().articleModelToArticle(articleModel));
		return getArticleMapper().articleToArticleModel(article);
	}

	@Override
	public ArticleModel update(final ArticleModel articleModel) {
		getById(articleModel.getId());

		final Article article = getArticleRepository().save(getArticleMapper().articleModelToArticle(articleModel));
		return getArticleMapper().articleToArticleModel(article);
	}

	/**
	 * TODO: try to refactor this, not needed for POC right now
	 *
	 * Had the problem, that I couldn't edit articles without removing already assigned tags. Therefore I created this
	 * update method. Would be nicer to have only one which is taking care about all update cases.
	 */
	public ArticleModel updateWithoutTags(final ArticleModel articleModel) {
		final Article articleToUpdate = getArticleMapper().articleModelToArticle(getById(articleModel.getId()));
		final Article changedArticle = getArticleMapper().articleModelToArticle(articleModel);

		changedArticle.setTags(articleToUpdate.getTags());

		final Article article = getArticleRepository().save(changedArticle);
		return getArticleMapper().articleToArticleModel(article);
	}

	@Override
	public void deleteById(final long id) {
		getArticleRepository().deleteById(id);
	}

	@Override
	public ArticleModel getById(final long id) {
		final Article article = getArticleRepository().findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id:" + id));
		return getArticleMapper().articleToArticleModel(article);
	}

	public List<ArticleModel> getAllApprovedOrderedByCreationDate() {
		return getArticleMapper().articlesToArticleModels(getArticleRepository().findArticlesByApprovedIsTrueOrderByCreationDateDesc());
	}

	public List<ArticleModel> getAllReviewNeeded() {
		return getArticleMapper().articlesToArticleModels(getArticleRepository().findArticlesByApprovedIsFalseAndTextIsNotNullOrderByCreationDateDesc());
	}

	public void approveById(final long id) {
		final ArticleModel articleModel = getById(id);
		articleModel.setApproved(true);
		update(articleModel);
	}

	public void summarizeById(final long id) {
		final ArticleModel articleModel = getById(id);
		articleModel.setText(getBasicTextSummarizerStrategyStrategy().summarize(articleModel.getRawText()));
		update(articleModel);
	}

	/**
	 * Set tags related to raw article text
	 *
	 * @param articleModel article
	 */
	public void setRelatedTags(final ArticleModel articleModel) {
		checkNotNull(articleModel.getRawText());

		// get words from text
		final Set<String> cleanedWords = getWords(articleModel);

		// find matching tags
		getTagService().getAll().forEach(tagModel -> {
					if (cleanedWords.contains(tagModel.getName())) {
						if (articleModel.getTags() == null) {
							articleModel.setTags(new HashSet<>());
						}
						// add tag
						articleModel.getTags().add(tagModel);
					}
				}
		);

		update(articleModel);
	}

	private Set<String> getWords(final ArticleModel articleModel) {
		final String[] words = articleModel.getRawText().split(SPACES);
		return Arrays.stream(words).map(word -> word.replaceAll(REGEX_SPECIAL_CHARS, "")).collect(Collectors.toSet());
	}
}
