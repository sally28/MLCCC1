package org.mlccc.cm.repository;

import org.mlccc.cm.domain.Invoice;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Invoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {

    @Query("select invoice from Invoice invoice where invoice.user.login = ?#{principal.username}")
    List<Invoice> findByUserIsCurrentUser();
    
}