package ch.wyler.minimalizednews.controllers;

import ch.wyler.minimalizednews.dtos.ArticleDto;
import ch.wyler.minimalizednews.mappers.ArticleMapper;
import ch.wyler.minimalizednews.services.impl.ArticleService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Index controller entrypoint of the web application
 */
@Controller
@Getter(value = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IndexController {

	ArticleService articleService;
	ArticleMapper articleMapper;

	@Autowired
	public void ArticleController(final ArticleService articleService, final ArticleMapper articleMapper) {
		this.articleService = articleService;
		this.articleMapper = articleMapper;
	}

	@GetMapping("/")
	public String homePage(final Model model) {
		final List<ArticleDto> articles = getArticleMapper().articleModelsToArticleDtos(getArticleService().getAllApprovedOrderedByCreationDate());
		model.addAttribute("articles", articles);
		return "index";
	}

}
