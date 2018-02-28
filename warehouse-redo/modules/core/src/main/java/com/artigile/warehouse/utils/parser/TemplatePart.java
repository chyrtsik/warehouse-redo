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
 * Represents part of template and way to get it's value.
 */
interface TemplatePart {
    /**
     * Should return true, if template part is visible and should e plased into output string.
     * @param dataSource data source, providing template fields values.
     * @return
     */
    boolean isVisible(ParsedTemplateDataSource dataSource);

    /**
     * Should return true, of field value if calculated dynamically.
     * @return
     */
    boolean isCalculated();

    /**
     * Should calculate value of template part. Value  will be placed into output string.
     * @param dataSource data source, providing template fields values.
     * @return
     */
    String calculateValue(ParsedTemplateDataSource dataSource);
}
