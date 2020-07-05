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

    private LocalDate modifyDate;

    private User billToUser;

    private Set<RegistrationDTO> registrations;

    private Double multiClassDiscount = 0.00;

    private Double earlyBirdDiscount = 0.00;

    private Double credit = 0.00;

    private Double benefits = 0.00;

    private Double registrationFee = 0.00;

    private Double adjustment = 0.00;

    private Double total = 0.00;

    private String comments;

    public InvoiceDTO() {
        // Empty constructor needed for Jackson.
    }

    public InvoiceDTO(Invoice invoice) {
        this.setBillToUser(invoice.getUser());
        this.setDescription(invoice.getDescription());
        this.setId(invoice.getId());
        this.setStatus(invoice.getStatus());
        this.setInvoiceDate(invoice.getInvoiceDate());
        this.setModifyDate(invoice.getModifyDate());
        registrations = new HashSet<>();
        for(Registration r : invoice.getRegistrations()){
            RegistrationDTO dto = new RegistrationDTO();
            dto.setMlcClassName(r.getMlcClass().getClassName());
            dto.setStatus(r.getStatus());
            dto.setStudentName(r.getStudent().getFirstName() +" " + r.getStudent().getLastName());
            dto.setRegistrationFee(r.getMlcClass().getSchoolTerm().getRegistrationFee());
            dto.setTuition(r.getMlcClass().getTuition());
            dto.setClassTime(r.getMlcClass().getClassTime() != null ? r.getMlcClass().getClassTime().getClassTime() : "");
            dto.setId(r.getId());
            registrations.add(dto);
        }
        this.setBenefits(invoice.getTeacherBenefits() != null ? invoice.getTeacherBenefits() : 0.00);
        this.setCredit(invoice.getUserCredit() != null ? invoice.getUserCredit() : 0.00);
        this.setEarlyBirdDiscount(invoice.getEarlyBirdDiscount() != null ? invoice.getEarlyBirdDiscount() : 0.00);
        this.setMultiClassDiscount(invoice.getMultiClassDiscount() != null ? invoice.getMultiClassDiscount() : 0.00);
        this.setRegistrationFee(invoice.getRegistrationFee() != null ? invoice.getRegistrationFee() : 0.00);
        this.setAdjustment(invoice.getAdjustment() != null ? invoice.getAdjustment() : 0.00);
        this.setTotal(invoice.getTotal() != null ? invoice.getTotal() : 0.00);
        this.setComments(invoice.getComments());
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

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getBenefits() {
        return benefits;
    }

    public void setBenefits(Double benefits) {
        this.benefits = benefits;
    }

    public LocalDate getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(LocalDate modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Double getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(Double adjustment) {
        this.adjustment = adjustment;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
