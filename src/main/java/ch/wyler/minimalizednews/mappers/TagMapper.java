package ch.wyler.minimalizednews.mappers;

import ch.wyler.minimalizednews.dtos.TagDto;
import ch.wyler.minimalizednews.entities.Tag;
import ch.wyler.minimalizednews.models.TagModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Mapper for populating data between DTO<->Model<->Entity
 */
@Component
@Mapper(componentModel = "spring")
public interface TagMapper {

	@Mappings({
			@Mapping(target = "childTags", ignore = true)
	})
	Tag tagModelToTag(final TagModel tagModel);

	TagModel tagToTagModel(final Tag tag);

	TagModel tagDtoToTagModel(final TagDto tagDto);

	TagDto tagModelToTagDto(final TagModel tagModel);

	Set<TagModel> tagsToTagModels(final Set<Tag> tags);

	Set<TagDto> tagModelsToTagDtos(final Set<TagModel> tagModels);

	Set<Tag> tagModelsToTags(final Set<TagModel> tagModels);

	List<TagModel> tagsToTagModels(final List<Tag> tags);

	List<TagDto> tagModelsToTagDtos(final List<TagModel> tagModels);

}
