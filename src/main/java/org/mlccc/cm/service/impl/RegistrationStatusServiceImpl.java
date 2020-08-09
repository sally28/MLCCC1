package org.mlccc.cm.service.impl;

import org.mlccc.cm.service.RegistrationStatusService;
import org.mlccc.cm.domain.RegistrationStatus;
import org.mlccc.cm.repository.RegistrationStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing RegistrationStatus.
 */
@Service
@Transactional
public class RegistrationStatusServiceImpl implements RegistrationStatusService{

    private final Logger log = LoggerFactory.getLogger(RegistrationStatusServiceImpl.class);

    private final RegistrationStatusRepository registrationStatusRepository;

    public RegistrationStatusServiceImpl(RegistrationStatusRepository registrationStatusRepository) {
        this.registrationStatusRepository = registrationStatusRepository;
    }

    /**
     * Save a registrationStatus.
     *
     * @param registrationStatus the entity to save
     * @return the persisted entity
     */
    @Override
    public RegistrationStatus save(RegistrationStatus registrationStatus) {
        log.debug("Request to save RegistrationStatus : {}", registrationStatus);
        return registrationStatusRepository.save(registrationStatus);
    }

    /**
     *  Get all the registrationStatuses.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RegistrationStatus> findAll() {
        log.debug("Request to get all RegistrationStatuses");
        return registrationStatusRepository.findAll();
    }

    /**
     *  Get one registrationStatus by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public RegistrationStatus findOne(Long id) {
        log.debug("Request to get RegistrationStatus : {}", id);
        return registrationStatusRepository.findOne(id);
    }

    /**
     *  Delete the  registrationStatus by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RegistrationStatus : {}", id);
        registrationStatusRepository.delete(id);
    }
}
