package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.AccountFlag;
import org.mlccc.cm.service.AccountFlagService;
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
 * REST controller for managing AccountFlag.
 */
@RestController
@RequestMapping("/api")
public class AccountFlagResource {

    private final Logger log = LoggerFactory.getLogger(AccountFlagResource.class);

    private static final String ENTITY_NAME = "accountFlag";

    private final AccountFlagService accountFlagService;

    public AccountFlagResource(AccountFlagService accountFlagService) {
        this.accountFlagService = accountFlagService;
    }

    /**
     * POST  /account-flags : Create a new accountFlag.
     *
     * @param accountFlag the accountFlag to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountFlag, or with status 400 (Bad Request) if the accountFlag has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/account-flags")
    @Timed
    public ResponseEntity<AccountFlag> createAccountFlag(@RequestBody AccountFlag accountFlag) throws URISyntaxException {
        log.debug("REST request to save AccountFlag : {}", accountFlag);
        if (accountFlag.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new accountFlag cannot already have an ID")).body(null);
        }
        AccountFlag result = accountFlagService.save(accountFlag);
        return ResponseEntity.created(new URI("/api/account-flags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /account-flags : Updates an existing accountFlag.
     *
     * @param accountFlag the accountFlag to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountFlag,
     * or with status 400 (Bad Request) if the accountFlag is not valid,
     * or with status 500 (Internal Server Error) if the accountFlag couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/account-flags")
    @Timed
    public ResponseEntity<AccountFlag> updateAccountFlag(@RequestBody AccountFlag accountFlag) throws URISyntaxException {
        log.debug("REST request to update AccountFlag : {}", accountFlag);
        if (accountFlag.getId() == null) {
            return createAccountFlag(accountFlag);
        }
        AccountFlag result = accountFlagService.save(accountFlag);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountFlag.getId().toString()))
            .body(result);
    }

    /**
     * GET  /account-flags : get all the accountFlags.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of accountFlags in body
     */
    @GetMapping("/account-flags")
    @Timed
    public List<AccountFlag> getAllAccountFlags() {
        log.debug("REST request to get all AccountFlags");
        return accountFlagService.findAll();
    }

    /**
     * GET  /account-flags/:id : get the "id" accountFlag.
     *
     * @param id the id of the accountFlag to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountFlag, or with status 404 (Not Found)
     */
    @GetMapping("/account-flags/{id}")
    @Timed
    public ResponseEntity<AccountFlag> getAccountFlag(@PathVariable Long id) {
        log.debug("REST request to get AccountFlag : {}", id);
        AccountFlag accountFlag = accountFlagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accountFlag));
    }

    /**
     * DELETE  /account-flags/:id : delete the "id" accountFlag.
     *
     * @param id the id of the accountFlag to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/account-flags/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountFlag(@PathVariable Long id) {
        log.debug("REST request to delete AccountFlag : {}", id);
        accountFlagService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
