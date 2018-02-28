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

import com.artigile.warehouse.bl.userprofile.FrameStateFilter;
import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.userprofile.FrameState;

import java.util.List;

/**
 * @author Borisok V.V., 13.09.2009
 */
public interface FrameStateDAO extends EntityDAO<FrameState> {
    List<FrameState> getByFilter(FrameStateFilter filter);
}
