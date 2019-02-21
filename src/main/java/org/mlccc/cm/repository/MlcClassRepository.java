package org.mlccc.cm.repository;

import org.mlccc.cm.domain.MlcClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the MlcClass entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MlcClassRepository extends JpaRepository<MlcClass,Long> {
    @Query("SELECT c FROM MlcClass c WHERE c.teacher.id = (:teacherId) ")
    List<MlcClass> findAllWithTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT c FROM MlcClass c WHERE lower(c.className) like (:searchTerm) ")
    Page<MlcClass> findAllWithSearchTerm(Pageable var1, @Param("searchTerm") String searchTerm);

    @Query("SELECT c FROM MlcClass c WHERE c.mlcClassCategory.id = (:categoryId) ")
    Page<MlcClass> findAllWithCategory(Pageable var1, @Param("categoryId") Long categoryId);

    @Query("SELECT c FROM MlcClass c WHERE c.teacher.id = (:teacherId) ")
    Page<MlcClass> findAllWithTeacher(Pageable var1, @Param("teacherId") Long teacherId);
}
