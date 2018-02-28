/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.column.relationship;

import java.awt.*;

/**
 * @author Valery Barysok, 6/20/11
 */

public interface TableRelationshipEditor {

    public Component getComponent();

    public RelationshipObservable getRelationshipObservable();

    public void updateRelationship();

    public void resetRelationship();
}
