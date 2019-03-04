package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.*;
import org.mlccc.cm.security.AuthoritiesConstants;
import org.mlccc.cm.service.MlcClassService;
import org.mlccc.cm.service.RegistrationService;
import org.mlccc.cm.service.StudentService;
import org.mlccc.cm.service.UserService;
import org.mlccc.cm.service.dto.StudentDTO;
import org.mlccc.cm.web.rest.util.HeaderUtil;
import org.mlccc.cm.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.*;

/**
 * REST controller for managing Student.
 */
@RestController
@RequestMapping("/api")
public class StudentResource {

    private final Logger log = LoggerFactory.getLogger(StudentResource.class);

    private static final String ENTITY_NAME = "student";

    private final StudentService studentService;

    private final UserService userService;

    private final RegistrationService registrationService;

    private final MlcClassService mlcClassService;

    public StudentResource(StudentService studentService, UserService userService, RegistrationService registrationService,
                           MlcClassService mlcClassService) {
        this.studentService = studentService;
        this.userService = userService;
        this.registrationService = registrationService;
        this.mlcClassService = mlcClassService;
    }

    /**
     * POST  /students : Create a new student.
     *
     * @param student the student to create
     * @return the ResponseEntity with status 201 (Created) and with body the new student, or with status 400 (Bad Request) if the student has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/students")
    @Timed
    public ResponseEntity<Student> createStudent(@RequestBody Student student) throws URISyntaxException {
        log.debug("REST request to save Student : {}", student);
        if (student.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new student cannot already have an ID")).body(null);
        }

        // if normal(non-admin) user creating a student, automatically link user with student
        User loginUser = userService.getUserWithAuthorities();
        Set<Authority> authorities = loginUser.getAuthorities();
        Boolean normalUser = true;
        for(Authority auth : authorities){
            if(auth.getName().equals(AuthoritiesConstants.ADMIN)){
                normalUser = false;
                break;
            }
        }

        if(normalUser){
            student.getAssociatedAccounts().add(loginUser);
        }

        Student result = studentService.save(student);
        return ResponseEntity.created(new URI("/api/students/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /students : Updates an existing student.
     *
     * @param student the student to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated student,
     * or with status 400 (Bad Request) if the student is not valid,
     * or with status 500 (Internal Server Error) if the student couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/students")
    @Timed
    public ResponseEntity<Student> updateStudent(@RequestBody StudentDTO studentDto) throws URISyntaxException {
        log.debug("REST request to update Student : {}", studentDto);
        Student student = studentService.map(studentDto);
        if (student.getId() == null) {
            return createStudent(student);
        }

        Student result = studentService.save(student);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, student.getId().toString()))
            .body(result);
    }

    /**
     * GET  /students : get all the students.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of students in body
     */
    @GetMapping("/students")
    @Timed
    public ResponseEntity<List<Student>> getAllStudents(@ApiParam Pageable pageable, @ApiParam String searchTerm) {
        log.debug("REST request to get a page of Students with search: {}", searchTerm);
        User loginUser = userService.getUserWithAuthorities();
        Set<Authority> authorities = loginUser.getAuthorities();
        Boolean allStudents = false;
        Boolean isTeacher = false;
        for(Authority auth : authorities){
            if(auth.getName().equals(AuthoritiesConstants.ADMIN)){
                allStudents = true;
            } else if(auth.getName().equals(AuthoritiesConstants.TEACHER)){
                isTeacher = true;
            }
        }

        Page<Student> page = null;
        if(allStudents){
            if(StringUtils.isEmpty(searchTerm)){
                page = studentService.findAll(pageable);
            } else {
                page = studentService.findAllWithSearchTerm(pageable, searchTerm);
            }
        } else {
            if(searchTerm.equalsIgnoreCase("inClass") && isTeacher){
                // find students in the classes taught bye login user
                List<MlcClass> classes = mlcClassService.findAllWithTeacherUserId(loginUser.getId());
                page = studentService.findStudentsInClassTaughtBy(pageable, loginUser.getId());
                page.forEach(student -> {
                    List<Registration> registrations = new ArrayList<>();
                    classes.forEach(mlcClass->{
                        registrations.addAll(registrationService.findAllWithStudentIdClassId(student.getId(), mlcClass.getId()));
                    });
                    student.setRegistrations(new HashSet<>(registrations));
                });
            } else {
                // if normal user getting student list, attach registration information
                page = studentService.findStudentsAssociatedWith(pageable, loginUser.getId());
                page.forEach(student -> {
                    List<Registration> registrations =  registrationService.findAllWithStudentId(student.getId());
                    student.setRegistrations(new HashSet<>(registrations));
                });
            }
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/students");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /students/:id : get the "id" student.
     *
     * @param id the id of the student to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the student, or with status 404 (Not Found)
     */
    @GetMapping("/students/{id}")
    @Timed
    public ResponseEntity<StudentDTO> getStudent(@PathVariable Long id) {
        log.debug("REST request to get Student : {}", id);
        Student student = studentService.findByIdAndFetchEager(id);
        return new ResponseEntity<>(new StudentDTO((student)), null, HttpStatus.OK);
    }

    /**
     * DELETE  /students/:id : delete the "id" student.
     *
     * @param id the id of the student to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/students/{id}")
    @Timed
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        log.debug("REST request to delete Student : {}", id);
        studentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
