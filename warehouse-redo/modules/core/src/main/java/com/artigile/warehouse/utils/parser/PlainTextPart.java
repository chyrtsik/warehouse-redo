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
 * Parts of the template, teptesented be plaint text fragment.
 */
class PlainTextPart implements TemplatePart {
    private String text;

    public PlainTextPart(String text) {
        this.text = text;
    }

    @Override
    public boolean isVisible(ParsedTemplateDataSource dataSource) {
        return true;
    }

    @Override
    public boolean isCalculated() {
        return false;
    }

    @Override
    public String calculateValue(ParsedTemplateDataSource dataSource) {
        return text;
    }
}
