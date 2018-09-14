package org.mlccc.cm.repository;

import org.mlccc.cm.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Student entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    @Query("select s from Student s join s.associatedAccounts aa  where aa.id = (:userId) ")
    Page<Student> findStudentsAssociatedWith(Pageable var1, @Param("userId")Long userId);

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.associatedAccounts WHERE s.id = (:id)")
    public Student findByIdAndFetchEager(@Param("id") Long id);
}
