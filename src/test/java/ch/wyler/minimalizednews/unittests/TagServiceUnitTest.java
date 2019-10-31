package ch.wyler.minimalizednews.unittests;

import ch.wyler.minimalizednews.entities.Tag;
import ch.wyler.minimalizednews.mappers.TagMapper;
import ch.wyler.minimalizednews.repositories.TagRepository;
import ch.wyler.minimalizednews.services.impl.TagService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Getter(AccessLevel.PROTECTED)
@RunWith(MockitoJUnitRunner.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagServiceUnitTest {

	Tag TAG;

	@InjectMocks
	TagService service;

	@Mock
	TagRepository repository;

	@Mock
	TagMapper mapper;

	@Before
	public void setUp() {
		TAG = Tag.builder()
				.id(1L)
				.name("Bern")
				.build();
	}

	@Test
	public void testGetAllTags() {
		// Arrange
		final List<Tag> results = new ArrayList<>();
		results.add(TAG);
		when(getRepository().findAll()).thenReturn(results);

		// Act
		getService().getAll();

		// Assert
		verify(getMapper(), times(1)).tagsToTagModels(results);
	}

	@Test
	public void testGetAllTags_noResult() {
		// Arrange
		final List<Tag> results = new ArrayList<>();
		when(getRepository().findAll()).thenReturn(results);

		// Act
		getService().getAll();

		// Assert
		verify(getMapper(), times(1)).tagsToTagModels(results);
	}

}