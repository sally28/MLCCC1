package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.AccountFlagStatus;

import org.mlccc.cm.repository.AccountFlagStatusRepository;
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
 * REST controller for managing AccountFlagStatus.
 */
@RestController
@RequestMapping("/api")
public class AccountFlagStatusResource {

    private final Logger log = LoggerFactory.getLogger(AccountFlagStatusResource.class);

    private static final String ENTITY_NAME = "accountFlagStatus";

    private final AccountFlagStatusRepository accountFlagStatusRepository;

    public AccountFlagStatusResource(AccountFlagStatusRepository accountFlagStatusRepository) {
        this.accountFlagStatusRepository = accountFlagStatusRepository;
    }

    /**
     * POST  /account-flag-statuses : Create a new accountFlagStatus.
     *
     * @param accountFlagStatus the accountFlagStatus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountFlagStatus, or with status 400 (Bad Request) if the accountFlagStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/account-flag-statuses")
    @Timed
    public ResponseEntity<AccountFlagStatus> createAccountFlagStatus(@RequestBody AccountFlagStatus accountFlagStatus) throws URISyntaxException {
        log.debug("REST request to save AccountFlagStatus : {}", accountFlagStatus);
        if (accountFlagStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new accountFlagStatus cannot already have an ID")).body(null);
        }
        AccountFlagStatus result = accountFlagStatusRepository.save(accountFlagStatus);
        return ResponseEntity.created(new URI("/api/account-flag-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /account-flag-statuses : Updates an existing accountFlagStatus.
     *
     * @param accountFlagStatus the accountFlagStatus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountFlagStatus,
     * or with status 400 (Bad Request) if the accountFlagStatus is not valid,
     * or with status 500 (Internal Server Error) if the accountFlagStatus couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/account-flag-statuses")
    @Timed
    public ResponseEntity<AccountFlagStatus> updateAccountFlagStatus(@RequestBody AccountFlagStatus accountFlagStatus) throws URISyntaxException {
        log.debug("REST request to update AccountFlagStatus : {}", accountFlagStatus);
        if (accountFlagStatus.getId() == null) {
            return createAccountFlagStatus(accountFlagStatus);
        }
        AccountFlagStatus result = accountFlagStatusRepository.save(accountFlagStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountFlagStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /account-flag-statuses : get all the accountFlagStatuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of accountFlagStatuses in body
     */
    @GetMapping("/account-flag-statuses")
    @Timed
    public List<AccountFlagStatus> getAllAccountFlagStatuses() {
        log.debug("REST request to get all AccountFlagStatuses");
        return accountFlagStatusRepository.findAll();
    }

    /**
     * GET  /account-flag-statuses/:id : get the "id" accountFlagStatus.
     *
     * @param id the id of the accountFlagStatus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountFlagStatus, or with status 404 (Not Found)
     */
    @GetMapping("/account-flag-statuses/{id}")
    @Timed
    public ResponseEntity<AccountFlagStatus> getAccountFlagStatus(@PathVariable Long id) {
        log.debug("REST request to get AccountFlagStatus : {}", id);
        AccountFlagStatus accountFlagStatus = accountFlagStatusRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accountFlagStatus));
    }

    /**
     * DELETE  /account-flag-statuses/:id : delete the "id" accountFlagStatus.
     *
     * @param id the id of the accountFlagStatus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/account-flag-statuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountFlagStatus(@PathVariable Long id) {
        log.debug("REST request to delete AccountFlagStatus : {}", id);
        accountFlagStatusRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
