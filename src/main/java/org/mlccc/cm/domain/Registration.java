package org.mlccc.cm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Registration.
 */
@Entity
@Table(name = "registration")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Registration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "modify_date")
    private LocalDate modifyDate;

    @ManyToOne
    private Student student;

    @ManyToOne
    private MlcClass mlcClass;

    @ManyToOne
    private RegistrationStatus status;

    @ManyToOne
    private Invoice invoice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public Registration createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getModifyDate() {
        return modifyDate;
    }

    public Registration modifyDate(LocalDate modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(LocalDate modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Student getStudent() {
        return student;
    }

    public Registration student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public MlcClass getMlcClass() {
        return mlcClass;
    }

    public Registration mlcClass(MlcClass mlcClass) {
        this.mlcClass = mlcClass;
        return this;
    }

    public void setMlcClass(MlcClass mlcClass) {
        this.mlcClass = mlcClass;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public Registration status(RegistrationStatus registrationStatus) {
        this.status = registrationStatus;
        return this;
    }

    public void setStatus(RegistrationStatus registrationStatus) {
        this.status = registrationStatus;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public Registration invoice(Invoice invoice) {
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
        Registration registration = (Registration) o;
        if (registration.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), registration.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Registration{" +
            "id=" + getId() +
            ", createDate='" + getCreateDate() + "'" +
            ", modifyDate='" + getModifyDate() + "'" +
            "}";
    }
}
