/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.finance.ExchangeRate;

import java.util.List;

/**
 * @author Shyrik, 09.12.2008
 */
public class CurrencyExchangeDAOImpl extends GenericEntityDAO<ExchangeRate> implements CurrencyExchangeDAO {
    @SuppressWarnings({"JpaQlInspection"})
    @Override
    public ExchangeRate getExchangeRateByCurrencies(long fromCurrencyId, long toCurrencyId) {
        return (ExchangeRate) getSession().createQuery("from ExchangeRate ex where ex.fromCurrency.id=? and ex.toCurrency.id=?")
                .setParameter(0, fromCurrencyId)
                .setParameter(1, toCurrencyId)
                .uniqueResult();
    }

    @SuppressWarnings({"unchecked", "JpaQlInspection"})
    @Override
    public List<ExchangeRate> getExchangeRatesByCurrency(long currencyId) {
        return (List<ExchangeRate>)getSession()
            .createQuery("from ExchangeRate ex where ex.fromCurrency.id=:curr_id or ex.toCurrency.id=:curr_id")
            .setParameter("curr_id", currencyId)
            .list();
    }
}
