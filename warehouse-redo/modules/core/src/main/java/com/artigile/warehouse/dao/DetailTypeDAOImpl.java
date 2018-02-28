/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.details.DetailType;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Shyrik, 14.12.2008
 */
public class DetailTypeDAOImpl extends GenericEntityDAO<DetailType> implements DetailTypeDAO {

    @Override
    public DetailType getDetailTypeByName(String name) {
        List types = getSession()
            .createCriteria(DetailType.class)
            .add(Restrictions.eq("name", name)).list();

        if (types.size() == 0) {
            return null;
        }
        else {
            return (DetailType) types.get(0);
        }
    }

    @Override
    public DetailType getDetailTypeByFieldId(long fieldId) {
        SQLQuery query = getSession().createSQLQuery("select detailType_id from DetailType_DetailField where detailField_id = :fieldId");
        query.setParameter("fieldId", fieldId);
        List result = query.list();
        if (result.size() == 0) {
            return null;
        }
        long detailId = ((BigInteger) result.get(0)).longValue();
        return get(detailId);
    }

}
