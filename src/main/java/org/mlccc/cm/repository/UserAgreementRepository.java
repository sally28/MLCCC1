package org.mlccc.cm.repository;

import org.mlccc.cm.domain.UserAgreement;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the UserAgreement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAgreementRepository extends JpaRepository<UserAgreement,Long> {

}
