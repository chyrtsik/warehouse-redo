/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.parser;

import com.artigile.warehouse.utils.StringUtils;

import java.util.List;

/**
 * @author Shyrik, 30.08.2009
 */

/**
 * Template part -- group of template parts, that can be hidden depending on
 * the value of the fierst calculated field.
 */
class HiddenFieldGroupPart implements TemplatePart {
    /**
     * Template parts in this hide group.
     */
    private List<TemplatePart> partsInGroup;

    /**
     * Template part, that is decides, is to show or not show fields group.
     */
    private TemplatePart decidingPart;

    public HiddenFieldGroupPart(List<TemplatePart> partsInGroup) {
        this.partsInGroup = partsInGroup;
        for (TemplatePart part : this.partsInGroup){
            if (part.isCalculated()){
                decidingPart = part;
                break;
            }
        }
    }

    @Override
    public boolean isVisible(ParsedTemplateDataSource dataSource) {
        return decidingPart == null || !StringUtils.isStringNullOrEmpty(decidingPart.calculateValue(dataSource));
    }

    @Override
    public boolean isCalculated() {
        return true;
    }

    @Override
    public String calculateValue(ParsedTemplateDataSource dataSource) {
        StringBuilder value = new StringBuilder();
        for (TemplatePart part : partsInGroup){
            value.append(part.calculateValue(dataSource));
        }
        return value.toString();
    }
}
