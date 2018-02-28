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

import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.directory.MeasureUnit;

import java.util.List;

public interface MeasureUnitDAO extends EntityDAO<MeasureUnit> {

    MeasureUnit getMeasureUnitBySign(String sign);

    MeasureUnit getMeasureUnitByName(String name);

    MeasureUnit getMeasureUnitByCode(String code);

    void setNewDefaultMeasureUnit(MeasureUnit newDefaultMeasureUnit);

    MeasureUnit getMeasureUnitByUid(String uid);

    List<String> getUidsByIds(List<Long> ids);
}
