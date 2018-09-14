package org.mlccc.cm.service;

import org.mlccc.cm.domain.MlcAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing MlcAccount.
 */
public interface MlcAccountService {

    /**
     * Save a mlcAccount.
     *
     * @param mlcAccount the entity to save
     * @return the persisted entity
     */
    MlcAccount save(MlcAccount mlcAccount);

    /**
     *  Get all the mlcAccounts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<MlcAccount> findAll(Pageable pageable);

    /**
     *  Get the "id" mlcAccount.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MlcAccount findOne(Long id);

    /**
     *  Delete the "id" mlcAccount.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
