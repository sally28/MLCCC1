package org.mlccc.cm.service.impl;

import org.mlccc.cm.domain.Registration;
import org.mlccc.cm.domain.User;
import org.mlccc.cm.repository.UserRepository;
import org.mlccc.cm.service.StudentService;
import org.mlccc.cm.domain.Student;
import org.mlccc.cm.repository.StudentRepository;
import org.mlccc.cm.service.dto.StudentDTO;
import org.mlccc.cm.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service Implementation for managing Student.
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService{

    private final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;

    private final UserRepository userRepository;

    public StudentServiceImpl(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a student.
     *
     * @param student the entity to save
     * @return the persisted entity
     */
    @Override
    public Student save(Student student) {
        log.debug("Request to save Student : {}", student);
        return studentRepository.save(student);
    }

    /**
     *  Get all the students.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Student> findAll(Pageable pageable) {
        log.debug("Request to get all Students");
        return studentRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Student> findStudentsAssociatedWith(Pageable pageable, Long userId) {
        log.debug("Request to get all Students associated with account");
        return studentRepository.findStudentsAssociatedWith(pageable, userId);
    }

    /**
     *  Get one student by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Student findOne(Long id) {
        log.debug("Request to get Student : {}", id);
        return studentRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Student findByIdAndFetchEager(Long id) {
        log.debug("Request to get Student and associatedAccounts : {}", id);
        return studentRepository.findByIdAndFetchEager(id);
    }

    /**
     *  Delete the  student by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Student : {}", id);
        studentRepository.delete(id);
    }

    @Override
    public Student map(StudentDTO studentDto) {
        Student student = new Student();
        student.setBirthMonth(studentDto.getBirthMonth());
        student.setBirthYear(studentDto.getBirthYear());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setGender(studentDto.getGender());
        student.setId(studentDto.getId());
        student.setNotes(studentDto.getNotes());

        Set<User> associatedAccounts = new HashSet<>();
        for(UserDTO userDto : studentDto.getAssociatedAccounts()){
            User user = userRepository.findOne(userDto.getId());
            associatedAccounts.add(user);
        }
        student.setAssociatedAccounts(associatedAccounts);
        return student;
    }

    @Override
    public StudentDTO map(Student student, Set<String> fields) {
        StudentDTO studentDto = new StudentDTO();
        studentDto.setBirthMonth(student.getBirthMonth());
        studentDto.setBirthYear(student.getBirthYear());
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setGender(student.getGender());
        studentDto.setId(student.getId());
        studentDto.setNotes(student.getNotes());

        if(fields.contains("associatedAccounts")){
            Set<UserDTO> associatedAccounts = new HashSet<>();
            for(User user : student.getAssociatedAccounts()){
                UserDTO userDto = new UserDTO(user);
                associatedAccounts.add(userDto);
            }
            studentDto.setAssociatedAccounts(associatedAccounts);
        }

        if(fields.contains("classesTaken")){
            Set<String> classesTaken = new HashSet<>();
            for(Registration registration : student.getRegistrations()){
                classesTaken.add(registration.getMlcClass().getClassName());
            }
            studentDto.setClassesTaken(classesTaken);
        }

        return studentDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Student> findAllWithSearchTerm(Pageable pageable, String searchTerm) {
        log.debug("Request to get all Students with searchTerm: {}", searchTerm);
        return studentRepository.findAllWithSearchTerm(pageable, searchTerm.toLowerCase());
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Student> findStudentsInClassTaughtBy(Pageable pageable, Long userId) {
        log.debug("Request to get all Students in classes taught by {}", userId);
        return studentRepository.findStudentsInClassTaughtBy(pageable, userId);
    }
}
