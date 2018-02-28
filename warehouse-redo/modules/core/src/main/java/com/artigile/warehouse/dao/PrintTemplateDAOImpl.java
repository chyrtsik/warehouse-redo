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
import com.artigile.warehouse.domain.printing.PrintTemplate;
import com.artigile.warehouse.domain.printing.PrintTemplateType;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Shyrik
 * Date: 29.11.2008
 * Time: 9:52:01
 */
public class PrintTemplateDAOImpl extends GenericEntityDAO<PrintTemplate> implements PrintTemplateDAO{
    @Override
    public PrintTemplate getFullTemplateData(PrintTemplateType templateType) {
        List result = getSession()
            .createCriteria(PrintTemplate.class)
            .add(Restrictions.eq("templateType", templateType))
            .setFetchMode("fieldsMapping", FetchMode.JOIN)
            .list();
        return result.size() > 0 ? (PrintTemplate)result.get(0) : null;
    }
}
