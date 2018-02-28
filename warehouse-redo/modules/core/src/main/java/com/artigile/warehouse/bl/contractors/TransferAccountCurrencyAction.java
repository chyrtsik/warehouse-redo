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

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.finance.AccountService;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.math.BigDecimal;

/**
 * @author Shyrik, 23.04.2010
 */

/**
 * Action of transferring some amount of contractor's balance from one currency to another.
 */
public class TransferAccountCurrencyAction implements AccountAction {
    private AccountAction withdrawAction;
    private AccountAction putActions;

    public TransferAccountCurrencyAction(long contractorId, CurrencyTO fromCurrency, BigDecimal changeOfFromAccount,
                                         CurrencyTO toCurrency, BigDecimal changeOfToAccount, BigDecimal usedExchangeRate, String notice) {
        //Naturally this actions consists of two basic actions: withdraw money from first account and
        //put money to second account.
        //1. Withdraw action.
        String withdrawNotice = I18nSupport.message(
            "account.transferCurrency.withdraw.notice",
            fromCurrency.getSign(),
            toCurrency.getSign(),
            StringUtils.formatNumber(usedExchangeRate)
        );
        withdrawAction = new ChangeAccountBalanceAction(contractorId, fromCurrency.getId(), changeOfFromAccount, withdrawNotice, notice);

        //2. Put money action.
        String putNotice = I18nSupport.message(
            "account.transferCurrency.put.notice",
            fromCurrency.getSign(),
            toCurrency.getSign(),
            StringUtils.formatNumber(usedExchangeRate)
        );
        putActions = new ChangeAccountBalanceAction(contractorId, toCurrency.getId(), changeOfToAccount, putNotice, notice);
    }

    @Override
    public void perform(AccountService accountService) throws BusinessException {
        withdrawAction.perform(accountService);
        putActions.perform(accountService);
    }
}
