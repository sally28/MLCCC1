package org.mlccc.cm.repository;

import org.mlccc.cm.domain.AccountAgreement;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AccountAgreement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountAgreementRepository extends JpaRepository<AccountAgreement,Long> {

}
