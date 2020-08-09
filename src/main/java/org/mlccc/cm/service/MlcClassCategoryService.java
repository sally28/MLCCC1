package org.mlccc.cm.service;

import org.mlccc.cm.domain.MlcClassCategory;
import java.util.List;

/**
 * Service Interface for managing MlcClassCategory.
 */
public interface MlcClassCategoryService {

    /**
     * Save a mlcClassCategory.
     *
     * @param mlcClassCategory the entity to save
     * @return the persisted entity
     */
    MlcClassCategory save(MlcClassCategory mlcClassCategory);

    /**
     *  Get all the mlcClassCategories.
     *
     *  @return the list of entities
     */
    List<MlcClassCategory> findAll();

    /**
     *  Get the "id" mlcClassCategory.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MlcClassCategory findOne(Long id);

    /**
     *  Delete the "id" mlcClassCategory.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

}
