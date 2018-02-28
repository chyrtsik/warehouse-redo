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

import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.finance.Currency;

import java.util.List;

/**
 * @author Shyrik, 09.12.2008
 */
public interface CurrencyDAO extends EntityDAO<Currency>{

    Currency getCurrencyBySign(String sign);

    Currency getCurrencyByName(String name);

    void setNewDefaultCurrency(Currency newDefaultCurrency);

    Currency getDefaultCurrency();

    Currency getCurrencyByUid(String uid);

    List<String> getUidsByIds(List<Long> ids);
}
