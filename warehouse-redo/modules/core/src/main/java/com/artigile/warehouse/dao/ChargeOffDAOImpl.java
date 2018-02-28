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
import com.artigile.warehouse.domain.chargeoff.ChargeOff;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * @author Shyrik, 09.10.2009
 */
public class ChargeOffDAOImpl extends GenericEntityDAO<ChargeOff> implements ChargeOffDAO {
    @Override
    public long getNextAvailableChargeOffNumber() {
        List result = getSession()
            .createCriteria(ChargeOff.class)
            .setProjection(Projections.max("number"))
            .list();
        return (result.size() > 0 && result.get(0) != null) ? (Long)result.get(0) + 1 : 1;
    }
}
