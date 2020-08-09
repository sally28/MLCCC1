package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.UserAgreement;

import org.mlccc.cm.repository.UserAgreementRepository;
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
 * REST controller for managing UserAgreement.
 */
@RestController
@RequestMapping("/api")
public class UserAgreementResource {

    private final Logger log = LoggerFactory.getLogger(UserAgreementResource.class);

    private static final String ENTITY_NAME = "userAgreement";

    private final UserAgreementRepository userAgreementRepository;

    public UserAgreementResource(UserAgreementRepository userAgreementRepository) {
        this.userAgreementRepository = userAgreementRepository;
    }

    /**
     * POST  /user-agreements : Create a new userAgreement.
     *
     * @param userAgreement the userAgreement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userAgreement, or with status 400 (Bad Request) if the userAgreement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-agreements")
    @Timed
    public ResponseEntity<UserAgreement> createUserAgreement(@RequestBody UserAgreement userAgreement) throws URISyntaxException {
        log.debug("REST request to save UserAgreement : {}", userAgreement);
        if (userAgreement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new userAgreement cannot already have an ID")).body(null);
        }
        UserAgreement result = userAgreementRepository.save(userAgreement);
        return ResponseEntity.created(new URI("/api/user-agreements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-agreements : Updates an existing userAgreement.
     *
     * @param userAgreement the userAgreement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userAgreement,
     * or with status 400 (Bad Request) if the userAgreement is not valid,
     * or with status 500 (Internal Server Error) if the userAgreement couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-agreements")
    @Timed
    public ResponseEntity<UserAgreement> updateUserAgreement(@RequestBody UserAgreement userAgreement) throws URISyntaxException {
        log.debug("REST request to update UserAgreement : {}", userAgreement);
        if (userAgreement.getId() == null) {
            return createUserAgreement(userAgreement);
        }
        UserAgreement result = userAgreementRepository.save(userAgreement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userAgreement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-agreements : get all the userAgreements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userAgreements in body
     */
    @GetMapping("/user-agreements")
    @Timed
    public List<UserAgreement> getAllUserAgreements() {
        log.debug("REST request to get all UserAgreements");
        return userAgreementRepository.findAll();
    }

    /**
     * GET  /user-agreements/:id : get the "id" userAgreement.
     *
     * @param id the id of the userAgreement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userAgreement, or with status 404 (Not Found)
     */
    @GetMapping("/user-agreements/{id}")
    @Timed
    public ResponseEntity<UserAgreement> getUserAgreement(@PathVariable Long id) {
        log.debug("REST request to get UserAgreement : {}", id);
        UserAgreement userAgreement = userAgreementRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userAgreement));
    }

    /**
     * DELETE  /user-agreements/:id : delete the "id" userAgreement.
     *
     * @param id the id of the userAgreement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-agreements/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserAgreement(@PathVariable Long id) {
        log.debug("REST request to delete UserAgreement : {}", id);
        userAgreementRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
