package org.mlccc.cm.service.impl;

import org.mlccc.cm.service.MlcClassService;
import org.mlccc.cm.domain.MlcClass;
import org.mlccc.cm.repository.MlcClassRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing MlcClass.
 */
@Service
@Transactional
public class MlcClassServiceImpl implements MlcClassService{

    private final Logger log = LoggerFactory.getLogger(MlcClassServiceImpl.class);

    private final MlcClassRepository mlcClassRepository;

    public MlcClassServiceImpl(MlcClassRepository mlcClassRepository) {
        this.mlcClassRepository = mlcClassRepository;
    }

    /**
     * Save a mlcClass.
     *
     * @param mlcClass the entity to save
     * @return the persisted entity
     */
    @Override
    public MlcClass save(MlcClass mlcClass) {
        log.debug("Request to save MlcClass : {}", mlcClass);
        return mlcClassRepository.save(mlcClass);
    }

    /**
     *  Get all the mlcClasses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MlcClass> findAll(Pageable pageable) {
        log.debug("Request to get all MlcClasses");
        return mlcClassRepository.findAll(pageable);
    }

    /**
     *  Get one mlcClass by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MlcClass findOne(Long id) {
        log.debug("Request to get MlcClass : {}", id);
        return mlcClassRepository.findOne(id);
    }

    /**
     *  Delete the  mlcClass by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MlcClass : {}", id);
        mlcClassRepository.delete(id);
    }
}
