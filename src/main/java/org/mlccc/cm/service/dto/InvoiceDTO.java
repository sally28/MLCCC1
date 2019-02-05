package org.mlccc.cm.service.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.mlccc.cm.config.Constants;
import org.mlccc.cm.domain.Authority;
import org.mlccc.cm.domain.Invoice;
import org.mlccc.cm.domain.Registration;
import org.mlccc.cm.domain.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing an invoice
 */
public class InvoiceDTO {

    private Long id;

    private String description;

    private String status;

    private LocalDate invoiceDate;

    private User billToUser;

    private Set<RegistrationDTO> registrations;

    private Double multiClassDiscount = 0.00;

    private Double earlyBirdDiscount = 0.00;

    private Double registrationFee = 0.00;

    private Double total = 0.00;

    public InvoiceDTO() {
        // Empty constructor needed for Jackson.
    }

    public InvoiceDTO(Invoice invoice) {
        this.setBillToUser(invoice.getUser());
        this.setDescription(invoice.getDescription());
        this.setId(invoice.getId());
        this.setStatus(invoice.getStatus());
        this.setInvoiceDate(invoice.getInvoiceDate());
        registrations = new HashSet<>();
        for(Registration r : invoice.getRegistrations()){
            RegistrationDTO dto = new RegistrationDTO();
            dto.setMlcClassName(r.getMlcClass().getClassName());
            dto.setStatus(r.getStatus());
            dto.setStudentName(r.getStudent().getFirstName() +" " + r.getStudent().getLastName());
            dto.setRegistrationFee(r.getMlcClass().getRegistrationFee());
            dto.setTuition(r.getMlcClass().getTuition());
            dto.setClassTime(r.getMlcClass().getClassTime().getClassTime());
            dto.setRegistrationFee(r.getMlcClass().getRegistrationFee());
            dto.setId(r.getId());
            registrations.add(dto);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public User getBillToUser() {
        return billToUser;
    }

    public void setBillToUser(User billToUser) {
        this.billToUser = billToUser;
    }

    public Set<RegistrationDTO> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(Set<RegistrationDTO> registrations) {
        this.registrations = registrations;
    }

    public Double getMultiClassDiscount() {
        return multiClassDiscount;
    }

    public void setMultiClassDiscount(Double multiClassDiscount) {
        this.multiClassDiscount = multiClassDiscount;
    }

    public Double getEarlyBirdDiscount() {
        return earlyBirdDiscount;
    }

    public void setEarlyBirdDiscount(Double earlyBirdDiscount) {
        this.earlyBirdDiscount = earlyBirdDiscount;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(Double registrationFee) {
        this.registrationFee = registrationFee;
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
            "id='" + id + '\'' +
            ", description='" + description + '\'' +
            ", billTo='" + billToUser.getLastName() + ", " + billToUser.getFirstName() +
            "}";
    }
}
