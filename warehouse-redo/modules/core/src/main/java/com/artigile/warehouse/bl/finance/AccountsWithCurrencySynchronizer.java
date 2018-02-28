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
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Shyrik, 23.04.2010
 */

/**
 * Synchronizes changes in currency with financial accounts.
 */
@Transactional(rollbackFor = BusinessException.class)
public class AccountsWithCurrencySynchronizer extends CurrencyChangeAdapter{
    private CurrencyService currencyService;
    private AccountService accountService;

    public void initialize(){
        currencyService.addListener(this);
    }

    @Override
    public void onCurrencyCreated(Currency currency) throws BusinessException {
        accountService.createAccountsForCurrency(currency);
    }

    @Override
    public void onCurrencyDeleted(Currency currency) throws BusinessException {
        accountService.deleteAccountsForCurrency(currency);
    }

    //======================================== Spring setters =======================================

    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
