/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.finance.AccountOperation;
import com.artigile.warehouse.utils.dto.AccountOperationTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 18.04.2010
 */
public class AccountOperationTransformer {
    public static List<AccountOperationTO> transformList(List<AccountOperation> operationsList) {
        List<AccountOperationTO> operationsTOList = new ArrayList<AccountOperationTO>(operationsList.size());
        for (AccountOperation operation : operationsList){
            operationsTOList.add(transform(operation));
        }
        return operationsTOList;
    }

    private static AccountOperationTO transform(AccountOperation operation) {
        AccountOperationTO operationTO = new AccountOperationTO();
        operationTO.setId(operation.getId());
        operationTO.setContractorName(operation.getAccount().getContractor().getName());
        operationTO.setCurrencySign(operation.getAccount().getCurrency().getSign());
        operationTO.setPerformedUserFullName(operation.getPerformedUser().getDisplayName());
        operationTO.setOperationDateTime(operation.getOperationDateTime());
        operationTO.setInitialBalance(operation.getInitialBalance());
        operationTO.setChangeOfBalance(operation.getChangeOfBalance());
        operationTO.setNewBalance(operation.getNewBalance());
        operationTO.setOperation(operation.getOperation());
        operationTO.setNotice(operation.getNotice());
        return operationTO;
    }
}
