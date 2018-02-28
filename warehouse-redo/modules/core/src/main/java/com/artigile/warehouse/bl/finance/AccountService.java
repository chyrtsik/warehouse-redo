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
import com.artigile.warehouse.dao.AccountDAO;
import com.artigile.warehouse.dao.AccountOperationDAO;
import com.artigile.warehouse.dao.ContractorDAO;
import com.artigile.warehouse.dao.UserDAO;
import com.artigile.warehouse.domain.contractors.Contractor;
import com.artigile.warehouse.domain.finance.Account;
import com.artigile.warehouse.domain.finance.AccountOperation;
import com.artigile.warehouse.domain.finance.Currency;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.dto.AccountOperationTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.transofmers.AccountOperationTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * @author Shyrik, 13.03.2010
 */

/**
 * Service for working with financial accounts of contractors.
 */
@Transactional(rollbackFor = BusinessException.class)
public class AccountService {
    private UserDAO userDAO;
    private AccountDAO accountDAO;
    private AccountOperationDAO accountOperationDAO;
    private ContractorDAO contractorDAO;

    /**
     * Use this method to change actual contractor's balance.
     * Balance changes is logged and later may be audited.
     * @param contractorId contractor's id, which balance will be changed.
     * @param currencyId currency id of the balance account to be changed.
     * @param changeOfBalance sum of money to add or withdraw from the balance.
     * @param operation additional information about operation.
     * @return account operation object, that represents balance change operation.
     * @throws BusinessException
     */
    public AccountOperation changeContractorBalance(long contractorId, long currencyId, BigDecimal changeOfBalance, String operation, String notice) throws BusinessException{
        AccountOperation accountOperation = createAccountOperation(contractorId, currencyId, changeOfBalance, operation, notice);
        accountOperation = performAccountOperation(accountOperation);
        return accountOperation;
    }

    /**
     * Performs change account balance operation.
     * @param accountOperation account operation with data, added during the operation.
     * @return
     */
    private AccountOperation performAccountOperation(AccountOperation accountOperation) {
        Account account = accountOperation.getAccount();

        accountOperation.setInitialBalance(account.getCurrentBalance());
        account.setCurrentBalance(account.getCurrentBalance().add(accountOperation.getChangeOfBalance()));
        accountOperation.setNewBalance(account.getCurrentBalance());

        accountDAO.save(account);
        accountOperationDAO.save(accountOperation);

        return accountOperation;
    }

    /**
     * Creates entity representing operation with contractor balance.
     *
     * @param contractorId
     * @param currencyId
     * @param changeOfBalance
     * @param operation
     * @param notice
     * @return
     */
    private AccountOperation createAccountOperation(long contractorId, long currencyId, BigDecimal changeOfBalance, String operation, String notice) {
        AccountOperation accountOperation = createInitializedAccountOperation();
        accountOperation.setAccount(accountDAO.getAccountForContractor(contractorId, currencyId));
        accountOperation.setChangeOfBalance(changeOfBalance);
        accountOperation.setOperation(operation);
        accountOperation.setNotice(notice);
        return accountOperation;
    }

    /**
     * Initializes new account operation with default values.
     * @return
     */
    private AccountOperation createInitializedAccountOperation() {
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setPerformedUser(userDAO.get(WareHouse.getUserSession().getUser().getId())); //TODO: eliminate this reference to presentation tier.
        accountOperation.setOperationDateTime(Calendar.getInstance().getTime());
        return accountOperation;
    }

    /**
     * Used for retrieving list of all contractor's account operations.
     * @param contractorId
     * @return
     */
    public List<AccountOperationTO> getContractorAccountOperations(long contractorId) {
        return AccountOperationTransformer.transformList(accountOperationDAO.getOperationsForContractor(contractorId));
    }

    /**
     * Creates accounts for given currency for every contractor.
     * @param currency
     */
    public void createAccountsForCurrency(Currency currency) {
        //Create accounts in new currency.
        List<Contractor> contactors = contractorDAO.getAll();
        for (Contractor contractor : contactors){
            contractor.getAccounts().add(new Account(currency, contractor, BigDecimal.ZERO));
        }
    }

    /**
     * Tries to delete accounts with given currency.
     * @param currency
     * @throws BusinessException
     */
    public void deleteAccountsForCurrency(Currency currency) throws BusinessException {
        List<Account> accounts = accountDAO.getAccountsByCurrency(currency);
        for (Account account : accounts){
            if (account.getCurrentBalance().compareTo(BigDecimal.ZERO) != 0){
                //Account could be deleted only if it's balance == 0.
                throw new BusinessException(I18nSupport.message("account.error.cannotDeleteNotZeroAccount"));
            }
            else if (accountDAO.accountHasHistory(account)){
                //And there should be no history about changes of this account's balance.
                throw new BusinessException(I18nSupport.message("account.error.cannotDeleteAccountWithHistory"));
            }
            accountDAO.remove(account);
        }
    }

    //====================================== Spring setters ==================================
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void setAccountOperationDAO(AccountOperationDAO accountOperationDAO) {
        this.accountOperationDAO = accountOperationDAO;
    }

    public void setContractorDAO(ContractorDAO contractorDAO) {
        this.contractorDAO = contractorDAO;
    }
}
