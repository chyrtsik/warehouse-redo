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
import com.artigile.warehouse.domain.movement.Movement;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Shyrik, 21.11.2009
 */
public class MovementDAOImpl extends GenericEntityDAO<Movement> implements MovementDAO {
    @Override
    public Movement getMovementByNumber(long number) {
        List movements = getSession()
            .createCriteria(Movement.class)
            .add(Restrictions.eq("number", number))
            .list();

        if (movements.size() > 0 ){
            return (Movement)movements.get(0);
        }
        return null;
    }

    @Override
    public long getNextAvailableMovementNumber() {
        List result = getSession()
            .createCriteria(Movement.class)
            .setProjection(Projections.max("number"))
            .list();
        return (result.size() > 0 && result.get(0) != null) ? (Long)result.get(0) + 1 : 1;
    }
}
