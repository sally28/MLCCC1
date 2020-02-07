package org.mlccc.cm.service.impl;

import org.mlccc.cm.service.MlcClassService;
import org.mlccc.cm.domain.MlcClass;
import org.mlccc.cm.repository.MlcClassRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * Service Implementation for managing MlcClass.
 */
@Service
@Transactional
public class MlcClassServiceImpl implements MlcClassService{

    private final Logger log = LoggerFactory.getLogger(MlcClassServiceImpl.class);

    private final MlcClassRepository mlcClassRepository;

    public MlcClassServiceImpl(MlcClassRepository mlcClassRepository) {
        this.mlcClassRepository = mlcClassRepository;
    }

    /**
     * Save a mlcClass.
     *
     * @param mlcClass the entity to save
     * @return the persisted entity
     */
    @Override
    public MlcClass save(MlcClass mlcClass) {
        log.debug("Request to save MlcClass : {}", mlcClass);
        return mlcClassRepository.save(mlcClass);
    }

    /**
     *  Get all the mlcClasses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MlcClass> findAll(Pageable pageable) {
        log.debug("Request to get all MlcClasses");
        return mlcClassRepository.findAll(pageable);
    }

    /**
     *  Get one mlcClass by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MlcClass findOne(Long id) {
        log.debug("Request to get MlcClass : {}", id);
        return mlcClassRepository.findOne(id);
    }

    /**
     *  Delete the  mlcClass by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MlcClass : {}", id);
        mlcClassRepository.delete(id);
    }

    @Override
    public List<MlcClass> findAllWithTeacherId(Long teacherId){
        log.debug("Request to findAllWithTeacherId: {}", teacherId);
        return mlcClassRepository.findAllWithTeacherId(teacherId);
    }

    @Override
    public List<MlcClass> findAllWithTeacherUserId(Long teacherUserId){
        log.debug("Request to findAllWithTeacherUserId: {}", teacherUserId);
        return mlcClassRepository.findAllWithTeacherUserId(teacherUserId);
    }

    @Override
    public Page<MlcClass> findAllWithSearchTerm(Pageable pageable, String searchTerm, Long categoryId, Long teacherId, Long schoolTermId){
        log.debug("Request to findAllWithSearchTerm: {}, {}, {}, {}", searchTerm, categoryId, teacherId);
        if(!StringUtils.isEmpty(searchTerm)){
            searchTerm = "%"+searchTerm.toLowerCase()+"%";
            return mlcClassRepository.findAllWithSearchTerm(pageable, searchTerm);
        } else if (teacherId != null){
            return mlcClassRepository.findAllWithTeacherAccountId(pageable, teacherId);
        } else if (schoolTermId != null || categoryId != null){
            if(schoolTermId == null){
                return mlcClassRepository.findAllWithCategory(pageable, categoryId);
            } else if(categoryId == null){
                return mlcClassRepository.findAllPageWithSchoolTermId(pageable, schoolTermId);
            } else {
                return mlcClassRepository.findAllWithSchoolTermCategory(pageable, schoolTermId, categoryId);
            }
        } else {
            return mlcClassRepository.findAll(pageable);
        }
    }

    @Override
    public List<MlcClass> findAllActive() {
        log.debug("Request to findAllActive ");
        return mlcClassRepository.findAllActive();
    }

    @Override
    public List<MlcClass> findAllWithSchoolTermId(Long schoolTermId) {
        log.debug("Request to findAllWithSchoolTermId ");
        return mlcClassRepository.findAllWithSchoolTermId(schoolTermId);
    }


}
