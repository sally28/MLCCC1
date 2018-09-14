package org.mlccc.cm.service;

import org.mlccc.cm.domain.Student;
import org.mlccc.cm.service.dto.StudentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Student.
 */
public interface StudentService {

    /**
     * Save a student.
     *
     * @param student the entity to save
     * @return the persisted entity
     */
    Student save(Student student);

    /**
     *  Get all the students.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Student> findAll(Pageable pageable);

    Page<Student> findStudentsAssociatedWith(Pageable pageable, Long userId);

    /**
     *  Get the "id" student.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Student findOne(Long id);

    Student findByIdAndFetchEager(Long id);

    /**
     *  Delete the "id" student.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    Student map(StudentDTO studentDto);
}
