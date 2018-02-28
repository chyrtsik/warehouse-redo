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

import com.artigile.warehouse.dao.AccountDAO;
import com.artigile.warehouse.dao.CurrencyExchangeDAO;
import com.artigile.warehouse.domain.finance.Account;
import com.artigile.warehouse.domain.finance.ExchangeRate;
import com.artigile.warehouse.utils.dto.AccountTO;
import com.artigile.warehouse.utils.dto.ExchangeRateTO;
import com.artigile.warehouse.utils.transofmers.CurrencyTransformer;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service to work with currency exchange.
 *
 * @author Shyrik, 09.12.2008
 */
@Transactional
public class CurrencyExchangeService {

    /**
     * Currency Exchange DAO.
     */
    private CurrencyExchangeDAO exchangeDAO;

    /**
     * Account DAO.
     */
    private AccountDAO accountDAO;

    
    public CurrencyExchangeService() {
    }

    
    public List<ExchangeRateTO> getAllRates() {
        return CurrencyTransformer.transformExchangeRateList(exchangeDAO.getAll());
    }

    public void saveRate(ExchangeRateTO rateTO) {
        ExchangeRate persistentRate = CurrencyTransformer.transformExchangeRate(rateTO);
        CurrencyTransformer.updateExchangeRate(persistentRate, rateTO);
        exchangeDAO.save(persistentRate);
        CurrencyTransformer.updateExchangeRate(rateTO, persistentRate);
    }

    public ExchangeRate getExchangeRateById(long rateId) {
        return exchangeDAO.get(rateId);
    }

    /**
     * Gets exchange rate by two currencies.
     *
     * @param fromCurrencyId - currency that needs to be exchage
     * @param toCurrencyId   - we need to exchange in this currency
     * @return exchange rate
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BigDecimal getExchangeRate(long fromCurrencyId, long toCurrencyId) {
        ExchangeRate exchangeRate = exchangeDAO.getExchangeRateByCurrencies(fromCurrencyId, toCurrencyId);
        if (exchangeRate == null){
            throw new IllegalArgumentException(MessageFormat.format(
                "Cannot find rate for conversion from currency with id={0} to currency with id={1}",
                fromCurrencyId, toCurrencyId)); 
        }
        return exchangeRate.getRate();
    }

    /**
     * Updates account.
     *
     * @param accountTO - account that needs to be updated
     */
    private void updateAccount(AccountTO accountTO) {
        Account account = accountDAO.get(accountTO.getId());
        account.setCurrentBalance(accountTO.getCurrentBalance());
        accountDAO.update(account);
    }

    /**
     * Converts sum of money from one currency to onother.
     * @param toCurrencyId
     * @param fromCurrencyId
     * @param sum
     * @return
     */
    public BigDecimal convert(long toCurrencyId, long fromCurrencyId, BigDecimal sum) {
        if (toCurrencyId == fromCurrencyId) {
            return sum;
        } else if (sum != null) {
            BigDecimal rate = getExchangeRate(fromCurrencyId, toCurrencyId);
            return rate.multiply(sum);
        }

        return null;
    }

    /**
     * @return Maps with exchange rates between two currencies, which defined identifiers
     */
    public Map<Long, Map<Long, Double>> getExchangeRateMap() {
        Map<Long, Map<Long, Double>> exchangeRateMap = new HashMap<Long, Map<Long, Double>>();
        List<ExchangeRate> exchangeRates = exchangeDAO.getAll();
        for (ExchangeRate exchangeRate : exchangeRates) {
            long fromID = exchangeRate.getFromCurrency().getId();
            long toID = exchangeRate.getToCurrency().getId();
            double rate = exchangeRate.getRate().doubleValue();
            if (!exchangeRateMap.containsKey(fromID)) {
                exchangeRateMap.put(fromID, new HashMap<Long, Double>());
            }
            exchangeRateMap.get(fromID).put(toID, rate);
        }
        return exchangeRateMap;
    }


    //====================== Spring setters =================
    public void setExchangeDAO(CurrencyExchangeDAO exchangeDAO) {
        this.exchangeDAO = exchangeDAO;
    }

    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }
}
