package org.mlccc.cm.repository;

import org.mlccc.cm.domain.ClassTime;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ClassTime entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassTimeRepository extends JpaRepository<ClassTime,Long> {

}
