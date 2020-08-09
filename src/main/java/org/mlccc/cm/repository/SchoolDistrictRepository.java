package org.mlccc.cm.repository;

import org.mlccc.cm.domain.SchoolDistrict;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SchoolDistrict entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchoolDistrictRepository extends JpaRepository<SchoolDistrict,Long> {

}
