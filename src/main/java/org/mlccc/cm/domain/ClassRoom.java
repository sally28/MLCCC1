package org.mlccc.cm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ClassRoom.
 */
@Entity
@Table(name = "class_room")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClassRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "room_number")
    private Integer roomNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "project_available")
    private Boolean projectAvailable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public ClassRoom roomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
        return this;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getDescription() {
        return description;
    }

    public ClassRoom description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isProjectAvailable() {
        return projectAvailable;
    }

    public ClassRoom projectAvailable(Boolean projectAvailable) {
        this.projectAvailable = projectAvailable;
        return this;
    }

    public void setProjectAvailable(Boolean projectAvailable) {
        this.projectAvailable = projectAvailable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassRoom classRoom = (ClassRoom) o;
        if (classRoom.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), classRoom.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClassRoom{" +
            "id=" + getId() +
            ", roomNumber='" + getRoomNumber() + "'" +
            ", description='" + getDescription() + "'" +
            ", projectAvailable='" + isProjectAvailable() + "'" +
            "}";
    }
}
