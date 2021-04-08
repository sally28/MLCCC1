package org.mlccc.cm.repository;

import org.mlccc.cm.domain.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Invoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {

    @EntityGraph(attributePaths = "registrations")
    @Query("select invoice from Invoice invoice where invoice.user.login = ?#{principal.username}")
    List<Invoice> findByUserIsCurrentUser();

    @EntityGraph(attributePaths = "registrations")
    @Query("SELECT DISTINCT invoice FROM Invoice invoice WHERE invoice.user.id = (:userId) and invoice.status = 'UNPAID' ")
    List<Invoice> findUnpaidByUserId(@Param("userId") Long userId);

    @EntityGraph(attributePaths = "registrations")
    @Query("SELECT DISTINCT invoice FROM Invoice invoice WHERE invoice.user.id = (:userId) and invoice.schoolTerm.id = (:schoolTermId) and invoice.status = 'UNPAID' ")
    List<Invoice> findUnpaidByUserIdSchoolTermId(@Param("userId") Long userId, @Param("schoolTermId") Long schoolTermId);

    @EntityGraph(attributePaths = "registrations")
    @Query("SELECT DISTINCT invoice FROM Invoice invoice WHERE invoice.user.id = (:userId) and invoice.status = 'UNPAID' ")
    Page<Invoice> findUnpaidByUserId(Pageable var1, @Param("userId") Long userId);

    @EntityGraph(attributePaths = "registrations")
    @Query("SELECT DISTINCT invoice FROM Invoice invoice WHERE invoice.status = 'UNPAID' ")
    List<Invoice> findAllInvoices();

    @EntityGraph(attributePaths = "registrations")
    @Query("SELECT DISTINCT invoice FROM Invoice invoice WHERE invoice.status = 'UNPAID' ")
    Page<Invoice> findAllInvoices(Pageable var1);

    @EntityGraph(attributePaths = "registrations")
    @Query("SELECT invoice FROM Invoice invoice WHERE invoice.id = (:invoiceId) ")
    Invoice findOneWithRegistrations(@Param("invoiceId") Long invoiceId);

}
