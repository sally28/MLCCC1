package org.mlccc.cm.service.impl;

import org.mlccc.cm.config.ApplicationProperties;
import org.mlccc.cm.service.PaymentService;
import org.mlccc.cm.domain.Payment;
import org.mlccc.cm.repository.PaymentRepository;
import org.mlccc.cm.service.dto.CCTransactionDTO;
import org.mlccc.cm.service.dto.CreditCardPayment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import net.authorize.Environment;
import net.authorize.api.contract.v1.*;
import net.authorize.api.controller.CreateTransactionController;
import net.authorize.api.controller.base.ApiOperationBase;

/**
 * Service Implementation for managing Payment.
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private ApplicationProperties applicationProperties;

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Save a payment.
     *
     * @param payment the entity to save
     * @return the persisted entity
     */
    @Override
    public Payment save(Payment payment) {
        log.debug("Request to save Payment : {}", payment);
        return paymentRepository.save(payment);
    }

    /**
     *  Get all the payments.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Payment> findAll() {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll();
    }

    /**
     *  Get one payment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Payment findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findOne(id);
    }

    /**
     *  Delete the  payment by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findByUserId(Long userId) {
        log.debug("Request to get Payments with user id");
        return paymentRepository.findByUserId(userId);
    }

    @Override
    public CCTransactionDTO processCCPayment(CreditCardPayment payment){
        // Set the request to operate in either the sandbox or production environment
        if(applicationProperties.getAnEnvironment().equals("sandbox")) {
            ApiOperationBase.setEnvironment(Environment.SANDBOX);
        } else {
            ApiOperationBase.setEnvironment(Environment.PRODUCTION);
        }

        // Create object with merchant authentication details
        MerchantAuthenticationType merchantAuthenticationType  = new MerchantAuthenticationType() ;
        merchantAuthenticationType.setName(applicationProperties.getAnLoginId());
        merchantAuthenticationType.setTransactionKey(applicationProperties.getAnTransactionKey());

        // Populate the payment data
        PaymentType paymentType = new PaymentType();
        CreditCardType creditCard = new CreditCardType();
        creditCard.setCardNumber(payment.getCardNumber());
        creditCard.setExpirationDate(payment.getExpirationDate());
        creditCard.setCardCode(payment.getCardCode());
        paymentType.setCreditCard(creditCard);

        // Set email address (optional)
        CustomerDataType customer = new CustomerDataType();
        customer.setEmail(payment.getEmail());

        // Create the payment transaction object
        TransactionRequestType txnRequest = new TransactionRequestType();
        txnRequest.setTransactionType(TransactionTypeEnum.AUTH_CAPTURE_TRANSACTION.value());
        txnRequest.setPayment(paymentType);
        txnRequest.setCustomer(customer);
        txnRequest.setAmount(new BigDecimal(payment.getPaymentAmount()).setScale(2, RoundingMode.CEILING));

        // Create the API request and set the parameters for this specific request
        CreateTransactionRequest apiRequest = new CreateTransactionRequest();
        apiRequest.setMerchantAuthentication(merchantAuthenticationType);
        apiRequest.setTransactionRequest(txnRequest);

        // Call the controller
        CreateTransactionController controller = new CreateTransactionController(apiRequest);
        controller.execute();

        // Get the response
        CreateTransactionResponse response = controller.getApiResponse();
        CCTransactionDTO transactionDTO = new CCTransactionDTO();

        // Parse the response to determine results
        if (response!=null) {
            // If API Response is OK, go ahead and check the transaction response
            if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {
                TransactionResponse result = response.getTransactionResponse();
                if (result.getMessages() != null) {
                    transactionDTO.setAuthCode(result.getAuthCode());
                    transactionDTO.setResponseCode(result.getResponseCode());
                    transactionDTO.setTransactionId(result.getTransId());
                    transactionDTO.setMessageCode(result.getMessages().getMessage().get(0).getCode());
                    transactionDTO.setDescription(result.getMessages().getMessage().get(0).getDescription());
                    log.info("Successfully created transaction with Transaction ID: " + transactionDTO.getTransactionId());
                    log.info("Response Code: " + transactionDTO.getResponseCode());
                    log.info("Message Code: " + transactionDTO.getMessageCode());
                    log.info("Description: " + transactionDTO.getDescription());
                    log.info("Auth Code: " + transactionDTO.getAuthCode());
                } else {
                    log.error("Failed Transaction.");
                    if (response.getTransactionResponse().getErrors() != null) {
                        transactionDTO.setErrorCode(response.getTransactionResponse().getErrors().getError().get(0).getErrorCode());
                        transactionDTO.setMessage(response.getTransactionResponse().getErrors().getError().get(0).getErrorText());
                        log.error("Error Code: " + transactionDTO.getErrorCode());
                        log.error("Error message: " + transactionDTO.getMessage());
                    }
                }
            } else {
                log.error("Failed Transaction.");
                if (response.getTransactionResponse() != null && response.getTransactionResponse().getErrors() != null) {
                    transactionDTO.setErrorCode(response.getTransactionResponse().getErrors().getError().get(0).getErrorCode());
                    transactionDTO.setMessage(response.getTransactionResponse().getErrors().getError().get(0).getErrorText());
                    log.error("Error Code: " + transactionDTO.getErrorCode());
                    log.error("Error message: " + transactionDTO.getMessage());
                } else {
                    transactionDTO.setErrorCode(response.getMessages().getMessage().get(0).getCode());
                    transactionDTO.setMessage(response.getMessages().getMessage().get(0).getText());
                    log.error("Error Code: " + transactionDTO.getErrorCode());
                    log.error("Error message: " + transactionDTO.getMessage());
                }
            }
        } else {
            // Display the error code and message when response is null
            ANetApiResponse errorResponse = controller.getErrorResponse();
            log.error("Failed to get response");
            if (!errorResponse.getMessages().getMessage().isEmpty()) {
                transactionDTO.setMessage(errorResponse.getMessages().getMessage().get(0).getCode()+" \n"+ errorResponse.getMessages().getMessage().get(0).getText());
                log.error("Error: "+ transactionDTO.getMessage());
            }
        }

        return transactionDTO;
    }
}
