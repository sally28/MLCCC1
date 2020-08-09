package org.mlccc.cm.service.dto;


import org.mlccc.cm.domain.Payment;
import org.mlccc.cm.domain.User;

import java.time.LocalDate;
import java.util.Objects;


public class PaymentDTO {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Double amount;

    private String status;

    private String type;

    private String referenceId;

    private LocalDate createDate;


    private User account;

    private InvoiceDTO invoiceDto;

    private CreditCardPayment creditCard;

    public PaymentDTO() {
        // Empty constructor needed for Jackson.
    }

    public PaymentDTO amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public PaymentDTO (String status, InvoiceDTO invoiceDto, User account, Double amount) {
        this.status = status;
        this.invoiceDto = invoiceDto;
        this.account = account;
        this.amount = amount;
    }

    public Payment toPayment(){
        Payment payment = new Payment();
        if(this.id != null){
            payment.setId(this.id);
        }
        payment.setAmount(this.getAmount());
        payment.setStatus(this.getStatus());
        payment.setType(this.getType());
        payment.setAccount(this.getAccount());
        payment.setReferenceId(this.getReferenceId());
        return payment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public PaymentDTO status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public PaymentDTO type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getAccount() {
        return account;
    }

    public void setAccount(User userAccount) {
        this.account = userAccount;
    }

    public InvoiceDTO getInvoiceDto() {
        return invoiceDto;
    }

    public void setInvoiceDto(InvoiceDTO invoiceDto) {
        this.invoiceDto = invoiceDto;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public CreditCardPayment getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCardPayment creditCard) {
        this.creditCard = creditCard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PaymentDTO payment = (PaymentDTO) o;
        if (payment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), payment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentDto{" +
            "id=" + getId() +
            ", amount='" + getAmount() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
