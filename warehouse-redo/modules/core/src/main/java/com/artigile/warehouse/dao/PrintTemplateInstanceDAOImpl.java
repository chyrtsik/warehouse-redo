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
import com.artigile.warehouse.domain.printing.PrintTemplateInstance;
import com.artigile.warehouse.domain.printing.PrintTemplateType;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Aliaksandr.Chyrtsik, 9/11/12
 */
public class PrintTemplateInstanceDAOImpl extends GenericEntityDAO<PrintTemplateInstance> implements PrintTemplateInstanceDAO {
    @Override
    public PrintTemplateInstance getFullTemplateInstanceData(long templateInstanceId) {
        List result = getSession()
            .createCriteria(PrintTemplateInstance.class)
            .add(Restrictions.eq("id", templateInstanceId))
            .setFetchMode("printTemplate.fieldsMapping", FetchMode.JOIN)
            .list();
        return result.size() > 0 ? (PrintTemplateInstance)result.get(0) : null;
    }

    @Override
    public PrintTemplateInstance findByName(String name) {
        return (PrintTemplateInstance) getSession()
                .createCriteria(PrintTemplateInstance.class)
                .add(Restrictions.eq("name", name))
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PrintTemplateInstance> findByTemplateTypes(PrintTemplateType[] templateTypes) {
        return getSession()
                .createCriteria(PrintTemplateInstance.class)
                .createAlias("printTemplate", "template")
                .add(Restrictions.in("template.templateType", templateTypes))
                .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setNewDefaultTemplateInstance(PrintTemplateInstance defaultTemplateInstance) {
        //Only one instance can be default.
        List<PrintTemplateInstance> sameTemplateInstances = getSession()
                .createCriteria(PrintTemplateInstance.class)
                .add(Restrictions.eq("printTemplate", defaultTemplateInstance.getPrintTemplate()))
                .list();
        for (PrintTemplateInstance templateInstance : sameTemplateInstances){
            if (templateInstance.getId() != defaultTemplateInstance.getId() && templateInstance.isDefaultTemplate()){
                templateInstance.setDefaultTemplate(false);
            }
        }
    }
}
