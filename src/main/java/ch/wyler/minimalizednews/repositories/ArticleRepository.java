package ch.wyler.minimalizednews.repositories;

import ch.wyler.minimalizednews.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for all article database operations
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

	/**
	 * Get all approved articles, newest articles first
	 */
	List<Article> findArticlesByApprovedIsTrueOrderByCreationDateDesc();

	/**
	 * Get all unapproved and summarized articles, newest articles first
	 */
	List<Article> findArticlesByApprovedIsFalseAndTextIsNotNullOrderByCreationDateDesc();
}