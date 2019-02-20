package org.mlccc.cm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A MlcClassCategory.
 */
@Entity
@Table(name = "mlc_class_category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MlcClassCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "mlcClassCategory")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MlcClass> mlcClasses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public MlcClassCategory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public MlcClassCategory description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<MlcClass> getMlcClasses() {
        return mlcClasses;
    }

    public MlcClassCategory mlcClasses(Set<MlcClass> mlcClasses) {
        this.mlcClasses = mlcClasses;
        return this;
    }

    public MlcClassCategory addMlcClass(MlcClass mlcClass) {
        this.mlcClasses.add(mlcClass);
        mlcClass.setMlcClassCategory(this);
        return this;
    }

    public MlcClassCategory removeMlcClass(MlcClass mlcClass) {
        this.mlcClasses.remove(mlcClass);
        mlcClass.setMlcClassCategory(null);
        return this;
    }

    public void setMlcClasses(Set<MlcClass> mlcClasses) {
        this.mlcClasses = mlcClasses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MlcClassCategory mlcClassCategory = (MlcClassCategory) o;
        if (mlcClassCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mlcClassCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MlcClassCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
