package org.mlccc.cm.service.impl;

import org.mlccc.cm.service.NewsLetterService;
import org.mlccc.cm.domain.NewsLetter;
import org.mlccc.cm.repository.NewsLetterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing NewsLetter.
 */
@Service
@Transactional
public class NewsLetterServiceImpl implements NewsLetterService{

    private final Logger log = LoggerFactory.getLogger(NewsLetterServiceImpl.class);

    private final NewsLetterRepository newsLetterRepository;

    public NewsLetterServiceImpl(NewsLetterRepository newsLetterRepository) {
        this.newsLetterRepository = newsLetterRepository;
    }

    /**
     * Save a newsLetter.
     *
     * @param newsLetter the entity to save
     * @return the persisted entity
     */
    @Override
    public NewsLetter save(NewsLetter newsLetter) {
        log.debug("Request to save NewsLetter : {}", newsLetter);
        return newsLetterRepository.save(newsLetter);
    }

    /**
     *  Get all the newsLetters.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<NewsLetter> findAll() {
        log.debug("Request to get all NewsLetters");
        return newsLetterRepository.findAllSortByUploadDateDesc();
    }

    /**
     *  Get one newsLetter by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public NewsLetter findOne(Long id) {
        log.debug("Request to get NewsLetter : {}", id);
        return newsLetterRepository.findOne(id);
    }

    /**
     *  Delete the  newsLetter by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete NewsLetter : {}", id);
        newsLetterRepository.delete(id);
    }
}
