package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.ApiParam;
import net.authorize.api.contract.v1.ANetApiResponse;
import org.mlccc.cm.config.Constants;
import org.mlccc.cm.domain.*;
import org.mlccc.cm.security.AuthoritiesConstants;
import org.mlccc.cm.service.InvoiceService;
import org.mlccc.cm.service.PaymentService;
import org.mlccc.cm.service.RegistrationService;
import org.mlccc.cm.service.UserService;
import org.mlccc.cm.service.dto.*;
import org.mlccc.cm.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.mlccc.cm.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Payment.
 */
@RestController
@RequestMapping("/api")
public class PaymentResource {

    private final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    private static final String ENTITY_NAME = "payment";

    private final PaymentService paymentService;

    private final InvoiceService invoiceService;

    private final RegistrationService registrationService;

    private final UserService userService;

    public PaymentResource(PaymentService paymentService, InvoiceService invoiceService, RegistrationService registrationService,
                           UserService userService) {
        this.paymentService = paymentService;
        this.invoiceService = invoiceService;
        this.registrationService = registrationService;
        this.userService = userService;
    }

    /**
     * POST  /payments : Create a new payment.
     *
     * @param paymentDto the payment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new payment, or with status 400 (Bad Request) if the payment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/payments")
    @Timed
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentDTO paymentDto) throws URISyntaxException {
        log.debug("REST request to save Payment : {}", paymentDto);
        if (paymentDto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new payment cannot already have an ID")).body(null);
        }
        if(!paymentDto.getStatus().equals(Constants.PAYMENT_REFUND_STATUS) && paymentDto.getAmount() < paymentDto.getInvoiceDto().getTotal()){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "amountNotMatch", "Payment amount does not match invoiced amount")).body(null);
        }

        if(Constants.PAYMENT_TYPES.indexOf(paymentDto.getType())<0) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "unsupportedType", "Payment type is not supported")).body(null);
        }

        User loginUser = userService.getUserWithAuthorities();
        Set<Authority> authorities = loginUser.getAuthorities();
        Boolean adminUser = false;
        for(Authority auth : authorities){
            if(auth.getName().equals(AuthoritiesConstants.ADMIN)){
                adminUser = true;
                break;
            }
        }

        if(!adminUser && !paymentDto.getType().equals(Constants.PAYMENT_TYPE_CC)){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "unsupportedType", "Payment type is not supported")).body(null);
        }

        Payment payment = paymentDto.toPayment();
        InvoiceDTO invoiceDto = paymentDto.getInvoiceDto();
        Invoice invoice = invoiceService.findOne(invoiceDto.getId());

        if(payment.getStatus().equals(Constants.PAYMENT_REFUND_STATUS)){
            // refund
            StringBuilder comments = new StringBuilder(invoice.getComments()).append(" Refund for registrations with id: ");
            for(RegistrationDTO regdto : invoiceDto.getRegistrations()){
                Registration reg = registrationService.findOne(regdto.getId());
                if(reg.getStatus().equals(Constants.WITHDRAWN_NEED_REFUND_STATUS)){
                    reg.setStatus(Constants.WITHDRAWN_REFUNDED_STATUS);
                    comments.append(reg.getId()).append(" ");
                }
                reg.setModifyDate(LocalDate.now());
                registrationService.save(reg);
            }
            invoice.setComments(comments.toString());
            invoiceService.save(invoice);
            if(payment.getType().equals(Constants.PAYMENT_REFUND_CREDIT)){
                User billToUser = userService.getUserWithAuthorities(invoice.getUser().getId());
                // Refund amount is a negative number in payment
                billToUser.setCredit(billToUser.getCredit() - payment.getAmount());
                userService.update(billToUser);
            }
        } else {
            // payment
            // post credit card transaction
            if(payment.getType().equals(Constants.PAYMENT_TYPE_CC)){
                CCTransactionDTO ccTransactionDTO = paymentService.processCCPayment(paymentDto.getCreditCard());
                if(ccTransactionDTO.getResponseCode() == null){
                    return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "CreditCardPayment", "Credit Card payment is not successful")).body(null);
                } else {
                    payment.setReferenceId(ccTransactionDTO.getTransactionId());
                }
            }

            invoice.setEarlyBirdDiscount(invoiceDto.getEarlyBirdDiscount());
            invoice.setMultiClassDiscount(invoiceDto.getMultiClassDiscount());
            invoice.setRegistrationFee(invoiceDto.getRegistrationFee());
            invoice.setTeacherBenefits(invoiceDto.getBenefits());
            invoice.setUserCredit(invoiceDto.getCredit());
            invoice.setTotal(invoiceDto.getTotal());
            invoice.setModifyDate(LocalDate.now());
            invoice.setStatus(Constants.INVOICE_PAID_STATUS);

            for(RegistrationDTO regdto : invoiceDto.getRegistrations()){
                Registration reg = registrationService.findOne(regdto.getId());
                reg.setStatus(Constants.CONFIRMED_STATUS);
                reg.setModifyDate(LocalDate.now());
                registrationService.save(reg);
            }

            invoiceService.save(invoice);
        }

        payment.setInvoice(invoice);
        payment.setCreateDate(LocalDate.now());
        Payment result = paymentService.save(payment);

        return ResponseEntity.created(new URI("/api/payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /payments : Updates an existing payment.
     *
     * @param paymentDto the payment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated payment,
     * or with status 400 (Bad Request) if the payment is not valid,
     * or with status 500 (Internal Server Error) if the payment couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/payments")
    @Timed
    public ResponseEntity<Payment> updatePayment(@RequestBody PaymentDTO paymentDto) throws URISyntaxException {
        log.debug("REST request to update Payment : {}", paymentDto);
        if (paymentDto.getId() == null) {
            return createPayment(paymentDto);
        }
        Payment payment = paymentDto.toPayment();
        Payment result = paymentService.save(payment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, payment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payments : get all the payments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of payments in body
     */
    @GetMapping("/payments")
    @Timed
    public ResponseEntity<List<Payment>> getAllPayments(@ApiParam Pageable pageable, @RequestParam(required=false) String searchTerm, @RequestParam(required=false) Long invoiceId) {
        log.debug("REST request to get all Payments");
        User loginUser = userService.getUserWithAuthorities();
        Set<Authority> authorities = loginUser.getAuthorities();
        Boolean allPayments = false;
        for(Authority auth : authorities){
            if(auth.getName().equals(AuthoritiesConstants.ADMIN)){
                allPayments = true;
            }
        }
        Page<Payment> page = null;
        if(allPayments){
            if(StringUtils.isEmpty(searchTerm)) {
                if(invoiceId != null){
                    page = paymentService.findByInvoiceId(pageable, invoiceId);
                } else {
                    page = paymentService.findAll(pageable);
                }
            } else {
                List<Payment> searchedPayments = new ArrayList<>();
                Pageable p = new PageRequest(0, 1000);
                Page<UserDTO> users = userService.searchUsers(p, searchTerm.toLowerCase());
                for(UserDTO userDTO: users.getContent()){
                    Page<Payment> payments = paymentService.findByUserId(pageable, userDTO.getId());
                    searchedPayments.addAll(payments.getContent());
                }
                long total = searchedPayments.size();
                page = new PageImpl<Payment>(searchedPayments, pageable, total);
            }
        } else {
            page = paymentService.findByUserId(pageable, loginUser.getId());
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /payments/:id : get the "id" payment.
     *
     * @param id the id of the payment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the payment, or with status 404 (Not Found)
     */
    @GetMapping("/payments/{id}")
    @Timed
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        log.debug("REST request to get Payment : {}", id);
        Payment payment = paymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(payment));
    }

    /**
     * DELETE  /payments/:id : delete the "id" payment.
     *
     * @param id the id of the payment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/payments/{id}")
    @Timed
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        log.debug("REST request to delete Payment : {}", id);
        paymentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
