package org.mlccc.cm.repository;

import org.mlccc.cm.domain.Student;
import org.mlccc.cm.domain.User;
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

    @Query("select s from Student s where lower(firstName) like (:searchTerm) or lower(lastName) like (:searchTerm) or lower(firstName || ' ' || lastName) like (:searchTerm) " +
            "or lower(lastName ||','||firstName) like (:searchTerm)")
    Page<Student> findAllWithSearchTerm(Pageable var1, @Param("searchTerm")String searchTerm);

    @EntityGraph(attributePaths = "associatedAccounts")
    @Query("select s from Student s join s.registrations r join r.mlcClass c join c.teacher t join t.account a where a.id = (:userId) and c.status.status != 'CLOSED' ")
    Page<Student> findStudentsInClassTaughtBy(Pageable var1, @Param("userId")Long userId);

}
