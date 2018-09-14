package org.mlccc.cm.service.impl;

import org.mlccc.cm.service.ClassTimeService;
import org.mlccc.cm.domain.ClassTime;
import org.mlccc.cm.repository.ClassTimeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing ClassTime.
 */
@Service
@Transactional
public class ClassTimeServiceImpl implements ClassTimeService{

    private final Logger log = LoggerFactory.getLogger(ClassTimeServiceImpl.class);

    private final ClassTimeRepository classTimeRepository;

    public ClassTimeServiceImpl(ClassTimeRepository classTimeRepository) {
        this.classTimeRepository = classTimeRepository;
    }

    /**
     * Save a classTime.
     *
     * @param classTime the entity to save
     * @return the persisted entity
     */
    @Override
    public ClassTime save(ClassTime classTime) {
        log.debug("Request to save ClassTime : {}", classTime);
        return classTimeRepository.save(classTime);
    }

    /**
     *  Get all the classTimes.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClassTime> findAll() {
        log.debug("Request to get all ClassTimes");
        return classTimeRepository.findAll();
    }

    /**
     *  Get one classTime by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ClassTime findOne(Long id) {
        log.debug("Request to get ClassTime : {}", id);
        return classTimeRepository.findOne(id);
    }

    /**
     *  Delete the  classTime by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClassTime : {}", id);
        classTimeRepository.delete(id);
    }
}
