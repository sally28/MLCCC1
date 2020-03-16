package org.mlccc.cm.service;

import io.swagger.models.auth.In;
import org.mlccc.cm.config.Constants;
import org.mlccc.cm.domain.*;
import org.mlccc.cm.repository.DiscountRepository;
import org.mlccc.cm.repository.InvoiceRepository;
import org.mlccc.cm.repository.SchoolTermRepository;
import org.mlccc.cm.security.AuthoritiesConstants;
import org.mlccc.cm.security.DomainUserDetailsService;
import org.mlccc.cm.service.dto.InvoiceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.*;

/**
 * Service Implementation for managing Invoice.
 */
@Service
@Transactional
public class InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    private final InvoiceRepository invoiceRepository;

    private final DiscountService discountService;

    private final UserService userService;

    private final StudentService studentService;

    private final SchoolTermRepository schoolTermRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, DiscountService discountService,
                          SchoolTermRepository schoolTermRepository, UserService userService,
                          StudentService studentService) {
        this.invoiceRepository = invoiceRepository;
        this.discountService = discountService;
        this.schoolTermRepository = schoolTermRepository;
        this.userService = userService;
        this.studentService = studentService;
    }

    /**
     * Save a invoice.
     *
     * @param invoice the entity to save
     * @return the persisted entity
     */
    public Invoice save(Invoice invoice) {
        log.debug("Request to save Invoice : {}", invoice);
        return invoiceRepository.save(invoice);
    }

    /**
     *  Get all the invoices.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Invoice> findAll() {
        log.debug("Request to get all Invoices");
        return invoiceRepository.findAll();
    }

    /**
     *  Get one invoice by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Invoice findOne(Long id) {
        log.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findOne(id);
    }

    /**
     *  Delete the  invoice by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Invoice : {}", id);
        invoiceRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<Invoice> findUnpaidByUserId(Long userId) {
        log.debug("Request to get unpaid Invoices for user : {} ", userId);
        return invoiceRepository.findUnpaidByUserId(userId);
    }


    @Transactional(readOnly = true)
    public List<Invoice> findAllInvoices() {
        log.debug("Request to get unpaid all invoices.");
        return invoiceRepository.findAllInvoices();
    }

    @Transactional(readOnly = true)
    public Invoice findOneWithRegistrations(Long id) {
        log.debug("Request to findOneWithRegistrations.");
        return invoiceRepository.findOneWithRegistrations(id);
    }

    @Transactional(readOnly = true)
    public void calculateTotalAmount(Invoice invoice, InvoiceDTO dto) {
        log.debug("Request to calculateTotalAmount: {}", invoice.getId());
        Date now = Calendar.getInstance().getTime();

        Long schoolTermId = getSchoolTermId(invoice);
        SchoolTerm schoolTerm = schoolTermRepository.findOne(schoolTermId);
        User billToUser = userService.getUserWithAuthorities(invoice.getUser().getId());
        dto.setTotal(0.00);
        dto.setMultiClassDiscount(0.00);
        // step 1: first step needs to apply teacher benefits to each class;
        Double teacherBenefits = 0.00;
        for(Registration registration : invoice.getRegistrations()){
            Student student = studentService.findByIdAndFetchEager(registration.getStudent().getId());
            Set<User> parents = student.getAssociatedAccounts();
            for(User parent : parents){
                // one of student's parent is the teacher of the class registered
                User teacherAccount = registration.getMlcClass().getTeacher().getAccount();
                if(teacherAccount != null && parent.equals(teacherAccount)){
                    teacherBenefits += registration.getMlcClass().getTuition();
                    registration.setTuition(0.00);
                    break;
                } else {
                    registration.setTuition(registration.getMlcClass().getTuition());
                }
            }
        }
        dto.setBenefits(teacherBenefits);

        // step 2: apply multi-class discount;
        Map<String, Discount> discountMap = discountService.findAllBySchoolTerm(schoolTermId);
        if(invoice.getRegistrations().size() > 1 && discountMap.get(Constants.DISCOUNT_CODE_MULTICLASS) != null){
            Double highestTuition = 0.00;
            Double totalTuition = 0.00;
            for (Registration reg : invoice.getRegistrations()){
                if(reg.getTuition()>highestTuition){
                    highestTuition = reg.getTuition();
                }
                totalTuition += reg.getTuition();
            }

            Discount multiClassDiscount = discountMap.get(Constants.DISCOUNT_CODE_MULTICLASS);
            if(multiClassDiscount.getAmount() != null){
                // amount: simply minus amount from total
                dto.setTotal(totalTuition-multiClassDiscount.getAmount());
            } else {
                // percentage: apply discount (percentage) to the total tuition minus highest tuition.
                dto.setMultiClassDiscount(Math.round((totalTuition-highestTuition)*(multiClassDiscount.getPercentage()/100)*100.0)/100.0);
                dto.setTotal(totalTuition-dto.getMultiClassDiscount());
            }
        } else {
            for(Registration r : invoice.getRegistrations()){
                dto.setTotal(dto.getTotal()+r.getTuition());
            }
        }

        // step 3: apply early bird discount;
        Discount earlyBirdDiscount = discountMap.get(Constants.DISCOUNT_CODE_EARLYBIRD);
        if(earlyBirdDiscount != null){
            if(now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(schoolTerm.getEarlyBirdDate())) {
                if(earlyBirdDiscount.getAmount() != null){
                    // amount: simply minus amount from total
                    dto.setTotal(dto.getTotal()-earlyBirdDiscount.getAmount());
                } else {
                    // percentage: apply discount (percentage) to the total amount.
                    dto.setEarlyBirdDiscount(Math.round(dto.getTotal()*(earlyBirdDiscount.getPercentage()/100)*100.0)/100.0);
                    dto.setTotal(dto.getTotal()-dto.getEarlyBirdDiscount());
                }
            }
        }

        // step 4: apply user credit;
        Double credit = billToUser.getCredit();
        if(credit != null && credit>0.00) {
            if(dto.getTotal()>=credit){
                // invoice total amount is more than credit, apply all credit
                dto.setCredit(billToUser.getCredit());
                dto.setTotal(dto.getTotal() - billToUser.getCredit());
                billToUser.setCredit(0.00);
            } else {
                // invoice total amount is less than credit, apply partial credit
                dto.setCredit(dto.getTotal());
                billToUser.setCredit(billToUser.getCredit()-dto.getTotal());
                dto.setTotal(0.00);
            }
        }

        // step 5: apply registration waiver;
        Discount regWaiverDiscount = discountMap.get(Constants.DISCOUNT_CODE_REGWAIVER);
        if(now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(schoolTerm.getPromDate())) {
            dto.setRegistrationFee(0.00);
        }else {
            dto.setRegistrationFee(schoolTerm.getRegistrationFee());
            dto.setTotal(dto.getTotal()+regWaiverDiscount.getAmount());
        }
    }

    public Long getSchoolTermId(Invoice invoice){
        if(!invoice.getRegistrations().isEmpty()){
            Registration reg = invoice.getRegistrations().iterator().next();
            return reg.getMlcClass().getSchoolTerm().getId();
        } else {
            return null;
        }
    }
}
