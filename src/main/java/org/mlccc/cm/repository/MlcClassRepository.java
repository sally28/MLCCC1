package org.mlccc.cm.repository;

import org.mlccc.cm.domain.MlcClass;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MlcClass entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MlcClassRepository extends JpaRepository<MlcClass,Long> {

}
