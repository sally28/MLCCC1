package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.RegistrationStatus;
import org.mlccc.cm.service.RegistrationStatusService;
import org.mlccc.cm.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RegistrationStatus.
 */
@RestController
@RequestMapping("/api")
public class RegistrationStatusResource {

    private final Logger log = LoggerFactory.getLogger(RegistrationStatusResource.class);

    private static final String ENTITY_NAME = "registrationStatus";

    private final RegistrationStatusService registrationStatusService;

    public RegistrationStatusResource(RegistrationStatusService registrationStatusService) {
        this.registrationStatusService = registrationStatusService;
    }

    /**
     * POST  /registration-statuses : Create a new registrationStatus.
     *
     * @param registrationStatus the registrationStatus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new registrationStatus, or with status 400 (Bad Request) if the registrationStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/registration-statuses")
    @Timed
    public ResponseEntity<RegistrationStatus> createRegistrationStatus(@RequestBody RegistrationStatus registrationStatus) throws URISyntaxException {
        log.debug("REST request to save RegistrationStatus : {}", registrationStatus);
        if (registrationStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new registrationStatus cannot already have an ID")).body(null);
        }
        RegistrationStatus result = registrationStatusService.save(registrationStatus);
        return ResponseEntity.created(new URI("/api/registration-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /registration-statuses : Updates an existing registrationStatus.
     *
     * @param registrationStatus the registrationStatus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated registrationStatus,
     * or with status 400 (Bad Request) if the registrationStatus is not valid,
     * or with status 500 (Internal Server Error) if the registrationStatus couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/registration-statuses")
    @Timed
    public ResponseEntity<RegistrationStatus> updateRegistrationStatus(@RequestBody RegistrationStatus registrationStatus) throws URISyntaxException {
        log.debug("REST request to update RegistrationStatus : {}", registrationStatus);
        if (registrationStatus.getId() == null) {
            return createRegistrationStatus(registrationStatus);
        }
        RegistrationStatus result = registrationStatusService.save(registrationStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, registrationStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /registration-statuses : get all the registrationStatuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of registrationStatuses in body
     */
    @GetMapping("/registration-statuses")
    @Timed
    public List<RegistrationStatus> getAllRegistrationStatuses() {
        log.debug("REST request to get all RegistrationStatuses");
        return registrationStatusService.findAll();
    }

    /**
     * GET  /registration-statuses/:id : get the "id" registrationStatus.
     *
     * @param id the id of the registrationStatus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the registrationStatus, or with status 404 (Not Found)
     */
    @GetMapping("/registration-statuses/{id}")
    @Timed
    public ResponseEntity<RegistrationStatus> getRegistrationStatus(@PathVariable Long id) {
        log.debug("REST request to get RegistrationStatus : {}", id);
        RegistrationStatus registrationStatus = registrationStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(registrationStatus));
    }

    /**
     * DELETE  /registration-statuses/:id : delete the "id" registrationStatus.
     *
     * @param id the id of the registrationStatus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/registration-statuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteRegistrationStatus(@PathVariable Long id) {
        log.debug("REST request to delete RegistrationStatus : {}", id);
        registrationStatusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
