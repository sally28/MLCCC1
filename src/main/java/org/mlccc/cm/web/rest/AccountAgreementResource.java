package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.AccountAgreement;

import org.mlccc.cm.repository.AccountAgreementRepository;
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
 * REST controller for managing AccountAgreement.
 */
@RestController
@RequestMapping("/api")
public class AccountAgreementResource {

    private final Logger log = LoggerFactory.getLogger(AccountAgreementResource.class);

    private static final String ENTITY_NAME = "accountAgreement";

    private final AccountAgreementRepository accountAgreementRepository;

    public AccountAgreementResource(AccountAgreementRepository accountAgreementRepository) {
        this.accountAgreementRepository = accountAgreementRepository;
    }

    /**
     * POST  /account-agreements : Create a new accountAgreement.
     *
     * @param accountAgreement the accountAgreement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountAgreement, or with status 400 (Bad Request) if the accountAgreement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/account-agreements")
    @Timed
    public ResponseEntity<AccountAgreement> createAccountAgreement(@RequestBody AccountAgreement accountAgreement) throws URISyntaxException {
        log.debug("REST request to save AccountAgreement : {}", accountAgreement);
        if (accountAgreement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new accountAgreement cannot already have an ID")).body(null);
        }
        AccountAgreement result = accountAgreementRepository.save(accountAgreement);
        return ResponseEntity.created(new URI("/api/account-agreements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /account-agreements : Updates an existing accountAgreement.
     *
     * @param accountAgreement the accountAgreement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountAgreement,
     * or with status 400 (Bad Request) if the accountAgreement is not valid,
     * or with status 500 (Internal Server Error) if the accountAgreement couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/account-agreements")
    @Timed
    public ResponseEntity<AccountAgreement> updateAccountAgreement(@RequestBody AccountAgreement accountAgreement) throws URISyntaxException {
        log.debug("REST request to update AccountAgreement : {}", accountAgreement);
        if (accountAgreement.getId() == null) {
            return createAccountAgreement(accountAgreement);
        }
        AccountAgreement result = accountAgreementRepository.save(accountAgreement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountAgreement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /account-agreements : get all the accountAgreements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of accountAgreements in body
     */
    @GetMapping("/account-agreements")
    @Timed
    public List<AccountAgreement> getAllAccountAgreements() {
        log.debug("REST request to get all AccountAgreements");
        return accountAgreementRepository.findAll();
    }

    /**
     * GET  /account-agreements/:id : get the "id" accountAgreement.
     *
     * @param id the id of the accountAgreement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountAgreement, or with status 404 (Not Found)
     */
    @GetMapping("/account-agreements/{id}")
    @Timed
    public ResponseEntity<AccountAgreement> getAccountAgreement(@PathVariable Long id) {
        log.debug("REST request to get AccountAgreement : {}", id);
        AccountAgreement accountAgreement = accountAgreementRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accountAgreement));
    }

    /**
     * DELETE  /account-agreements/:id : delete the "id" accountAgreement.
     *
     * @param id the id of the accountAgreement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/account-agreements/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountAgreement(@PathVariable Long id) {
        log.debug("REST request to delete AccountAgreement : {}", id);
        accountAgreementRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
