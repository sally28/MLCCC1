package org.mlccc.cm.repository;

import org.mlccc.cm.domain.DiscountCode;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DiscountCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiscountCodeRepository extends JpaRepository<DiscountCode,Long> {
    
}
