package ch.wyler.minimalizednews.services;

import java.util.List;

/**
 * Interface for all basic CRUD operations
 */
public interface CrudService<MODEL> {

	/**
	 * Get all models
	 *
	 * @return List of models
	 */
	List<MODEL> getAll();

	/**
	 * Create model
	 *
	 * @return model
	 */
	MODEL create(final MODEL model);

	/**
	 * Update model
	 *
	 * @param model
	 * @return model
	 */
	MODEL update(final MODEL model);

	/**
	 * Delete model by id
	 *
	 * @param id id of model
	 */
	void deleteById(final long id);

	/**
	 * Get model by id
	 *
	 * @param id id of model
	 * @return model
	 */
	MODEL getById(final long id);
}