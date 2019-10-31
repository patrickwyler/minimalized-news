package ch.wyler.minimalizednews.controllers;

import ch.wyler.minimalizednews.dtos.ArticleFullDto;
import ch.wyler.minimalizednews.dtos.TagDto;
import ch.wyler.minimalizednews.dtos.TagEdgeDto;
import ch.wyler.minimalizednews.dtos.TagNodeDto;
import ch.wyler.minimalizednews.mappers.ArticleMapper;
import ch.wyler.minimalizednews.mappers.TagMapper;
import ch.wyler.minimalizednews.services.impl.ArticleService;
import ch.wyler.minimalizednews.services.impl.TagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Set;

/**
 * Controller for tag functions
 */
@Controller
@RequestMapping("/tags")
@Getter(value = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagController {

	private static final String RED = "#dc3545";
	private static final String WHITE = "#f8f9fa";
	private static final String HEXAGON = "hexagon";
	private static final String DOT = "dot";
	private static final int SIZE_ROOT = 23;
	private static final int SIZE_STANDARD = 10;
	private static final long ROOT_NODE_ID = 99999999L;

	TagService tagService;
	TagMapper tagMapper;
	ArticleService articleService;
	ArticleMapper articleMapper;
	ObjectMapper objectMapper;

	@Autowired
	public TagController(final TagService tagService, final TagMapper tagMapper, final ArticleService articleService, final ArticleMapper articleMapper, final ObjectMapper objectMapper) {
		this.tagService = tagService;
		this.tagMapper = tagMapper;
		this.articleService = articleService;
		this.articleMapper = articleMapper;
		this.objectMapper = objectMapper;
	}

	@GetMapping("/all")
	public String showAll(final Model model) {
		final List<TagDto> tags = getTagMapper().tagModelsToTagDtos(getTagService().getAll());
		model.addAttribute("tags", tags);
		return "tags/all";
	}

	@GetMapping("/create")
	public String createForm(final Model model) {
		final TagDto dto = TagDto.builder().build();

		model.addAttribute("form", dto);
		model.addAttribute("tags", getTagMapper().tagModelsToTagDtos(getTagService().getAll()));

		return "tags/create";
	}

	@PostMapping("/create")
	public String createSubmit(@ModelAttribute final TagDto dto) {
		getTagService().create(getTagMapper().tagDtoToTagModel(dto));
		return "redirect:/tags/all";
	}

	@GetMapping("/edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		final TagDto dto = getTagMapper().tagModelToTagDto(getTagService().getById(id));

		model.addAttribute("form", dto);

		return "tags/edit";
	}

	@PostMapping("/update/{id}")
	public String update(@PathVariable("id") long id, @Valid TagDto dto, BindingResult result) {
		if (result.hasErrors()) {
			dto.setId(id);
			return "redirect:/tags/edit/" + id;
		}

		getTagService().update(getTagMapper().tagDtoToTagModel(dto));

		return "redirect:/tags/all";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") long id) {
		getTagService().deleteById(id);
		return "redirect:/tags/all";
	}

	@GetMapping("/{actionType}/{tagId}/article/{articleId}")
	public String addTagToArticle(@PathVariable("actionType") String actionType, @PathVariable("tagId") long tagId, @PathVariable("articleId") long articleId) {
		final TagDto tagDto = getTagMapper().tagModelToTagDto(getTagService().getById(tagId));
		final ArticleFullDto articleDto = getArticleMapper().articleModelToArticleFullDto(getArticleService().getById(articleId));

		// add/remove tag
		final Set<TagDto> tags = articleDto.getTags();
		if (StringUtils.equalsIgnoreCase("add", actionType)) {
			tags.add(tagDto);
		} else if (StringUtils.equalsIgnoreCase("remove", actionType)) {
			tags.remove(tagDto);
		} else {
			throw new IllegalArgumentException("Action type not supported.");
		}

		articleDto.setTags(tags);

		// update
		getArticleService().update(getArticleMapper().articleFullDtoToArticleModel(articleDto));

		return "redirect:/articles/edit/" + articleId;
	}

	/**
	 * Create tag network
	 */
	@GetMapping("/network")
	public String createNetwork(final Model model) throws JsonProcessingException {
		final List<TagDto> tags = getTagMapper().tagModelsToTagDtos(getTagService().getAllUsedTags());
		final List<TagNodeDto> nodeDtos = new ArrayList<>();
		final List<TagEdgeDto> edgeDtos = new ArrayList<>();

		nodeDtos.add(TagNodeDto.builder()
				.id(ROOT_NODE_ID)
				.label("Tags")
				.size(SIZE_ROOT)
				.shape(HEXAGON)
				.color(RED)
				.font("18px arial " + WHITE)
				.build());

		tags.forEach(tagDto -> {
			nodeDtos.add(TagNodeDto.builder()
					.id(tagDto.getId())
					.label(tagDto.getName())
					.size(SIZE_STANDARD)
					.shape(DOT)
					.color(RED)
					.font("14px arial " + WHITE)
					.build());
			edgeDtos.add(TagEdgeDto.builder()
					.from(tagDto.getParentTag() == null ? ROOT_NODE_ID : tagDto.getParentTag().getId())
					.to(tagDto.getId())
					.length(15)
					.build());
		});

		model.addAttribute("nodesArray", getObjectMapper().writeValueAsString(nodeDtos.toArray()));
		model.addAttribute("edgesArray", getObjectMapper().writeValueAsString(edgeDtos.toArray()));
		return "tags/network";
	}
}
