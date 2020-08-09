package org.mlccc.cm.service;

import org.mlccc.cm.domain.AccountFlag;
import java.util.List;

/**
 * Service Interface for managing AccountFlag.
 */
public interface AccountFlagService {

    /**
     * Save a accountFlag.
     *
     * @param accountFlag the entity to save
     * @return the persisted entity
     */
    AccountFlag save(AccountFlag accountFlag);

    /**
     *  Get all the accountFlags.
     *
     *  @return the list of entities
     */
    List<AccountFlag> findAll();

    /**
     *  Get the "id" accountFlag.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AccountFlag findOne(Long id);

    /**
     *  Delete the "id" accountFlag.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
