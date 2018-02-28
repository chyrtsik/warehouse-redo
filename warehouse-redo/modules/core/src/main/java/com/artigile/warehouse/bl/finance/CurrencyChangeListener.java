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
import com.artigile.warehouse.domain.finance.Currency;

/**
 * @author Shyrik, 23.04.2010
 */

/**
 * Interface for listening changes in currencies.
 */
public interface CurrencyChangeListener {
    /**
     * Called when new currency has been added.
     * @param currency
     * @throws BusinessException
     */
    void onCurrencyCreated(Currency currency) throws BusinessException;

    /**
     * Called when given currency is about to be deleted.
     * @param currency
     * @throws BusinessException
     */
    void onCurrencyDeleted(Currency currency) throws BusinessException;
}
