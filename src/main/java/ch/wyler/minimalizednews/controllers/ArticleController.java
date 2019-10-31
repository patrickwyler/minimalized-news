package ch.wyler.minimalizednews.controllers;

import ch.wyler.minimalizednews.dtos.ArticleFullDto;
import ch.wyler.minimalizednews.dtos.TagDto;
import ch.wyler.minimalizednews.mappers.ArticleMapper;
import ch.wyler.minimalizednews.mappers.TagMapper;
import ch.wyler.minimalizednews.services.impl.ArticleService;
import ch.wyler.minimalizednews.services.impl.TagService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for article functions
 */
@Controller
@RequestMapping("/articles")
@Getter(value = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleController {

	ArticleService articleService;
	ArticleMapper articleMapper;
	TagService tagService;
	TagMapper tagMapper;

	@Autowired
	public ArticleController(final ArticleService articleService, final ArticleMapper articleMapper, final TagService tagService, final TagMapper tagMapper) {
		this.articleService = articleService;
		this.articleMapper = articleMapper;
		this.tagService = tagService;
		this.tagMapper = tagMapper;
	}

	@GetMapping("/all")
	public String showAll(final Model model) {
		final List<ArticleFullDto> articles = getArticleMapper().articleModelsToArticleFullDtos(getArticleService().getAll());
		model.addAttribute("articles", articles);
		return "articles/all";
	}

	@GetMapping("/create")
	public String createForm(final Model model) {
		final ArticleFullDto dto = ArticleFullDto.builder().build();
		model.addAttribute("form", dto);
		model.addAttribute("articles", getArticleMapper().articleModelsToArticleFullDtos(getArticleService().getAll()));
		return "articles/create";
	}

	@PostMapping("/create")
	public String createSubmit(@ModelAttribute final ArticleFullDto dto) {
		getArticleService().create(getArticleMapper().articleFullDtoToArticleModel(dto));
		return "redirect:/articles/all";
	}

	@GetMapping("/edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		final ArticleFullDto dto = getArticleMapper().articleModelToArticleFullDto(getArticleService().getById(id));
		final List<TagDto> tags = getTagMapper().tagModelsToTagDtos(getTagService().getAll());
		model.addAttribute("form", dto);
		model.addAttribute("tags", tags);

		final List<Long> selectedTags = new ArrayList<>();
		dto.getTags().forEach(tagDto -> selectedTags.add(tagDto.getId()));

		model.addAttribute("selectedTags", selectedTags);

		return "articles/edit";
	}

	@PostMapping("/update/{id}")
	public String update(@PathVariable("id") long id, @Valid ArticleFullDto dto, BindingResult result) {
		if (result.hasErrors()) {
			dto.setId(id);
			return "redirect:/articles/edit/" + id;
		}

		getArticleService().updateWithoutTags(getArticleMapper().articleFullDtoToArticleModel(dto));

		return "redirect:/articles/all";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") long id) {
		getArticleService().deleteById(id);
		return "redirect:/articles/all";
	}

	@GetMapping("/review")
	public String showAllReviewNeeded(final Model model) {
		final List<ArticleFullDto> articles = getArticleMapper().articleModelsToArticleFullDtos(getArticleService().getAllReviewNeeded());
		model.addAttribute("articles", articles);
		return "articles/review";
	}

	@GetMapping("/approve/{id}")
	public String approve(@PathVariable("id") long id) {
		getArticleService().approveById(id);

		return "redirect:/articles/review";
	}

	@GetMapping("/summarize/{id}")
	public String summarize(@PathVariable("id") long id) {
		getArticleService().summarizeById(id);

		return "redirect:/articles/edit/" + id;
	}
}
