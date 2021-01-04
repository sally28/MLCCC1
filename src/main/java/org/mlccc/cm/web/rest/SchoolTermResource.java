package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.config.Constants;
import org.mlccc.cm.domain.ClassStatus;
import org.mlccc.cm.domain.MlcClass;
import org.mlccc.cm.domain.SchoolTerm;
import org.mlccc.cm.service.ClassStatusService;
import org.mlccc.cm.service.MlcClassService;
import org.mlccc.cm.service.SchoolTermService;
import org.mlccc.cm.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SchoolTerm.
 */
@RestController
@RequestMapping("/api")
public class SchoolTermResource {

    private final Logger log = LoggerFactory.getLogger(SchoolTermResource.class);

    private static final String ENTITY_NAME = "schoolTerm";

    private final SchoolTermService schoolTermService;

    private final MlcClassService mlcClassService;

    private final ClassStatusService classStatusService;

    public SchoolTermResource(SchoolTermService schoolTermService, MlcClassService mlcClassService, ClassStatusService classStatusService) {
        this.schoolTermService = schoolTermService;
        this.mlcClassService = mlcClassService;
        this.classStatusService = classStatusService;
    }

    /**
     * POST  /school-terms : Create a new schoolTerm.
     *
     * @param schoolTerm the schoolTerm to create
     * @return the ResponseEntity with status 201 (Created) and with body the new schoolTerm, or with status 400 (Bad Request) if the schoolTerm has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/school-terms")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<SchoolTerm> createSchoolTerm(@RequestBody SchoolTerm schoolTerm) throws URISyntaxException {
        log.debug("REST request to save SchoolTerm : {}", schoolTerm);
        if (schoolTerm.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new schoolTerm cannot already have an ID")).body(null);
        }
        schoolTerm.setStatus("PENDING");
        SchoolTerm result = schoolTermService.save(schoolTerm);

        // copy all classes of current active school term to new school term.
        if(!schoolTerm.getTerm().endsWith("Summer Camp")){
            List<MlcClass> activeClasses = mlcClassService.findAllActive();
            ClassStatus openStatus = classStatusService.findByName("OPEN");
            for(MlcClass mlcClass : activeClasses){
                MlcClass newClass = new MlcClass(mlcClass);
                newClass.setStatus(openStatus);
                newClass.setSchoolTerm(schoolTerm);
                mlcClassService.save(newClass);
            }
        }

        return ResponseEntity.created(new URI("/api/school-terms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /school-terms : Updates an existing schoolTerm.
     *
     * @param schoolTerm the schoolTerm to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated schoolTerm,
     * or with status 400 (Bad Request) if the schoolTerm is not valid,
     * or with status 500 (Internal Server Error) if the schoolTerm couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/school-terms")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<SchoolTerm> updateSchoolTerm(@RequestBody SchoolTerm schoolTerm) throws URISyntaxException {
        log.debug("REST request to update SchoolTerm : {}", schoolTerm);
        if (schoolTerm.getId() == null) {
            return createSchoolTerm(schoolTerm);
        }
        boolean statusChanged = false;
        SchoolTerm existing = schoolTermService.findOne(schoolTerm.getId());
        if(!existing.getStatus().equals(schoolTerm.getStatus())){
            statusChanged = true;
        }
        SchoolTerm result = schoolTermService.save(schoolTerm);

        // School Term is finished, all classes must be closed.
        if(statusChanged) {
            if (schoolTerm.getStatus().equals(Constants.FINISHED_STATUS)) {
                List<MlcClass> allClasses = mlcClassService.findAllWithSchoolTermId(schoolTerm.getId());
                ClassStatus closeStatus = classStatusService.findByName(Constants.CLOSED_STATUS);
                allClasses.forEach(mlcClass -> {
                    mlcClass.setStatus(closeStatus);
                    mlcClassService.save(mlcClass);
                });
            } else if(schoolTerm.getStatus().equals(Constants.ACTIVE_STATUS)) {
                List<MlcClass> allClasses = mlcClassService.findAllWithSchoolTermId(schoolTerm.getId());
                ClassStatus openStatus = classStatusService.findByName(Constants.OPEN_STATUS);
                allClasses.forEach(mlcClass -> {
                    mlcClass.setStatus(openStatus);
                    mlcClassService.save(mlcClass);
                });
            }
        }

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, schoolTerm.getId().toString()))
            .body(result);
    }

    /**
     * GET  /school-terms : get all the schoolTerms.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of schoolTerms in body
     */
    @GetMapping("/school-terms")
    @Timed
    public List<SchoolTerm> getAllSchoolTerms() {
        log.debug("REST request to get all SchoolTerms");
        return schoolTermService.findAll();
    }

    /**
     * GET  /school-terms/:id : get the "id" schoolTerm.
     *
     * @param id the id of the schoolTerm to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the schoolTerm, or with status 404 (Not Found)
     */
    @GetMapping("/school-terms/{id}")
    @Timed
    public ResponseEntity<SchoolTerm> getSchoolTerm(@PathVariable Long id) {
        log.debug("REST request to get SchoolTerm : {}", id);
        SchoolTerm schoolTerm = schoolTermService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(schoolTerm));
    }

    /**
     * DELETE  /school-terms/:id : delete the "id" schoolTerm.
     *
     * @param id the id of the schoolTerm to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/school-terms/{id}")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteSchoolTerm(@PathVariable Long id) {
        log.debug("REST request to delete SchoolTerm : {}", id);
        // copy all classes of current active school term to new school term.
        List<MlcClass> activeClasses = mlcClassService.findAllWithSchoolTermId(id);

        for(MlcClass mlcClass : activeClasses){
            mlcClassService.delete(mlcClass.getId());
        }

        schoolTermService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
