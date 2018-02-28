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
import com.artigile.warehouse.domain.finance.AccountOperation;

import java.util.List;

/**
 * @author Shyrik, 14.03.2010
 */
public interface AccountOperationDAO extends EntityDAO<AccountOperationDAO> {
    /**
     * Returns a list of all operations with contractor's accounts.
     * @param contractorId
     */
    List<AccountOperation> getOperationsForContractor(long contractorId);
}
