package ch.wyler.minimalizednews.mappers;

import ch.wyler.minimalizednews.dtos.ArticleDto;
import ch.wyler.minimalizednews.dtos.ArticleFullDto;
import ch.wyler.minimalizednews.entities.Article;
import ch.wyler.minimalizednews.models.ArticleModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Mapper for populating data between DTO<->Model<->Entity
 */
@Component
@Mapper(componentModel = "spring", uses = {TagMapper.class})
public interface ArticleMapper {

	Article articleModelToArticle(final ArticleModel articleModel);

	ArticleModel articleToArticleModel(final Article article);

	ArticleModel articleDtoToArticleModel(final ArticleDto articleDto);

	ArticleModel articleFullDtoToArticleModel(final ArticleFullDto articleFullDto);

	ArticleDto articleModelToArticleDto(final ArticleModel articleModel);

	ArticleFullDto articleModelToArticleFullDto(final ArticleModel articleModel);

	Set<ArticleModel> articlesToArticleModels(final Set<Article> articles);

	List<ArticleModel> articlesToArticleModels(final List<Article> articles);

	List<ArticleDto> articleModelsToArticleDtos(final List<ArticleModel> articleModels);

	List<ArticleFullDto> articleModelsToArticleFullDtos(final List<ArticleModel> articleModels);
}
