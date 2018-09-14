package org.mlccc.cm.service;

import org.mlccc.cm.domain.Discount;
import org.mlccc.cm.repository.DiscountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Discount.
 */
@Service
@Transactional
public class DiscountService {

    private final Logger log = LoggerFactory.getLogger(DiscountService.class);

    private final DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    /**
     * Save a discount.
     *
     * @param discount the entity to save
     * @return the persisted entity
     */
    public Discount save(Discount discount) {
        log.debug("Request to save Discount : {}", discount);
        return discountRepository.save(discount);
    }

    /**
     *  Get all the discounts.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Discount> findAll() {
        log.debug("Request to get all Discounts");
        return discountRepository.findAll();
    }

    /**
     *  Get one discount by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Discount findOne(Long id) {
        log.debug("Request to get Discount : {}", id);
        return discountRepository.findOne(id);
    }

    /**
     *  Delete the  discount by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Discount : {}", id);
        discountRepository.delete(id);
    }
}
