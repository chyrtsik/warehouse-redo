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

import java.math.BigDecimal;

/**
 * @author Shyrik, 23.04.2010
 */

/**
 * Action of changing contractor's account balance.
 */
public class ChangeAccountBalanceAction implements AccountAction {
    private long contractorId;
    private long currencyId;
    private BigDecimal changeOfBalance;
    private String operation;
    private String notice;

    public ChangeAccountBalanceAction(long contractorId, long currencyId, BigDecimal changeOfBalance, String operation, String notice) {
        this.contractorId = contractorId;
        this.currencyId = currencyId;
        this.changeOfBalance = changeOfBalance;
        this.operation = operation;
        this.notice = notice;
    }

    @Override
    public void perform(AccountService accountService) throws BusinessException {
        accountService.changeContractorBalance(contractorId, currencyId, changeOfBalance, operation, notice);
    }
}
