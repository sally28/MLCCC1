package org.mlccc.cm.repository;

import org.mlccc.cm.domain.PhoneType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PhoneType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhoneTypeRepository extends JpaRepository<PhoneType,Long> {

}
