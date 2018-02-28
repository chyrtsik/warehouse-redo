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
import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.userprofile.ReportState;

import java.util.List;

/**
 * @author Borisok V.V., 15.02.2009
 */
public interface ReportStateDAO extends EntityDAO<ReportState> {
    List<ReportState> getByFilter(ReportStateFilter filter);
}
