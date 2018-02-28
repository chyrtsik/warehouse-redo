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

import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.finance.Account;
import com.artigile.warehouse.domain.finance.AccountOperation;
import com.artigile.warehouse.domain.finance.Currency;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author IoaN, Dec 14, 2008
 */

public class AccountDAOImpl extends GenericEntityDAO<Account> implements AccountDAO {
    @SuppressWarnings("unchecked")
    @Override
    public List<Account> getAccountsByCurrency(Currency currency) {
        return getSession()
            .createCriteria(Account.class)
            .add(Restrictions.eq("currency", currency))
            .list();
    }

    @Override
    public Account getAccountForContractor(long contractorId, long currencyId) {
        List result = getSession()
            .createCriteria(Account.class)
            .add(Restrictions.eq("contractor.id", contractorId))
            .add(Restrictions.eq("currency.id", currencyId))
            .list();
        return result.size() == 0 ? null : (Account)result.get(0);
    }

    @Override
    public boolean accountHasHistory(Account account) {
        Long historyCount = (Long)getSession()
            .createCriteria(AccountOperation.class)
            .add(Restrictions.eq("account", account))
            .setProjection(Projections.rowCount())
            .list().get(0);
        return historyCount > 0;
    }
}
