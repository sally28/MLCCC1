package org.mlccc.cm.service;

import org.mlccc.cm.domain.ClassTime;
import java.util.List;

/**
 * Service Interface for managing ClassTime.
 */
public interface ClassTimeService {

    /**
     * Save a classTime.
     *
     * @param classTime the entity to save
     * @return the persisted entity
     */
    ClassTime save(ClassTime classTime);

    /**
     *  Get all the classTimes.
     *
     *  @return the list of entities
     */
    List<ClassTime> findAll();

    /**
     *  Get the "id" classTime.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ClassTime findOne(Long id);

    /**
     *  Delete the "id" classTime.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
