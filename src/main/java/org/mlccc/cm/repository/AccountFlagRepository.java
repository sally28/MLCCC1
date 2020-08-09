package org.mlccc.cm.repository;

import org.mlccc.cm.domain.AccountFlag;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AccountFlag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountFlagRepository extends JpaRepository<AccountFlag,Long> {

}
