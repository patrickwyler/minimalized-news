package ch.wyler.minimalizednews.services.impl;

import ch.wyler.minimalizednews.entities.Article;
import ch.wyler.minimalizednews.entities.Tag;
import ch.wyler.minimalizednews.mappers.ArticleMapper;
import ch.wyler.minimalizednews.mappers.TagMapper;
import ch.wyler.minimalizednews.models.ArticleModel;
import ch.wyler.minimalizednews.repositories.ArticleRepository;
import ch.wyler.minimalizednews.repositories.TagRepository;
import ch.wyler.minimalizednews.repositories.helper.ArticleSpecification;
import ch.wyler.minimalizednews.repositories.helper.SearchCriteria;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ch.wyler.minimalizednews.repositories.helper.SearchCriteriaOperation.EQUALS;
import static ch.wyler.minimalizednews.repositories.helper.SearchCriteriaOperation.LIKE;

@Service
@Getter(value = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchService {

	private static final String FIELD_APPROVED = "approved";
	private static final String FIELD_TITLE = "title";
	private static final String FIELD_TEXT = "text";
	private static final String TABLE_TAGS = "tags";
	private static final String FIELD_NAME = "name";

	ArticleRepository articleRepository;
	ArticleMapper articleMapper;
	TagRepository tagRepository;
	TagMapper tagMapper;

	@Autowired
	public SearchService(final ArticleRepository articleRepository, final ArticleMapper articleMapper, final TagRepository tagRepository, final TagMapper tagMapper) {
		this.articleRepository = articleRepository;
		this.articleMapper = articleMapper;
		this.tagRepository = tagRepository;
		this.tagMapper = tagMapper;
	}

	/**
	 * Free text search function
	 *
	 * @param searchText search text
	 * @return search result
	 */
	public List<ArticleModel> search(final String searchText) {
		// only approved articles should be found
		final ArticleSpecification spec1 = new ArticleSpecification(new SearchCriteria(FIELD_APPROVED, EQUALS, true));
		// search in title, text and sourceName fields with the search text
		final ArticleSpecification spec2 = new ArticleSpecification(new SearchCriteria(FIELD_TITLE, LIKE, searchText));
		final ArticleSpecification spec3 = new ArticleSpecification(new SearchCriteria(FIELD_TEXT, LIKE, searchText));
		// join tags and search for tags with name like the search text
		final ArticleSpecification spec4 = new ArticleSpecification(new SearchCriteria(TABLE_TAGS, FIELD_NAME, LIKE, searchText));

		// search query specification
		final Specification specification = Specification.where(spec1).and((spec2).or(spec3).or(spec4));

		return getArticleMapper().articlesToArticleModels(getArticleRepository().findAll(specification));
	}

	/**
	 * Search by tag name, check subtags too for articles
	 *
	 * @param tagName tag name
	 * @return search result
	 */
	public List<ArticleModel> searchByTag(final String tagName) {
		final Set<Article> articles = new HashSet<>();
		final Tag tag = getTagRepository().findTagByName(tagName);

		// no tag found => return empty result list
		if (tag == null) {
			return new ArrayList<>();
		}

		// add articles from selected tag
		tag.getArticles().stream().filter(Article::isApproved).forEach(articles::add);

		// add articles from all child tags
		addChildArticles(articles, tag);

		return new ArrayList(getArticleMapper().articlesToArticleModels(articles));
	}

	/**
	 * Add articles attached to subtags to the search result
	 *
	 * @param articles search result
	 * @param tag tag from which the child tags should be checked for articles
	 */
	private void addChildArticles(final Set<Article> articles, final Tag tag) {
		tag.getChildTags().forEach(childTag -> {
			articles.addAll(childTag.getArticles().stream().filter(Article::isApproved).collect(Collectors.toList()));

			// recursive method call, get all articles from childs and the childs from the childs..
			addChildArticles(articles, childTag);
		});
	}
}
