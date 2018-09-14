package org.mlccc.cm.repository;

import org.mlccc.cm.domain.SchoolTerm;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SchoolTerm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchoolTermRepository extends JpaRepository<SchoolTerm,Long> {

}
