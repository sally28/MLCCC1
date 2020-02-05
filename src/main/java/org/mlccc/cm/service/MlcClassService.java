package org.mlccc.cm.service;

import org.mlccc.cm.domain.MlcClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing MlcClass.
 */
public interface MlcClassService {

    /**
     * Save a mlcClass.
     *
     * @param mlcClass the entity to save
     * @return the persisted entity
     */
    MlcClass save(MlcClass mlcClass);

    /**
     *  Get all the mlcClasses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<MlcClass> findAll(Pageable pageable);

    /**
     *  Get the "id" mlcClass.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MlcClass findOne(Long id);

    /**
     *  Delete the "id" mlcClass.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<MlcClass> findAllWithTeacherId(Long teacherId);

    List<MlcClass> findAllWithTeacherUserId(Long teacherUserId);

    Page<MlcClass> findAllWithSearchTerm(Pageable pageable, String searchTerm, Long categoryId, Long teacherId, Long schoolTermId);

    List<MlcClass> findAllActive();

    List<MlcClass> findAllWithSchoolTermId(Long schoolTermId);
}
