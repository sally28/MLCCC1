package org.mlccc.cm.repository;

import org.mlccc.cm.domain.SchoolTerm;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the SchoolTerm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchoolTermRepository extends JpaRepository<SchoolTerm,Long> {
    @Query("SELECT st FROM SchoolTerm st order by  st.status, st.id")
    List<SchoolTerm> findAll();
}
