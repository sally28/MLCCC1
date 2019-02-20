package org.mlccc.cm.repository;

import org.mlccc.cm.domain.MlcClass;
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
}
