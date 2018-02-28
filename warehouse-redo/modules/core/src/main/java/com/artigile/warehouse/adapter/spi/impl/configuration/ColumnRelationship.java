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

import com.artigile.warehouse.adapter.spi.impl.DomainColumn;

/**
 * Class for storing information about column relationships between application domain fields and
 * columns of data table to be imported.
 *
 * @author Valery Barysok, 6/21/11
 */

public class ColumnRelationship {
    private DomainColumn domainColumn;
    private int columnIndex;

    public ColumnRelationship(DomainColumn domainColumn, int columnIndex) {
        this.domainColumn = domainColumn;
        this.columnIndex = columnIndex;
    }

    public DomainColumn getDomainColumn() {
        return domainColumn;
    }

    public int getColumnIndex() {
        return columnIndex;
    }
}
