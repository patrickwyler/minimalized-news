package ch.wyler.minimalizednews.repositories;

import ch.wyler.minimalizednews.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for all tag database operations
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

	/**
	 * Get tag with specific name
	 *
	 * @return tag with a specific name
	 */
	Tag findTagByName(String name);
}