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
import com.artigile.warehouse.domain.userprofile.ColumnState;

import java.util.List;

/**
 * @author Borisok V.V., 15.02.2009
 */
public class ColumnStateDAOImpl extends GenericEntityDAO<ColumnState> implements ColumnStateDAO {

    @Override
    public void removeByIds(List<Long> ids) {
        getSession().createQuery("delete from ColumnState where id in (:ids)")
                .setParameterList("ids", ids)
                .executeUpdate();
    }
}
