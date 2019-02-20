package org.mlccc.cm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A SchoolDistrict.
 */
@Entity
@Table(name = "school_district")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SchoolDistrict implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "county")
    private String county;

    @OneToMany(
            mappedBy = "schoolDistrict",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<User> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public SchoolDistrict name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCounty() {
        return county;
    }

    public SchoolDistrict county(String county) {
        this.county = county;
        return this;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SchoolDistrict schoolDistrict = (SchoolDistrict) o;
        if (schoolDistrict.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), schoolDistrict.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SchoolDistrict{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", county='" + getCounty() + "'" +
            "}";
    }
}
