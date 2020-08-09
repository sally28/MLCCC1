package org.mlccc.cm.repository;

import org.mlccc.cm.domain.RegistrationStatus;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RegistrationStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegistrationStatusRepository extends JpaRepository<RegistrationStatus,Long> {

}
