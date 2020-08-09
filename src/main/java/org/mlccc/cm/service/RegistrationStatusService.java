package org.mlccc.cm.service;

import org.mlccc.cm.domain.RegistrationStatus;
import java.util.List;

/**
 * Service Interface for managing RegistrationStatus.
 */
public interface RegistrationStatusService {

    /**
     * Save a registrationStatus.
     *
     * @param registrationStatus the entity to save
     * @return the persisted entity
     */
    RegistrationStatus save(RegistrationStatus registrationStatus);

    /**
     *  Get all the registrationStatuses.
     *
     *  @return the list of entities
     */
    List<RegistrationStatus> findAll();

    /**
     *  Get the "id" registrationStatus.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    RegistrationStatus findOne(Long id);

    /**
     *  Delete the "id" registrationStatus.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
