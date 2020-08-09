package org.mlccc.cm.service.impl;

import org.mlccc.cm.service.ClassRoomService;
import org.mlccc.cm.domain.ClassRoom;
import org.mlccc.cm.repository.ClassRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing ClassRoom.
 */
@Service
@Transactional
public class ClassRoomServiceImpl implements ClassRoomService{

    private final Logger log = LoggerFactory.getLogger(ClassRoomServiceImpl.class);

    private final ClassRoomRepository classRoomRepository;

    public ClassRoomServiceImpl(ClassRoomRepository classRoomRepository) {
        this.classRoomRepository = classRoomRepository;
    }

    /**
     * Save a classRoom.
     *
     * @param classRoom the entity to save
     * @return the persisted entity
     */
    @Override
    public ClassRoom save(ClassRoom classRoom) {
        log.debug("Request to save ClassRoom : {}", classRoom);
        return classRoomRepository.save(classRoom);
    }

    /**
     *  Get all the classRooms.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClassRoom> findAll() {
        log.debug("Request to get all ClassRooms");
        return classRoomRepository.findAll();
    }

    /**
     *  Get one classRoom by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ClassRoom findOne(Long id) {
        log.debug("Request to get ClassRoom : {}", id);
        return classRoomRepository.findOne(id);
    }

    /**
     *  Delete the  classRoom by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClassRoom : {}", id);
        classRoomRepository.delete(id);
    }
}
