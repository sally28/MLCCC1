package org.mlccc.cm.repository;

import org.mlccc.cm.domain.Payment;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    @Query("SELECT p FROM Payment p WHERE p.account.id = (:userId) ")
    List<Payment> findByUserId(@Param("userId") Long userId);
}
