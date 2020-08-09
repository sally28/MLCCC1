package org.mlccc.cm.service;

import org.mlccc.cm.domain.NewsLetter;
import java.util.List;

/**
 * Service Interface for managing NewsLetter.
 */
public interface NewsLetterService {

    /**
     * Save a newsLetter.
     *
     * @param newsLetter the entity to save
     * @return the persisted entity
     */
    NewsLetter save(NewsLetter newsLetter);

    /**
     *  Get all the newsLetters.
     *
     *  @return the list of entities
     */
    List<NewsLetter> findAll();

    /**
     *  Get the "id" newsLetter.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    NewsLetter findOne(Long id);

    /**
     *  Delete the "id" newsLetter.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
