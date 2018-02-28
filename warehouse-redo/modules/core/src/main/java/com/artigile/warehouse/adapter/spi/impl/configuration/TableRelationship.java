/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.configuration;

import com.artigile.warehouse.adapter.spi.impl.column.relationship.RelationshipObservable;
import com.artigile.warehouse.adapter.spi.impl.column.relationship.RelationshipObserver;

/**
 * @author Valery Barysok, 6/21/11
 */

public class TableRelationship implements RelationshipObserver {
    @Override
    public void relationshipUpdated(RelationshipObservable obs, ColumnRelationship relationship) {
    }
}
