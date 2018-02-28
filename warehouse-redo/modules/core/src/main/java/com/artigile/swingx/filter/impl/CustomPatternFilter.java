/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.swingx.filter.impl;

import com.artigile.warehouse.utils.StringUtils;
import org.jdesktop.swingx.decorator.PatternFilter;

import java.util.StringTokenizer;

/**
 * @author Valery Barysok, 7/10/11
 */

public class CustomPatternFilter extends PatternFilter {

    private static final String LOGICAL_AND_DELIMITER = " ";
    
    
    public CustomPatternFilter(String regularExpr, int matchFlags, int col) {
        super(regularExpr.trim(), matchFlags, col);
    }

    @Override
    public boolean test(int row) {
        String columnText = StringUtils.toString(getInputValue(row, getColumnIndex()));
        if (pattern == null || !adapter.isTestable(getColumnIndex()) || StringUtils.isStringNullOrEmpty(columnText)) {
            return false;
        }

        // Start comparing
        columnText = columnText.toLowerCase();
        String inputText = pattern.pattern();
        // Each space in the input text it's a logical AND
        StringTokenizer inputTextTokenizer = new StringTokenizer(inputText, LOGICAL_AND_DELIMITER);
        if (inputTextTokenizer.countTokens() > 1) {
            // Each part (delimited with space) of the input text should contained in the column text
            while (inputTextTokenizer.hasMoreTokens()) {
                if (!columnText.contains(inputTextTokenizer.nextToken().toLowerCase())) {
                    return false;
                }
            }
            return true;
        } else if (inputTextTokenizer.countTokens() == 1) {
            return pattern.matcher(columnText).find();
        }
        return false;
    }
}
