package org.mlccc.cm.repository;

import org.mlccc.cm.domain.MlcClassCategory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MlcClassCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MlcClassCategoryRepository extends JpaRepository<MlcClassCategory,Long> {

}
