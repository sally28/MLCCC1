package org.mlccc.cm.service.impl;

import org.mlccc.cm.service.AccountFlagService;
import org.mlccc.cm.domain.AccountFlag;
import org.mlccc.cm.repository.AccountFlagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing AccountFlag.
 */
@Service
@Transactional
public class AccountFlagServiceImpl implements AccountFlagService{

    private final Logger log = LoggerFactory.getLogger(AccountFlagServiceImpl.class);

    private final AccountFlagRepository accountFlagRepository;

    public AccountFlagServiceImpl(AccountFlagRepository accountFlagRepository) {
        this.accountFlagRepository = accountFlagRepository;
    }

    /**
     * Save a accountFlag.
     *
     * @param accountFlag the entity to save
     * @return the persisted entity
     */
    @Override
    public AccountFlag save(AccountFlag accountFlag) {
        log.debug("Request to save AccountFlag : {}", accountFlag);
        return accountFlagRepository.save(accountFlag);
    }

    /**
     *  Get all the accountFlags.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AccountFlag> findAll() {
        log.debug("Request to get all AccountFlags");
        return accountFlagRepository.findAll();
    }

    /**
     *  Get one accountFlag by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AccountFlag findOne(Long id) {
        log.debug("Request to get AccountFlag : {}", id);
        return accountFlagRepository.findOne(id);
    }

    /**
     *  Delete the  accountFlag by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountFlag : {}", id);
        accountFlagRepository.delete(id);
    }
}
