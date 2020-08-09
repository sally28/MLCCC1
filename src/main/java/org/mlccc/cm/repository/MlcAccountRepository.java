package org.mlccc.cm.repository;

import org.mlccc.cm.domain.MlcAccount;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MlcAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MlcAccountRepository extends JpaRepository<MlcAccount,Long> {

}
