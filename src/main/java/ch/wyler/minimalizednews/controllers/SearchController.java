package ch.wyler.minimalizednews.controllers;

import ch.wyler.minimalizednews.dtos.ArticleDto;
import ch.wyler.minimalizednews.mappers.ArticleMapper;
import ch.wyler.minimalizednews.mappers.TagMapper;
import ch.wyler.minimalizednews.services.impl.SearchService;
import ch.wyler.minimalizednews.services.impl.TagService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controller for search functions
 */
@Controller
@Getter(value = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchController {

	SearchService searchService;
	ArticleMapper articleMapper;
	TagMapper tagMapper;

	@Autowired
	public SearchController(final SearchService searchService, final ArticleMapper articleMapper, final TagService tagService, final TagMapper tagMapper) {
		this.searchService = searchService;
		this.articleMapper = articleMapper;
		this.tagMapper = tagMapper;
	}

	@GetMapping("/search")
	public String searchArticles(@RequestParam(value = "q") String searchText, final Model model) {
		final List<ArticleDto> articles = getArticleMapper().articleModelsToArticleDtos(getSearchService().search(searchText));
		model.addAttribute("articles", articles);
		return "articles/search";
	}

	@GetMapping(value = "/search", params = "tag")
	public String searchArticlesByTag(@RequestParam(value = "tag") String tagName, final Model model) {
		final List<ArticleDto> articles = getArticleMapper().articleModelsToArticleDtos(getSearchService().searchByTag(tagName));
		model.addAttribute("articles", articles);
		return "articles/search";
	}
}
