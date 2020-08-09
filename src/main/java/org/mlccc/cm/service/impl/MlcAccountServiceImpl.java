package org.mlccc.cm.service.impl;

import org.mlccc.cm.service.MlcAccountService;
import org.mlccc.cm.domain.MlcAccount;
import org.mlccc.cm.repository.MlcAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing MlcAccount.
 */
@Service
@Transactional
public class MlcAccountServiceImpl implements MlcAccountService{

    private final Logger log = LoggerFactory.getLogger(MlcAccountServiceImpl.class);

    private final MlcAccountRepository mlcAccountRepository;

    public MlcAccountServiceImpl(MlcAccountRepository mlcAccountRepository) {
        this.mlcAccountRepository = mlcAccountRepository;
    }

    /**
     * Save a mlcAccount.
     *
     * @param mlcAccount the entity to save
     * @return the persisted entity
     */
    @Override
    public MlcAccount save(MlcAccount mlcAccount) {
        log.debug("Request to save MlcAccount : {}", mlcAccount);
        return mlcAccountRepository.save(mlcAccount);
    }

    /**
     *  Get all the mlcAccounts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MlcAccount> findAll(Pageable pageable) {
        log.debug("Request to get all MlcAccounts");
        return mlcAccountRepository.findAll(pageable);
    }

    /**
     *  Get one mlcAccount by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MlcAccount findOne(Long id) {
        log.debug("Request to get MlcAccount : {}", id);
        return mlcAccountRepository.findOne(id);
    }

    /**
     *  Delete the  mlcAccount by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MlcAccount : {}", id);
        mlcAccountRepository.delete(id);
    }
}
