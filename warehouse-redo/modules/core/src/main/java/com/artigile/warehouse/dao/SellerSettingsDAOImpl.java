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
import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.priceimport.SellerSettings;
import org.hibernate.criterion.Restrictions;

/**
 * @author Valery Barysok, 9/19/11
 */

public class SellerSettingsDAOImpl extends GenericEntityDAO<SellerSettings> implements SellerSettingsDAO {

    @Override
    public SellerSettings findSellerSettingsBy(long userId, long contractorId) {
        return (SellerSettings) getSession().createCriteria(SellerSettings.class)
                .add(Restrictions.eq("user", new User(userId)))
                .add(Restrictions.eq("contractorId", contractorId))
                .uniqueResult();
    }
}
