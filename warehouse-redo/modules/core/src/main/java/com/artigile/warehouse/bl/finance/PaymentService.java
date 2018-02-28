/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.finance;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.dao.*;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNote;
import com.artigile.warehouse.domain.finance.AccountOperation;
import com.artigile.warehouse.domain.finance.Payment;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Shyrik, 13.03.2010
 */

/**
 * Service for working with payments.
 */
@Transactional(rollbackFor = BusinessException.class)
public class PaymentService {

    private UserDAO userDAO;
    private ContractorDAO contractorDAO;
    private CurrencyDAO currencyDAO;
    private DeliveryNoteDAO deliveryNoteDAO;
    private PaymentDAO paymentDAO;

    private CurrencyExchangeService exchangeService;
    private AccountService accountService;

    public PaymentService(){
    }

    //=============================== Listeners support =======================================
    private List<PaymentEventListener> listeners = new ArrayList<PaymentEventListener>();

    public void addListener(PaymentEventListener listener){
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    public void removeListener(PaymentEventListener listener){
        listeners.remove(listener);        
    }

    private void firePaymentPerformed(Payment payment) throws BusinessException {
        for (PaymentEventListener listener : listeners){
            listener.onPaymentPerformed(payment);
        }
    }

    //=============================== Payment operations ======================================

    /**
     * User for performing income payment from contractor.
     * @param paymentSum total amount of money payed.
     * @param paymentCurrencyId currency of the payment.
     * @param contractorId contractor, who payed money.
     * @param deliveryNoteIds delivery notes to be payed for.
     * @param paymentNotice about the payment.
     * @throws BusinessException
     */
    public void acceptIncomePayment(BigDecimal paymentSum, long paymentCurrencyId, long contractorId, List<Long> deliveryNoteIds, String paymentNotice) throws BusinessException {
        //1. Create and fill payment information.
        Payment payment = createPayment(paymentSum, paymentCurrencyId, contractorId, deliveryNoteIds, paymentNotice);

        //2. Perform payment.
        payment = performPayment(payment);

        //3. Notify that new payment was performed.
        firePaymentPerformed(payment);
    }

    /**
     * Fills information about new payment.
     * @param paymentSum
     * @param currencyId
     * @param contractorId
     * @param deliveryNoteIds
     * @param notice
     * @return
     */
    private Payment createPayment(BigDecimal paymentSum, long currencyId, long contractorId, List<Long> deliveryNoteIds, String notice) {
        Payment payment = createInitializedPayment();
        payment.setContractor(contractorDAO.get(contractorId));
        payment.setPaymentSum(paymentSum);
        payment.setPaymentCurrency(currencyDAO.get(currencyId));
        payment.setNotice(notice);
        for (Long deliveryNoteId : deliveryNoteIds){
            payment.getDeliveryNotes().add(deliveryNoteDAO.get(deliveryNoteId));
        }
        return payment;
    }

    /**
     * Created new payment and initializes it with automatic calculated data.
     * @return
     */
    private Payment createInitializedPayment() {
        Payment payment = new Payment();
        payment.setDateTime(Calendar.getInstance().getTime());
        payment.setAcceptedUser(userDAO.get(WareHouse.getUserSession().getUser().getId())); //TODO: eliminate this usage of ui tier.
        return payment;
    }

    /**
     * Performs given payment operation.
     * @param payment initial payment data.
     * @return full payment data (additional data may be added during the payment operation).
     * @throws BusinessException
     */
    private Payment performPayment(Payment payment) throws BusinessException {
        //1. Calculating total sum, that was expected from the contractor and, if needed,
        //perform operation with contractor's balance to make payment.
        BigDecimal expectedPaymentSum = calculateExpectedSumForPayment(payment);
        BigDecimal paymentSumDifference = payment.getPaymentSum().subtract(expectedPaymentSum);
        if (paymentSumDifference.compareTo(BigDecimal.ZERO) != 0){
            //We need to withdraw from or add money to contractor's balance to perform payment operation.
            AccountOperation accountOperation = accountService.changeContractorBalance(
                payment.getContractor().getId(), payment.getPaymentCurrency().getId(), paymentSumDifference,
                I18nSupport.message("payment.changeContractorBalanceNotice"), payment.getNotice()
            );
            payment.setAccountOperation(accountOperation);
        }

        //2. Saving new payment.
        paymentDAO.save(payment);
        return payment;
    }

    /**
     * Calculates total expected sum for given payment.
     * Calculation takes into attention currency of the payment.
     * @param payment
     * @return
     */
    private BigDecimal calculateExpectedSumForPayment(Payment payment) {
        BigDecimal sum = BigDecimal.ZERO;
        long paymentCurrencyId = payment.getPaymentCurrency().getId();
        for (DeliveryNote deliveryNote : payment.getDeliveryNotes()){
            long deliveryNoteCurrencyId = deliveryNote.getCurrency().getId();
            if (deliveryNoteCurrencyId != paymentCurrencyId){
                BigDecimal priceInTargetCurrency = exchangeService.convert(paymentCurrencyId, deliveryNoteCurrencyId, deliveryNote.getTotalPrice());
                sum = sum.add(priceInTargetCurrency);
            }
            else{
                sum = sum.add(deliveryNote.getTotalPrice());
            }
        }
        return sum;
    }

    //===================================== Spring setters =====================================

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setContractorDAO(ContractorDAO contractorDAO) {
        this.contractorDAO = contractorDAO;
    }

    public void setCurrencyDAO(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
    }

    public void setDeliveryNoteDAO(DeliveryNoteDAO deliveryNoteDAO) {
        this.deliveryNoteDAO = deliveryNoteDAO;
    }

    public void setPaymentDAO(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    public void setExchangeService(CurrencyExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
