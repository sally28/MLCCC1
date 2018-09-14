package org.mlccc.cm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A AccountAgreement.
 */
@Entity
@Table(name = "account_agreement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AccountAgreement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "create_date")
    private LocalDate createDate;

    @OneToOne
    @JoinColumn(unique = true)
    private MlcAccount account;

    @OneToOne
    @JoinColumn(unique = true)
    private UserAgreement agreement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public AccountAgreement createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public MlcAccount getAccount() {
        return account;
    }

    public AccountAgreement account(MlcAccount mlcAccount) {
        this.account = mlcAccount;
        return this;
    }

    public void setAccount(MlcAccount mlcAccount) {
        this.account = mlcAccount;
    }

    public UserAgreement getAgreement() {
        return agreement;
    }

    public AccountAgreement agreement(UserAgreement userAgreement) {
        this.agreement = userAgreement;
        return this;
    }

    public void setAgreement(UserAgreement userAgreement) {
        this.agreement = userAgreement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountAgreement accountAgreement = (AccountAgreement) o;
        if (accountAgreement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountAgreement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountAgreement{" +
            "id=" + getId() +
            ", createDate='" + getCreateDate() + "'" +
            "}";
    }
}
