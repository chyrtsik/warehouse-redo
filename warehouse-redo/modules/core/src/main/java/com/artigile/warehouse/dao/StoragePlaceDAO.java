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

import com.artigile.warehouse.bl.warehouse.StoragePlaceFilter;
import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.warehouse.StoragePlace;

import java.util.List;

/**
 * @author Borisok V.V., 21.12.2008
 */
public interface StoragePlaceDAO extends EntityDAO<StoragePlace> {
    List<StoragePlace> getListByFilter(StoragePlaceFilter filter);
}
