package org.mlccc.cm.service;

import org.mlccc.cm.domain.AppliedDiscount;
import org.mlccc.cm.repository.AppliedDiscountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing AppliedDiscount.
 */
@Service
@Transactional
public class AppliedDiscountService {

    private final Logger log = LoggerFactory.getLogger(AppliedDiscountService.class);

    private final AppliedDiscountRepository appliedDiscountRepository;

    public AppliedDiscountService(AppliedDiscountRepository appliedDiscountRepository) {
        this.appliedDiscountRepository = appliedDiscountRepository;
    }

    /**
     * Save a appliedDiscount.
     *
     * @param appliedDiscount the entity to save
     * @return the persisted entity
     */
    public AppliedDiscount save(AppliedDiscount appliedDiscount) {
        log.debug("Request to save AppliedDiscount : {}", appliedDiscount);
        return appliedDiscountRepository.save(appliedDiscount);
    }

    /**
     *  Get all the appliedDiscounts.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AppliedDiscount> findAll() {
        log.debug("Request to get all AppliedDiscounts");
        return appliedDiscountRepository.findAll();
    }

    /**
     *  Get one appliedDiscount by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AppliedDiscount findOne(Long id) {
        log.debug("Request to get AppliedDiscount : {}", id);
        return appliedDiscountRepository.findOne(id);
    }

    /**
     *  Delete the  appliedDiscount by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AppliedDiscount : {}", id);
        appliedDiscountRepository.delete(id);
    }
}
