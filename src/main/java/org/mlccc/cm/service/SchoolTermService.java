package org.mlccc.cm.service;

import org.mlccc.cm.domain.SchoolTerm;
import java.util.List;

/**
 * Service Interface for managing SchoolTerm.
 */
public interface SchoolTermService {

    /**
     * Save a schoolTerm.
     *
     * @param schoolTerm the entity to save
     * @return the persisted entity
     */
    SchoolTerm save(SchoolTerm schoolTerm);

    /**
     *  Get all the schoolTerms.
     *
     *  @return the list of entities
     */
    List<SchoolTerm> findAll();

    /**
     *  Get the "id" schoolTerm.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SchoolTerm findOne(Long id);

    /**
     *  Delete the "id" schoolTerm.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
