package ch.wyler.minimalizednews.integrationtests.repositories;

import ch.wyler.minimalizednews.entities.Tag;
import ch.wyler.minimalizednews.repositories.TagRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@Getter(AccessLevel.PROTECTED)
@RunWith(SpringRunner.class)
@DataJpaTest
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagRepositoryIntegrationTest {

	private static final String LAENDER = "Länder";
	private static final String SCHWEIZ = "Schweiz";
	private static final String BERN = "Bern";
	private static final String ZURICH = "Zürich";
	private static final String IT = "IT";

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	TagRepository repository;

	@Before
	public void setUp() {
		// level 1
		getRepository().save(Tag.builder()
				.name(LAENDER)
				.build());

		// level 2
		getRepository().save(Tag.builder()
				.name(SCHWEIZ)
				.parentTag(getRepository().findTagByName(LAENDER))
				.build());

		getRepository().save(Tag.builder()
				.name(IT)
				.parentTag(getRepository().findTagByName(LAENDER))
				.build());

		// level 3
		getRepository().save(Tag.builder()
				.name(BERN)
				.parentTag(getRepository().findTagByName(SCHWEIZ))
				.build());

		getRepository().save(Tag.builder()
				.name(ZURICH)
				.parentTag(getRepository().findTagByName(SCHWEIZ))
				.build());

	}

	@Test
	public void testFindTagByName_correct() {
		// Act
		final Tag result = getRepository().findTagByName(BERN);

		// Assert
		assertThat("Wrong tag", result.getName(), is(BERN));
	}

	@Test
	public void testFindTagByName_notFound() {
		// Act
		final Tag result = getRepository().findTagByName("bhsdbhs");

		// Assert
		assertThat("No tag should be found", result, nullValue());
	}

	@Test
	public void testFindTagByName_empty() {
		// Act
		final Tag result = getRepository().findTagByName("");

		// Assert
		assertThat("No tag should be found", result, nullValue());
	}
}
