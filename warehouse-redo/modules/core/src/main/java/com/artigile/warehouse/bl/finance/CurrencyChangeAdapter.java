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
public class CurrencyChangeAdapter implements CurrencyChangeListener {
    @Override
    public void onCurrencyCreated(Currency currency) throws BusinessException {
    }

    @Override
    public void onCurrencyDeleted(Currency currency) throws BusinessException {
    }
}
