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
import com.artigile.warehouse.domain.directory.MeasureUnit;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class MeasureUnitDAOImpl extends GenericEntityDAO<MeasureUnit> implements MeasureUnitDAO {

    @Override
    public MeasureUnit getMeasureUnitBySign(String sign) {
        List measureUnits = getSession()
                .createCriteria(MeasureUnit.class)
                .add(Restrictions.eq("sign", sign)).list();

        if (measureUnits.size() == 0) {
            return null;
        } else {
            return (MeasureUnit) measureUnits.get(0);
        }
    }

    @Override
    public MeasureUnit getMeasureUnitByName(String name) {
        List measureUnits = getSession()
                .createCriteria(MeasureUnit.class)
                .add(Restrictions.eq("name", name)).list();

        if (measureUnits.size() == 0) {
            return null;
        } else {
            return (MeasureUnit) measureUnits.get(0);
        }
    }

    @Override
    public MeasureUnit getMeasureUnitByCode(String code) {
        List measureUnits = getSession()
                .createCriteria(MeasureUnit.class)
                .add(Restrictions.eq("code", code)).list();

        if (measureUnits.size() == 0) {
            return null;
        } else {
            return (MeasureUnit) measureUnits.get(0);
        }
    }

    @Override
    public void setNewDefaultMeasureUnit(MeasureUnit newDefaultMeasureUnit) {
        //Update database to prevent existence of more than one default measure unit.
        List<MeasureUnit> measures = getAll();
        for (MeasureUnit measure : measures){
            if ( measure.getId() != newDefaultMeasureUnit.getId() && measure.isDefaultMeasureUnit() ){
                measure.setDefaultMeasureUnit(false);
            }
        }
    }

    @Override
    public MeasureUnit getMeasureUnitByUid(String uid) {
        return (MeasureUnit) getSession().createCriteria(MeasureUnit.class)
                .add(Restrictions.eq("uidMeasureUnit", uid))
                .uniqueResult();
    }

    @Override
    public List<String> getUidsByIds(List<Long> ids) {
        org.hibernate.Criteria criteria = getSession().createCriteria(MeasureUnit.class);
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("uidMeasureUnit"));
        criteria.setProjection(projectionList);

        List<String> results = criteria.add(Restrictions.in("id", ids)).list();
        return results;
    }
}
