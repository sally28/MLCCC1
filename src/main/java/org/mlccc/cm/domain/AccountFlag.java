package org.mlccc.cm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AccountFlag.
 */
@Entity
@Table(name = "account_flag")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AccountFlag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "flag_type")
    private String flagType;

    @Column(name = "related_key")
    private Integer relatedKey;

    @ManyToOne
    private MlcAccount account;

    @ManyToOne
    private AccountFlagStatus flagStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlagType() {
        return flagType;
    }

    public AccountFlag flagType(String flagType) {
        this.flagType = flagType;
        return this;
    }

    public void setFlagType(String flagType) {
        this.flagType = flagType;
    }

    public Integer getRelatedKey() {
        return relatedKey;
    }

    public AccountFlag relatedKey(Integer relatedKey) {
        this.relatedKey = relatedKey;
        return this;
    }

    public void setRelatedKey(Integer relatedKey) {
        this.relatedKey = relatedKey;
    }

    public MlcAccount getAccount() {
        return account;
    }

    public AccountFlag account(MlcAccount mlcAccount) {
        this.account = mlcAccount;
        return this;
    }

    public void setAccount(MlcAccount mlcAccount) {
        this.account = mlcAccount;
    }

    public AccountFlagStatus getFlagStatus() {
        return flagStatus;
    }

    public AccountFlag flagStatus(AccountFlagStatus accountFlagStatus) {
        this.flagStatus = accountFlagStatus;
        return this;
    }

    public void setFlagStatus(AccountFlagStatus accountFlagStatus) {
        this.flagStatus = accountFlagStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountFlag accountFlag = (AccountFlag) o;
        if (accountFlag.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountFlag.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountFlag{" +
            "id=" + getId() +
            ", flagType='" + getFlagType() + "'" +
            ", relatedKey='" + getRelatedKey() + "'" +
            "}";
    }
}
