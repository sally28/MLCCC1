package org.mlccc.cm.repository;

import org.mlccc.cm.domain.AppliedDiscount;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AppliedDiscount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppliedDiscountRepository extends JpaRepository<AppliedDiscount,Long> {
    
}
