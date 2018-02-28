/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.bl.finance.CurrencyService;
import com.artigile.warehouse.domain.finance.Currency;
import com.artigile.warehouse.domain.finance.ExchangeRate;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.ExchangeRateTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Transforms currency.
 *
 * @author IoaN, Dec 10, 2008
 */
public final class CurrencyTransformer {
    private CurrencyTransformer() {
    }

    private static CurrencyService getCurrencyService(){
        return SpringServiceContext.getInstance().getCurrencyService();
    }

    /**
     * Transforms list of CurrencyTO in list of Currency.
     *
     * @param currencies - list of domain objects
     * @return list of TO objects.
     */
    public static List<CurrencyTO> transformCurrenceList(List<Currency> currencies) {
        List<CurrencyTO> newList = new ArrayList<CurrencyTO>();
        for (Currency currency : currencies) {
            newList.add(transformCurrency(currency));
        }
        return newList;
    }

    public static CurrencyTO transformCurrency(Currency currency) {
        if (currency == null) {
            return null;
        }
        CurrencyTO currencyTO = new CurrencyTO();
        updateCurrency(currencyTO, currency);
        return currencyTO;
    }

    /**
     * @param currencyTO (in, out) -- DTO to be synchronized with entity.
     * @param currency (in) -- entity with fresh data.
     */
    public static void updateCurrency(CurrencyTO currencyTO, Currency currency) {
        currencyTO.setId(currency.getId());
        currencyTO.setUidCurrency(currency.getUidCurrency());
        currencyTO.setSign(currency.getSign());
        currencyTO.setName(currency.getName());
        currencyTO.setDefaultCurrency(currency.isDefaultCurrency());
        currencyTO.setCurrencyWord(CurrencyWordTransformer.transform(currency.getCurrencyWord(), currencyTO));
        currencyTO.setAssociatedColor(currency.getAssociatedColor());
    }

    public static Currency transformCurrency(CurrencyTO currencyTO) {
        if (currencyTO == null){
            return null;
        }
        Currency currency = getCurrencyService().getCurrencyById(currencyTO.getId());
        if (currency == null){
            currency = new Currency();
        }
        return currency;
    }

    /**
     * @param currency (in, out) -- entity to be synchronized with DTO.
     * @param currencyTO (in) -- DTO with fresh data.
     */
    public static void updateCurrency(Currency currency, CurrencyTO currencyTO) {
        currency.setSign(currencyTO.getSign());
        currency.setName(currencyTO.getName());
        currency.setDefaultCurrency(currencyTO.getDefaultCurrency());
        currency.setCurrencyWord(CurrencyWordTransformer.transform(currencyTO.getCurrencyWord(), currency));
        currency.setAssociatedColor(currencyTO.getAssociatedColor());
    }

    public static ExchangeRateTO transformExchangeRate(ExchangeRate rate) {
        if (rate == null){
            return null;
        }
        ExchangeRateTO rateTO = new ExchangeRateTO();
        updateExchangeRate(rateTO, rate);
        return rateTO;
    }

    public static void updateExchangeRate(ExchangeRateTO rateTO, ExchangeRate rate) {
        rateTO.setId(rate.getId());
        rateTO.setFromCurrency(transformCurrency(rate.getFromCurrency()));
        rateTO.setToCurrency(transformCurrency(rate.getToCurrency()));
        rateTO.setRate(rate.getRate());
    }

    public static ExchangeRate transformExchangeRate(ExchangeRateTO rateTO) {
        if (rateTO == null){
            return null;
        }
        ExchangeRate rate = SpringServiceContext.getInstance().getExchangeService().getExchangeRateById(rateTO.getId());
        if (rate == null){
            rate = new ExchangeRate();
        }
        return rate;
    }

    public static void updateExchangeRate(ExchangeRate rate, ExchangeRateTO rateTO){
        rate.setFromCurrency(transformCurrency(rateTO.getFromCurrency()));
        rate.setToCurrency(transformCurrency(rateTO.getToCurrency()));
        rate.setRate(rateTO.getRate());
    }

    public static List<ExchangeRateTO> transformExchangeRateList(List<ExchangeRate> rates) {
        List<ExchangeRateTO> result = new ArrayList<ExchangeRateTO>();
        for (ExchangeRate rate : rates) {
            result.add(transformExchangeRate(rate));
        }
        return result;
    }
}
