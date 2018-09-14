package org.mlccc.cm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ClassStatus.
 */
@Entity
@Table(name = "class_status")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClassStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "color_displayed")
    private String colorDisplayed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public ClassStatus status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getColorDisplayed() {
        return colorDisplayed;
    }

    public ClassStatus colorDisplayed(String colorDisplayed) {
        this.colorDisplayed = colorDisplayed;
        return this;
    }

    public void setColorDisplayed(String colorDisplayed) {
        this.colorDisplayed = colorDisplayed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassStatus classStatus = (ClassStatus) o;
        if (classStatus.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), classStatus.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClassStatus{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", colorDisplayed='" + getColorDisplayed() + "'" +
            "}";
    }
}
