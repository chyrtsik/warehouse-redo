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

/**
 * @author Shyrik, 23.04.2010
 */

/**
 * Action, performed with contractor's financial account.
 */
public interface AccountAction {
    /**
     * This method should be implemented to perform appropriate saving of account action data.
     * This works like a callback. All actions in same list of actions will be processed in single transaction.
     * @param accountService
     */
    void perform(AccountService accountService) throws BusinessException;
}
