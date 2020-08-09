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

    @Query("SELECT c FROM MlcClass c WHERE c.mlcClassCategory.id = (:categoryId) AND c.status.status = 'OPEN'")
    Page<MlcClass> findAllActiveWithCategory(Pageable var1, @Param("categoryId") Long categoryId);

    @Query("SELECT c FROM MlcClass c WHERE c.teacher.account.id = (:teacherAccountId) ")
    Page<MlcClass> findAllWithTeacherAccountId(Pageable var1, @Param("teacherAccountId") Long teacherAccountId);

    @Query("SELECT c FROM MlcClass c WHERE c.teacher.account.id = (:teacherUserId) ")
    List<MlcClass> findAllWithTeacherUserId(@Param("teacherUserId") Long teacherUserId);

    @Query("SELECT c FROM MlcClass c WHERE c.schoolTerm.status = 'ACTIVE' ORDER BY c.id")
    List<MlcClass> findAllActive();

    @Query("SELECT c FROM MlcClass c WHERE c.schoolTerm.id = (:schoolTermId) ")
    List<MlcClass> findAllWithSchoolTermId(@Param("schoolTermId") Long schoolTermId);

    @Query("SELECT c FROM MlcClass c WHERE c.schoolTerm.id = (:schoolTermId) ")
    Page<MlcClass> findAllPageWithSchoolTermId(Pageable var1, @Param("schoolTermId") Long schoolTermId);

    @Query("SELECT c FROM MlcClass c WHERE c.schoolTerm.id = (:schoolTermId) AND c.mlcClassCategory.id = (:categoryId) ")
    Page<MlcClass> findAllWithSchoolTermCategory(Pageable var1, @Param("schoolTermId") Long schoolTermId, @Param("categoryId") Long categoryId);
}
