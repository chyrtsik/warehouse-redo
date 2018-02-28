/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.finance;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.dao.CurrencyDAO;
import com.artigile.warehouse.dao.CurrencyExchangeDAO;
import com.artigile.warehouse.dao.CurrencyWordDAO;
import com.artigile.warehouse.domain.finance.Currency;
import com.artigile.warehouse.domain.finance.CurrencyWord;
import com.artigile.warehouse.domain.finance.ExchangeRate;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.transofmers.CurrencyTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Shyrik, 26.12.2008
 */
@Transactional(rollbackFor = BusinessException.class)
public class CurrencyService {
    private CurrencyDAO currencyDAO;

    private CurrencyWordDAO currencyWordDAO;

    private CurrencyExchangeDAO exchangeDAO;

    //=========================== Constructors ===============================
    public CurrencyService() {
    }

    //========================= Listeners support ===========================
    private List<CurrencyChangeListener> listeners = new LinkedList<CurrencyChangeListener>();

    public void addListener(CurrencyChangeListener listener){
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    public void removeListener(CurrencyChangeListener listener){
        listeners.remove(listener);
    }

    private void fireCurrencyCreated(Currency currency) throws BusinessException {
        for (CurrencyChangeListener listener : listeners){
            listener.onCurrencyCreated(currency);
        }
    }

    private void fireCurrencyDeleted(Currency currency) throws BusinessException {
        for (CurrencyChangeListener listener : listeners){
            listener.onCurrencyDeleted(currency);
        }
    }

    //============================= Operations =============================
    public List<CurrencyTO> getAll(){
        return CurrencyTransformer.transformCurrenceList(currencyDAO.getAll());
    }

    public Currency getCurrencyByUid(String uid) {
        return currencyDAO.getCurrencyByUid(uid);
    }

    public List<String> getCurrencyUidsByIds(List<Long> ids) {
        return currencyDAO.getUidsByIds(ids);
    }

    public boolean isUniqueCurrencySign(String currencySign, long currencyId) {
        Currency sameCurrency = currencyDAO.getCurrencyBySign(currencySign);
        return sameCurrency == null || sameCurrency.getId() == currencyId;
    }

    public boolean isUniqueCurrencyName(String currencyName, long currencyId) {
        Currency sameCurrency = currencyDAO.getCurrencyByName(currencyName);
        return sameCurrency == null || sameCurrency.getId() == currencyId;
    }

    public void save(CurrencyTO currencyTO) throws BusinessException {
        boolean createCurrency = currencyTO.isNew();

        Currency persistentCurrency = currencyDAO.get(currencyTO.getId());
        if (persistentCurrency == null) {
            persistentCurrency = new Currency();
        }

        //1. Create new currency.
        //Currency persistentCurrency = CurrencyTransformer.transformCurrency(currencyTO);
        CurrencyTransformer.updateCurrency(persistentCurrency, currencyTO);
        currencyDAO.save(persistentCurrency);
        currencyDAO.flush();
        currencyDAO.refresh(persistentCurrency);

        if ( persistentCurrency.isDefaultCurrency() )
        {
            currencyDAO.setNewDefaultCurrency(persistentCurrency);
        }
        CurrencyTransformer.updateCurrency(currencyTO, persistentCurrency);

        //2. Ensures, that all exchange rates exists.  
        createExchangeRates();

        //3. Notify about creation of new currency.
        if (createCurrency){
            fireCurrencyCreated(persistentCurrency);
        }
    }

    public void remove(CurrencyTO currencyTO) throws BusinessException {
        Currency currency = currencyDAO.get(currencyTO.getId());
        if (currency != null) {
            //1. Remove exchange rates for new currency.
            List<ExchangeRate> rates = exchangeDAO.getExchangeRatesByCurrency(currency.getId());
            for (ExchangeRate rate : rates){
                exchangeDAO.remove(rate);
            }

            //2. Notify about deletions of currency.
            fireCurrencyDeleted(currency);

            //3. Remove currency.
            currencyDAO.remove(currency);
        }
    }

    public Currency getCurrencyById(long currencyId) {
        return currencyDAO.get(currencyId);
    }

    public CurrencyTO getCurrencyTOById(long currencyId) {
        return CurrencyTransformer.transformCurrency(getCurrencyById(currencyId));
    }

    public CurrencyWord getCurrencyWordById(long currencyWordId) {
        return currencyWordDAO.get(currencyWordId);
    }

    public Currency getDefaultCurrency() {
        return currencyDAO.getDefaultCurrency();
    }

    public CurrencyTO getDefaultCurrencyTO() {
        Currency defaultCurrency = currencyDAO.getDefaultCurrency();
        return defaultCurrency == null
                ? null
                : CurrencyTransformer.transformCurrency(defaultCurrency);
    }

    /**
     * Creates new currency, if there is no currency with such sign,  or updates existent currency
     * with given name.
     * @param currencySign
     * @param currencyName
     */
    public void createOrUpdateCurrency(String currencySign, String currencyName) throws BusinessException {
        //1. Creating currency.
        Currency currency = currencyDAO.getCurrencyBySign(currencySign);
        Boolean created = false;
        if (currency == null) {
            currency = new Currency(currencySign, currencyName);
            created = true;
        } else {
            currency.setName(currencyName);
        }
        currencyDAO.save(currency);

        //2. Ensures, that all exhange rates between all currencies exist.
        createExchangeRates();

        //3. Notify about new currency creation.
        if (created){
            fireCurrencyCreated(currency);
        }
    }

    private void createExchangeRates() {
        //Ensures, that all exhange rates between all currencies exist.
        List<Currency> allCurrencies = currencyDAO.getAll();
        for (Currency fromCurrency : allCurrencies) {
            for (Currency toCurrency : allCurrencies) {
                if (fromCurrency.getId() != toCurrency.getId()) {
                    ExchangeRate rate = exchangeDAO.getExchangeRateByCurrencies(fromCurrency.getId(), toCurrency.getId());
                    if (rate == null) {
                        rate = new ExchangeRate(fromCurrency, toCurrency, BigDecimal.ONE);
                        exchangeDAO.save(rate);
                    }
                }
            }
        }
    }

    //========================== Spring setters =========================================
    public void setCurrencyDAO(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
    }

    public void setCurrencyWordDAO(CurrencyWordDAO currencyWordDAO) {
        this.currencyWordDAO = currencyWordDAO;
    }

    public void setExchangeDAO(CurrencyExchangeDAO exchangeDAO) {
        this.exchangeDAO = exchangeDAO;
    }
}
