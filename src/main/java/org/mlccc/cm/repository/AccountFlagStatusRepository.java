package org.mlccc.cm.repository;

import org.mlccc.cm.domain.AccountFlagStatus;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AccountFlagStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountFlagStatusRepository extends JpaRepository<AccountFlagStatus,Long> {

}
