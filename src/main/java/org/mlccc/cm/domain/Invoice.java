package org.mlccc.cm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "invoice")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Registration> registrations = new HashSet<>();

    @OneToMany(mappedBy = "invoice")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Payment> payments = new HashSet<>();

    @OneToMany(mappedBy = "invoice")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AppliedDiscount> appliedDiscounts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Invoice description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public Invoice status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public Invoice invoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public User getUser() {
        return user;
    }

    public Invoice user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Registration> getRegistrations() {
        return registrations;
    }

    public Invoice registrations(Set<Registration> registrations) {
        this.registrations = registrations;
        return this;
    }

    public Invoice addRegistration(Registration registration) {
        this.registrations.add(registration);
        registration.setInvoice(this);
        return this;
    }

    public Invoice removeRegistration(Registration registration) {
        this.registrations.remove(registration);
        registration.setInvoice(null);
        return this;
    }

    public void setRegistrations(Set<Registration> registrations) {
        this.registrations = registrations;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public Invoice payments(Set<Payment> payments) {
        this.payments = payments;
        return this;
    }

    public Invoice addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setInvoice(this);
        return this;
    }

    public Invoice removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setInvoice(null);
        return this;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    public Set<AppliedDiscount> getAppliedDiscounts() {
        return appliedDiscounts;
    }

    public Invoice appliedDiscounts(Set<AppliedDiscount> appliedDiscounts) {
        this.appliedDiscounts = appliedDiscounts;
        return this;
    }

    public Invoice addAppliedDiscount(AppliedDiscount appliedDiscount) {
        this.appliedDiscounts.add(appliedDiscount);
        appliedDiscount.setInvoice(this);
        return this;
    }

    public Invoice removeAppliedDiscount(AppliedDiscount appliedDiscount) {
        this.appliedDiscounts.remove(appliedDiscount);
        appliedDiscount.setInvoice(null);
        return this;
    }

    public void setAppliedDiscounts(Set<AppliedDiscount> appliedDiscounts) {
        this.appliedDiscounts = appliedDiscounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Invoice invoice = (Invoice) o;
        if (invoice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            "}";
    }
}
