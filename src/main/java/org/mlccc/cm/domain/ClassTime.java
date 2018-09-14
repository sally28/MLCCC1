package org.mlccc.cm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ClassTime.
 */
@Entity
@Table(name = "class_time")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClassTime implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "class_time")
    private String classTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassTime() {
        return classTime;
    }

    public ClassTime classTime(String classTime) {
        this.classTime = classTime;
        return this;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassTime classTime = (ClassTime) o;
        if (classTime.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), classTime.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClassTime{" +
            "id=" + getId() +
            ", classTime='" + getClassTime() + "'" +
            "}";
    }
}
