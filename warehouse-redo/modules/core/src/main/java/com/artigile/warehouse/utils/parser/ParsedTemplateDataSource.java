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

/**
 * @author Shyrik, 18.01.2009
 */

/**
 * Interface, that provides fields for the parser of string templates.
 */
public interface ParsedTemplateDataSource {
    /**
     * Must return index (or another integer identifier) of the speficied field.
     * @param fieldName - name of the field to be searched.
     * @return - index (id) of the field or -1, if no there is no such field.
     */
    int getFieldIndexByName(String fieldName);

    /**
     * Must return value if the field by it's index (integer id).
     * @param index - index of the field.
     * @return field value.
     */
    String getFieldValue(int index);
}
