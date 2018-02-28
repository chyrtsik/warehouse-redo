/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.contractors;

import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.math.BigDecimal;

/**
 * @author Shyrik, 23.04.2010
 */

/**
 * Actions for putting money to contractor's account.
 */
public class PutMoneyToAccountAction extends ChangeAccountBalanceAction {
    public PutMoneyToAccountAction(long contractorId, long currencyId, BigDecimal changeOfBalance, String notice) {
        super(contractorId, currencyId, changeOfBalance, I18nSupport.message("account.putMoney.notice"), notice);
    }
}
