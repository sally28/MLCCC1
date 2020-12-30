package org.mlccc.cm.service.impl;

import org.mlccc.cm.config.Constants;
import org.mlccc.cm.domain.*;
import org.mlccc.cm.repository.UserRepository;
import org.mlccc.cm.service.ClassStatusService;
import org.mlccc.cm.service.InvoiceService;
import org.mlccc.cm.service.MlcClassService;
import org.mlccc.cm.service.RegistrationService;
import org.mlccc.cm.repository.RegistrationRepository;
import org.mlccc.cm.service.dto.InvoiceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


/**
 * Service Implementation for managing Registration.
 */
@Service
@Transactional
public class RegistrationServiceImpl implements RegistrationService{

    private final Logger log = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    private final RegistrationRepository registrationRepository;

    private final UserRepository userRepository;

    private final InvoiceService invoiceService;

    private final ClassStatusService classStatusService;

    private final MlcClassService classService;

    public RegistrationServiceImpl(RegistrationRepository registrationRepository, UserRepository userRepository,
                                   InvoiceService invoiceService, ClassStatusService classStatusService,
                                   MlcClassService classService) {
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.invoiceService = invoiceService;
        this.classStatusService = classStatusService;
        this.classService = classService;
    }

    /**
     * Save a registration.
     *
     * @param registration the entity to save
     * @return the persisted entity
     */
    @Override
    public Registration save(Registration registration) {
        log.debug("Request to save Registration : {}", registration);
        // update mlcClass status if needed
        MlcClass mlcClass = registration.getMlcClass();
        Long numOfRegistrations = registrationRepository.findNumberOfRegistrationWithClassId(mlcClass.getId());

        if(numOfRegistrations.doubleValue()/mlcClass.getSize().doubleValue() >= 0.8){
            mlcClass.setStatus(classStatusService.findByName(Constants.ALMOST_FULL_STATUS));
        } else {
            mlcClass.setStatus(classStatusService.findByName(Constants.OPEN_STATUS));
        }
        classService.save(mlcClass);

        return registrationRepository.save(registration);
    }


    /**
     *  Get all the registrations.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Registration> findAll(Pageable pageable) {
        log.debug("Request to get all Registrations");
        return registrationRepository.findAll(pageable);
    }

    /**
     *  Get one registration by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Registration findOne(Long id) {
        log.debug("Request to get Registration : {}", id);
        return registrationRepository.findOne(id);
    }

    /**
     *  Delete the  registration by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Registration : {}", id);
        registrationRepository.delete(id);
    }

    @Override
    public List<Registration> findAllWithStudentIdClassId(Long studentId, Long mlcClassId){
        log.debug("Request to findAllWithStudentIdClassId: {} {} ", studentId, mlcClassId);
        return registrationRepository.findAllWithStudentIdClassId(studentId, mlcClassId);
    }

    @Override
    public List<Registration> findAllWithStudentId(Long studentId){
        log.debug("Request to findAllWithStudentId: {}", studentId);
        return registrationRepository.findAllWithStudentId(studentId);
    }

    @Override
    public Page<Registration> findAllWithClassId(Pageable pageable, Long mlcClassId){
        log.debug("Request to findAllWithClassId: {}", mlcClassId);
        return registrationRepository.findAllWithClassId(pageable, mlcClassId);
    }

    @Override
    public Page<Registration> findAllWithAssociatedUserId(Pageable pageable, Long userId){
        log.debug("Request to findAllWithAssociatedUserId: {}", userId);
        return registrationRepository.findAllWithAssociatedUserId(pageable, userId);
    }

    @Override
    public Page<Registration> findAllWithTeacherUserId(Pageable pageable, Long userId){
        log.debug("Request to findAllWithTeacherUserId: {}", userId);
        return registrationRepository.findAllWithTeacherUserId(pageable, userId);
    }

    @Override
    public Long findNumberOfRegistrationWithClassId(Long classId){
        log.debug("Request to findNumberOfRegistrationWithClassId: {}", classId);
        return registrationRepository.findNumberOfRegistrationWithClassId(classId);
    }

    @Override
    public Registration switchClass(Registration registration, MlcClass newMlcClass) {
        log.debug("Request to switch class for Registration : {}", registration);
        MlcClass existingClass = registration.getMlcClass();
        registration.setMlcClass(newMlcClass);

        Invoice invoice = invoiceService.findOne(registration.getInvoice().getId());
        for(Registration reg : invoice.getRegistrations()){
            if (reg.getId().equals(registration.getId())){
                reg.setMlcClass(newMlcClass);
            }
        }

        if(newMlcClass.getTuition() > existingClass.getTuition()){
            // if new class costs more than existing class
            registration.setStatus(Constants.PENDING_STATUS);
            invoice.setStatus(Constants.INVOICE_PARTIALLY_PAID_STATUS);
        } else if(newMlcClass.getTuition() < existingClass.getTuition()){
            // if new class costs less than existing class, need  refund
            InvoiceDTO dto = new InvoiceDTO();
            invoiceService.calculateTotalAmount(invoice, dto);
            invoice.setTeacherBenefits(dto.getBenefits());
            invoice.setRegistrationFee(dto.getRegistrationFee());
            invoice.setMultiClassDiscount(dto.getMultiClassDiscount());
            invoice.setEarlyBirdDiscount(dto.getEarlyBirdDiscount());
            invoice.setUserCredit(dto.getCredit());
            invoice.setAdjustment(dto.getAdjustment());
            invoice.setTotal(dto.getTotal());
        }

        invoice.setComments(invoice.getComments()+" Switch class from " + registration.getMlcClass().getClassName() + " to " + newMlcClass.getClassName());
        invoice.setModifyDate(LocalDate.now());

        Registration result = registrationRepository.save(registration);

        if(result != null){
            Long numOfRegistrations = registrationRepository.findNumberOfRegistrationWithClassId(newMlcClass.getId());

            if((numOfRegistrations.doubleValue()+1)/newMlcClass.getSize().doubleValue() >= 0.8){
                newMlcClass.setStatus(classStatusService.findByName(Constants.ALMOST_FULL_STATUS));
            } else {
                newMlcClass.setStatus(classStatusService.findByName(Constants.OPEN_STATUS));
            }

            Long numOfRegsExisting = registrationRepository.findNumberOfRegistrationWithClassId(existingClass.getId());

            if((numOfRegsExisting.doubleValue()-1)/existingClass.getSize().doubleValue() >= 0.8){
                existingClass.setStatus(classStatusService.findByName(Constants.ALMOST_FULL_STATUS));
            } else {
                existingClass.setStatus(classStatusService.findByName(Constants.OPEN_STATUS));
            }
            classService.save(newMlcClass);
            classService.save(existingClass);
            invoiceService.save(invoice);
        }

        return result;
    }
}
