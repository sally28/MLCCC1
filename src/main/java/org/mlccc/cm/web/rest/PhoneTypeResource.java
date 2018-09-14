package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.PhoneType;

import org.mlccc.cm.repository.PhoneTypeRepository;
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
 * REST controller for managing PhoneType.
 */
@RestController
@RequestMapping("/api")
public class PhoneTypeResource {

    private final Logger log = LoggerFactory.getLogger(PhoneTypeResource.class);

    private static final String ENTITY_NAME = "phoneType";

    private final PhoneTypeRepository phoneTypeRepository;

    public PhoneTypeResource(PhoneTypeRepository phoneTypeRepository) {
        this.phoneTypeRepository = phoneTypeRepository;
    }

    /**
     * POST  /phone-types : Create a new phoneType.
     *
     * @param phoneType the phoneType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new phoneType, or with status 400 (Bad Request) if the phoneType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/phone-types")
    @Timed
    public ResponseEntity<PhoneType> createPhoneType(@RequestBody PhoneType phoneType) throws URISyntaxException {
        log.debug("REST request to save PhoneType : {}", phoneType);
        if (phoneType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new phoneType cannot already have an ID")).body(null);
        }
        PhoneType result = phoneTypeRepository.save(phoneType);
        return ResponseEntity.created(new URI("/api/phone-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /phone-types : Updates an existing phoneType.
     *
     * @param phoneType the phoneType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated phoneType,
     * or with status 400 (Bad Request) if the phoneType is not valid,
     * or with status 500 (Internal Server Error) if the phoneType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/phone-types")
    @Timed
    public ResponseEntity<PhoneType> updatePhoneType(@RequestBody PhoneType phoneType) throws URISyntaxException {
        log.debug("REST request to update PhoneType : {}", phoneType);
        if (phoneType.getId() == null) {
            return createPhoneType(phoneType);
        }
        PhoneType result = phoneTypeRepository.save(phoneType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, phoneType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /phone-types : get all the phoneTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of phoneTypes in body
     */
    @GetMapping("/phone-types")
    @Timed
    public List<PhoneType> getAllPhoneTypes() {
        log.debug("REST request to get all PhoneTypes");
        return phoneTypeRepository.findAll();
    }

    /**
     * GET  /phone-types/:id : get the "id" phoneType.
     *
     * @param id the id of the phoneType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the phoneType, or with status 404 (Not Found)
     */
    @GetMapping("/phone-types/{id}")
    @Timed
    public ResponseEntity<PhoneType> getPhoneType(@PathVariable Long id) {
        log.debug("REST request to get PhoneType : {}", id);
        PhoneType phoneType = phoneTypeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(phoneType));
    }

    /**
     * DELETE  /phone-types/:id : delete the "id" phoneType.
     *
     * @param id the id of the phoneType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/phone-types/{id}")
    @Timed
    public ResponseEntity<Void> deletePhoneType(@PathVariable Long id) {
        log.debug("REST request to delete PhoneType : {}", id);
        phoneTypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
