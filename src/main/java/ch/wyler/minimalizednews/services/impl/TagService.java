package ch.wyler.minimalizednews.services.impl;

import ch.wyler.minimalizednews.entities.Tag;
import ch.wyler.minimalizednews.mappers.TagMapper;
import ch.wyler.minimalizednews.models.TagModel;
import ch.wyler.minimalizednews.repositories.TagRepository;
import ch.wyler.minimalizednews.services.CrudService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter(value = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagService implements CrudService<TagModel> {

	TagRepository tagRepository;
	TagMapper tagMapper;

	@Autowired
	public TagService(final TagRepository tagRepository, final TagMapper tagMapper) {
		this.tagRepository = tagRepository;
		this.tagMapper = tagMapper;
	}

	@Override
	public List<TagModel> getAll() {
		return getTagMapper().tagsToTagModels(getTagRepository().findAll());
	}

	@Override
	public TagModel create(final TagModel tagModel) {
		final Tag tag = getTagRepository().save(getTagMapper().tagModelToTag(tagModel));
		return getTagMapper().tagToTagModel(tag);
	}

	@Override
	public TagModel update(final TagModel tagModel) {
		getById(tagModel.getId());

		final Tag tag = getTagRepository().save(getTagMapper().tagModelToTag(tagModel));
		return getTagMapper().tagToTagModel(tag);
	}

	@Override
	public void deleteById(final long id) {
		getTagRepository().deleteById(id);
	}

	@Override
	public TagModel getById(final long id) {
		final Tag tag = getTagRepository().findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id:" + id));
		return getTagMapper().tagToTagModel(tag);
	}

	public List<TagModel> getAllUsedTags() {
		return getTagMapper().tagsToTagModels(getTagRepository().findAll());
	}

}
