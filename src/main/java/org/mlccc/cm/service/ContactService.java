package org.mlccc.cm.service;

import org.mlccc.cm.domain.Contact;
import java.util.List;

/**
 * Service Interface for managing Contact.
 */
public interface ContactService {

    /**
     * Save a contact.
     *
     * @param contact the entity to save
     * @return the persisted entity
     */
    Contact save(Contact contact);

    /**
     *  Get all the contacts.
     *
     *  @return the list of entities
     */
    List<Contact> findAll();

    /**
     *  Get the "id" contact.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Contact findOne(Long id);

    /**
     *  Delete the "id" contact.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
