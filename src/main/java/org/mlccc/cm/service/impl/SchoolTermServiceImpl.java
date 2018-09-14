package org.mlccc.cm.service.impl;

import org.mlccc.cm.service.SchoolTermService;
import org.mlccc.cm.domain.SchoolTerm;
import org.mlccc.cm.repository.SchoolTermRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing SchoolTerm.
 */
@Service
@Transactional
public class SchoolTermServiceImpl implements SchoolTermService{

    private final Logger log = LoggerFactory.getLogger(SchoolTermServiceImpl.class);

    private final SchoolTermRepository schoolTermRepository;

    public SchoolTermServiceImpl(SchoolTermRepository schoolTermRepository) {
        this.schoolTermRepository = schoolTermRepository;
    }

    /**
     * Save a schoolTerm.
     *
     * @param schoolTerm the entity to save
     * @return the persisted entity
     */
    @Override
    public SchoolTerm save(SchoolTerm schoolTerm) {
        log.debug("Request to save SchoolTerm : {}", schoolTerm);
        return schoolTermRepository.save(schoolTerm);
    }

    /**
     *  Get all the schoolTerms.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SchoolTerm> findAll() {
        log.debug("Request to get all SchoolTerms");
        return schoolTermRepository.findAll();
    }

    /**
     *  Get one schoolTerm by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SchoolTerm findOne(Long id) {
        log.debug("Request to get SchoolTerm : {}", id);
        return schoolTermRepository.findOne(id);
    }

    /**
     *  Delete the  schoolTerm by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SchoolTerm : {}", id);
        schoolTermRepository.delete(id);
    }
}
