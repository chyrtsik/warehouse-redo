/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.details.DetailField;
import com.artigile.warehouse.domain.details.DetailType;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import java.util.List;

/**
 * @author Shyrik, 01.02.2009
 */
public class DetailFieldDAOImpl extends GenericEntityDAO<DetailField> implements DetailFieldDAO {
    @Override
    public List<String> findAllUniqueDetailFieldNames() {
        DetachedCriteria fieldCriteria = DetachedCriteria.forClass(DetailType.class, "detailType")
                .createAlias("detailType.fields", "fieldInType")
                .add(Restrictions.eqProperty("fieldInType.id", "field.id"))
                .setProjection(Projections.id());
        return doGetUniqueFieldNames(fieldCriteria);
    }

    @Override
    public List<String> findAllUniqueSerialNumberFieldNames() {
        DetachedCriteria fieldCriteria = DetachedCriteria.forClass(DetailType.class, "detailType")
                .createAlias("detailType.serialNumberFields", "serialNumberFieldInType")
                .add(Restrictions.eqProperty("serialNumberFieldInType.id", "field.id"))
                .setProjection(Projections.id());
        return doGetUniqueFieldNames(fieldCriteria);
    }

    @SuppressWarnings("unchecked")
    private List<String> doGetUniqueFieldNames(DetachedCriteria fieldCriteria) {
        return getSession()
                .createCriteria(DetailField.class, "field")
                .setProjection(Projections.distinct(Projections.property("name")))
                .add(Subqueries.exists(fieldCriteria))
                .list();
    }
}
