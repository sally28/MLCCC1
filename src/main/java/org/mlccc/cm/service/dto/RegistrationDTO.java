package org.mlccc.cm.service.dto;

import org.mlccc.cm.domain.Invoice;
import org.mlccc.cm.domain.Registration;
import org.mlccc.cm.domain.User;

import java.time.LocalDate;
import java.util.Set;

/**
 * A DTO representing an invoice
 */
public class RegistrationDTO {

    private long id;

    private String status;

    private LocalDate createDate;

    private LocalDate modifyDate;

    private String studentName;

    private String mlcClassName;

    private Double tuition;

    private Double registrationFee;

    private String classTime;

    private String teacher;


    public RegistrationDTO() {
        // Empty constructor needed for Jackson.
    }

    public RegistrationDTO(Registration registration) {
        this.id = registration.getId();
        this.createDate = registration.getCreateDate();
        this.modifyDate = registration.getModifyDate();
        this.studentName = registration.getStudent().getFirstName() + " " + registration.getStudent().getLastName();
        this.mlcClassName = registration.getMlcClass().getClassName();
        this.tuition = registration.getMlcClass().getTuition();
        this.registrationFee = registration.getMlcClass().getRegistrationFee();
        this.classTime = (registration.getMlcClass().getClassTime() != null)? registration.getMlcClass().getClassTime().getClassTime() : null;
        this.status = registration.getStatus();
        this.teacher = registration.getMlcClass().getTeacher().getFirstName() + " " + registration.getMlcClass().getTeacher().getLastName();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(LocalDate modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getMlcClassName() {
        return mlcClassName;
    }

    public void setMlcClassName(String mlcClassName) {
        this.mlcClassName = mlcClassName;
    }

    public void setTuition(Double tuition) {
        this.tuition = tuition;
    }

    public void setRegistrationFee(Double registrationFee) {
        this.registrationFee = registrationFee;
    }

    public Double getTuition() {
        return tuition;
    }

    public Double getRegistrationFee() {
        return registrationFee;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "RegistrationDTO{" +
            ", studentName='" + studentName + '\'' +
            ", mlcClassName='" + mlcClassName +
            "}";
    }
}
