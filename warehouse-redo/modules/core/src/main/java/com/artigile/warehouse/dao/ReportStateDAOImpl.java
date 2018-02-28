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

import com.artigile.warehouse.bl.userprofile.ReportStateFilter;
import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.userprofile.ReportState;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Borisok V.V., 15.02.2009
 */
public class ReportStateDAOImpl extends GenericEntityDAO<ReportState> implements ReportStateDAO {
    @Override
    @SuppressWarnings("unchecked")
    public List<ReportState> getByFilter(ReportStateFilter filter) {
        return getSession()
            .createCriteria(ReportState.class)
                    .add(Restrictions.eq("user", filter.getUser()))
                    .add(Restrictions.eq("reportMajor", filter.getMajor()))
                    .add(Restrictions.eq("reportMinor", filter.getMinor()))
            .list();
    }
}
