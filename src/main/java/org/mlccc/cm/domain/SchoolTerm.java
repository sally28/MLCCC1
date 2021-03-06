package org.mlccc.cm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/**
 * A SchoolTerm.
 */
@Entity
@Table(name = "school_term")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SchoolTerm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "term")
    private String term;

    @Column(name = "status")
    private String status;

    @Column(name = "register")
    private Boolean register;

    @Column(name = "prom_date")
    private LocalDate promDate;

    @Column(name = "early_bird_date")
    private LocalDate earlyBirdDate;

    @Column(name = "registration_fee")
    private Double registrationFee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public SchoolTerm term(String term) {
        this.term = term;
        return this;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getStatus() {
        return status;
    }

    public SchoolTerm status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean isRegister() {
        return register;
    }

    public SchoolTerm register(Boolean register) {
        this.register = register;
        return this;
    }

    public void setRegister(Boolean register) {
        this.register = register;
    }

    public LocalDate getPromDate() {
        return promDate;
    }

    public SchoolTerm promDate(LocalDate promDate) {
        this.promDate = promDate;
        return this;
    }

    public void setPromDate(LocalDate promDate) {
        this.promDate = promDate;
    }

    public LocalDate getEarlyBirdDate() {
        return earlyBirdDate;
    }

    public SchoolTerm earlyBirdDate(LocalDate earlyBirdDate) {
        this.earlyBirdDate = earlyBirdDate;
        return this;
    }

    public void setEarlyBirdDate(LocalDate earlyBirdDate) {
        this.earlyBirdDate = earlyBirdDate;
    }

    public Double getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(Double registrationFee) {
        this.registrationFee = registrationFee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SchoolTerm schoolTerm = (SchoolTerm) o;
        if (schoolTerm.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), schoolTerm.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SchoolTerm{" +
            "id=" + getId() +
            ", term='" + getTerm() + "'" +
            ", status='" + getStatus() + "'" +
            ", register='" + isRegister() + "'" +
            ", registrationFee='" + getRegistrationFee() + "'" +
            ", promDate='" + getPromDate() + "'" +
            ", earlyBirdDate='" + getEarlyBirdDate() + "'" +
            "}";
    }
}
