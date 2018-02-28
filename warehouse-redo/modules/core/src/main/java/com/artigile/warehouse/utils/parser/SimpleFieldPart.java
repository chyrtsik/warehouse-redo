/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.parser;

/**
 * @author Shyrik, 30.08.2009
 */

/**
 * Temlate part -- calculated field. Value is provide by external data provider.
 */
class SimpleFieldPart implements TemplatePart {
    private int fieldIndex;

    public SimpleFieldPart(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    @Override
    public boolean isVisible(ParsedTemplateDataSource dataSource) {
        return true;
    }

    @Override
    public boolean isCalculated() {
        return true;
    }

    @Override
    public String calculateValue(ParsedTemplateDataSource dataSource) {
        return dataSource.getFieldValue(fieldIndex);
    }
}
