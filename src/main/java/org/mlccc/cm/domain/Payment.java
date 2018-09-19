package org.mlccc.cm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "status")
    private String status;

    @Column(name = "jhi_type")
    private String type;

    @ManyToOne
    private MlcAccount account;

    @ManyToOne
    private Registration registration;

    @ManyToOne
    private Invoice invoice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public Payment amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public Payment status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public Payment type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MlcAccount getAccount() {
        return account;
    }

    public Payment account(MlcAccount mlcAccount) {
        this.account = mlcAccount;
        return this;
    }

    public void setAccount(MlcAccount mlcAccount) {
        this.account = mlcAccount;
    }

    public Registration getRegistration() {
        return registration;
    }

    public Payment registration(Registration registration) {
        this.registration = registration;
        return this;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public Payment invoice(Invoice invoice) {
        this.invoice = invoice;
        return this;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Payment payment = (Payment) o;
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
        return "Payment{" +
            "id=" + getId() +
            ", amount='" + getAmount() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
