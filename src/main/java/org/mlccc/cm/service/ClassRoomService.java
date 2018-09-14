package org.mlccc.cm.service;

import org.mlccc.cm.domain.ClassRoom;
import java.util.List;

/**
 * Service Interface for managing ClassRoom.
 */
public interface ClassRoomService {

    /**
     * Save a classRoom.
     *
     * @param classRoom the entity to save
     * @return the persisted entity
     */
    ClassRoom save(ClassRoom classRoom);

    /**
     *  Get all the classRooms.
     *
     *  @return the list of entities
     */
    List<ClassRoom> findAll();

    /**
     *  Get the "id" classRoom.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ClassRoom findOne(Long id);

    /**
     *  Delete the "id" classRoom.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
