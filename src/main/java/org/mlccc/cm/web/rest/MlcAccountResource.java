package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.MlcAccount;
import org.mlccc.cm.service.MlcAccountService;
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
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MlcAccount.
 */
@RestController
@RequestMapping("/api")
public class MlcAccountResource {

    private final Logger log = LoggerFactory.getLogger(MlcAccountResource.class);

    private static final String ENTITY_NAME = "mlcAccount";

    private final MlcAccountService mlcAccountService;

    public MlcAccountResource(MlcAccountService mlcAccountService) {
        this.mlcAccountService = mlcAccountService;
    }

    /**
     * POST  /mlc-accounts : Create a new mlcAccount.
     *
     * @param mlcAccount the mlcAccount to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mlcAccount, or with status 400 (Bad Request) if the mlcAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mlc-accounts")
    @Timed
    public ResponseEntity<MlcAccount> createMlcAccount(@RequestBody MlcAccount mlcAccount) throws URISyntaxException {
        log.debug("REST request to save MlcAccount : {}", mlcAccount);
        if (mlcAccount.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new mlcAccount cannot already have an ID")).body(null);
        }
        MlcAccount result = mlcAccountService.save(mlcAccount);
        return ResponseEntity.created(new URI("/api/mlc-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mlc-accounts : Updates an existing mlcAccount.
     *
     * @param mlcAccount the mlcAccount to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mlcAccount,
     * or with status 400 (Bad Request) if the mlcAccount is not valid,
     * or with status 500 (Internal Server Error) if the mlcAccount couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mlc-accounts")
    @Timed
    public ResponseEntity<MlcAccount> updateMlcAccount(@RequestBody MlcAccount mlcAccount) throws URISyntaxException {
        log.debug("REST request to update MlcAccount : {}", mlcAccount);
        if (mlcAccount.getId() == null) {
            return createMlcAccount(mlcAccount);
        }
        MlcAccount result = mlcAccountService.save(mlcAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mlcAccount.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mlc-accounts : get all the mlcAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mlcAccounts in body
     */
    @GetMapping("/mlc-accounts")
    @Timed
    public ResponseEntity<List<MlcAccount>> getAllMlcAccounts(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of MlcAccounts");
        Page<MlcAccount> page = mlcAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mlc-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mlc-accounts/:id : get the "id" mlcAccount.
     *
     * @param id the id of the mlcAccount to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mlcAccount, or with status 404 (Not Found)
     */
    @GetMapping("/mlc-accounts/{id}")
    @Timed
    public ResponseEntity<MlcAccount> getMlcAccount(@PathVariable Long id) {
        log.debug("REST request to get MlcAccount : {}", id);
        MlcAccount mlcAccount = mlcAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mlcAccount));
    }

    /**
     * DELETE  /mlc-accounts/:id : delete the "id" mlcAccount.
     *
     * @param id the id of the mlcAccount to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mlc-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteMlcAccount(@PathVariable Long id) {
        log.debug("REST request to delete MlcAccount : {}", id);
        mlcAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
