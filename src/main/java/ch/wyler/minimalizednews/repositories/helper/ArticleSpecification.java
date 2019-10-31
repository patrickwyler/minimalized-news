package ch.wyler.minimalizednews.repositories.helper;

import ch.wyler.minimalizednews.entities.Article;
import ch.wyler.minimalizednews.entities.Tag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static ch.wyler.minimalizednews.repositories.helper.SearchCriteriaOperation.LIKE;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSpecification implements Specification<Article> {

	private static final String LIKE_FUNCTION = "%";
	private static final String ATTRIBUTE_ID = "id";
	private SearchCriteria criteria;

	/**
	 * Generically generate predicate depending on search criteria
	 *
	 * @param root    entity
	 * @param query   criteria query
	 * @param builder criteria builder
	 * @return predicate generated out of search criteria
	 */
	@Override
	public Predicate toPredicate(final Root<Article> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
		checkNotNull(criteria, "Criteria is null");
		checkNotNull(criteria.getOperation(), "Criteria.operation is null");
		checkNotNull(criteria.getFieldName(), "Criteria.fieldname is null");

		// otherwise entries could be returned more than one time in the result
		query.distinct(true);

		// check for join
		if (isNotBlank(criteria.getJoinTable())) {
			final Join<Article, Tag> owners = root.join(criteria.getJoinTable(), JoinType.LEFT);
			query.orderBy(builder.desc(root.get(ATTRIBUTE_ID)));

			if (criteria.getOperation().equals(LIKE)) {
				return builder.like(owners.get(criteria.getFieldName()), LIKE_FUNCTION + criteria.getSearchText().toString() + LIKE_FUNCTION);
			}
			return builder.equal(owners.get(criteria.getFieldName()), criteria.getSearchText());
		}

		// check for other operations
		switch (criteria.getOperation()) {
			case GREATER_THAN:
				return builder.greaterThanOrEqualTo(root.get(criteria.getFieldName()), criteria.getSearchText().toString());
			case LESS_THAN:
				return builder.lessThanOrEqualTo(root.get(criteria.getFieldName()), criteria.getSearchText().toString());
			case LIKE:
				return builder.like(root.get(criteria.getFieldName()), LIKE_FUNCTION + criteria.getSearchText().toString() + LIKE_FUNCTION);
			case EQUALS:
				return builder.equal(root.get(criteria.getFieldName()), criteria.getSearchText());
			default:
				log.warn("Operation not found! Operation:" + criteria.getOperation());
				return null;
		}
	}
}