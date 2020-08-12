package org.mlccc.cm.service.impl;

import org.mlccc.cm.service.MlcClassCategoryService;
import org.mlccc.cm.domain.MlcClassCategory;
import org.mlccc.cm.repository.MlcClassCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing MlcClassCategory.
 */
@Service
@Transactional
public class MlcClassCategoryServiceImpl implements MlcClassCategoryService{

    private final Logger log = LoggerFactory.getLogger(MlcClassCategoryServiceImpl.class);

    private final MlcClassCategoryRepository mlcClassCategoryRepository;

    public MlcClassCategoryServiceImpl(MlcClassCategoryRepository mlcClassCategoryRepository) {
        this.mlcClassCategoryRepository = mlcClassCategoryRepository;
    }

    /**
     * Save a mlcClassCategory.
     *
     * @param mlcClassCategory the entity to save
     * @return the persisted entity
     */
    @Override
    public MlcClassCategory save(MlcClassCategory mlcClassCategory) {
        log.debug("Request to save MlcClassCategory : {}", mlcClassCategory);
        return mlcClassCategoryRepository.save(mlcClassCategory);
    }

    /**
     *  Get all the mlcClassCategories.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MlcClassCategory> findAll() {
        log.debug("Request to get all MlcClassCategories");
        return mlcClassCategoryRepository.findAll(sortByNameAsc());
    }

    private Sort sortByNameAsc() {
        return new Sort(Sort.Direction.ASC, "name");
    }
    /**
     *  Get one mlcClassCategory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MlcClassCategory findOne(Long id) {
        log.debug("Request to get MlcClassCategory : {}", id);
        return mlcClassCategoryRepository.findOne(id);
    }

    /**
     *  Delete the  mlcClassCategory by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MlcClassCategory : {}", id);
        mlcClassCategoryRepository.delete(id);
    }
}
