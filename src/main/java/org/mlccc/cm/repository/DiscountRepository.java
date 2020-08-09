package org.mlccc.cm.repository;

import org.mlccc.cm.domain.Discount;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Discount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiscountRepository extends JpaRepository<Discount,Long> {
    @Query("select d from Discount d where d.schoolTerm.id = (:schoolTermId) ")
    List<Discount> findAllBySchoolTerm(@Param("schoolTermId") Long schoolTermId);
}
