package ch.wyler.minimalizednews.repositories.helper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Search criteria for a specific field of an entity
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchCriteria {

	private String fieldName;
	private SearchCriteriaOperation operation;
	private Object searchText;
	private String joinTable;

	/**
	 * Constructor for standard search criterias
	 *
	 * @param fieldName  field name of the entity
	 * @param operation  search operation
	 * @param searchText search text
	 */
	public SearchCriteria(final String fieldName, final SearchCriteriaOperation operation, final Object searchText) {
		this.fieldName = fieldName;
		this.operation = operation;
		this.searchText = searchText;
	}

	/**
	 * Constructor for join search criterias
	 *
	 * @param joinTable  table which should be joined
	 * @param fieldName  field name of the entity
	 * @param operation  search operation
	 * @param searchText search text
	 */
	public SearchCriteria(final String joinTable, final String fieldName, final SearchCriteriaOperation operation, final Object searchText) {
		this.joinTable = joinTable;
		this.fieldName = fieldName;
		this.operation = operation;
		this.searchText = searchText;
	}
}