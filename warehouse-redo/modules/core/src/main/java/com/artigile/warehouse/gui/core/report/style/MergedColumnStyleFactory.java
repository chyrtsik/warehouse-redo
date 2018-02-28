/*
 * Copyright (c) 2007-2012 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.style;

import java.util.HashMap;
import java.util.Map;

/**
 * Style factory that support merging styles of row and column to allow column styles to overload row styles.
 *
 * @author Aliaksandr.Chyrtsik, 18.11.12
 */
public class MergedColumnStyleFactory implements StyleFactory {
    private StyleFactory columnStyleFactory;
    private StyleFactory rowStyleFactory;

    /**
     * Cached merge styles (to prevent creating new objects each time report is drawn).
     */
    private Map<Style, Map<Style, Style>> mergedStyles = new HashMap<Style, Map<Style, Style>>();

    public MergedColumnStyleFactory(StyleFactory columnStyleFactory, StyleFactory rowStyleFactory) {
        this.columnStyleFactory = columnStyleFactory;
        this.rowStyleFactory = rowStyleFactory;
    }

    @Override
    public Style getStyle(Object rowData) {
        Style columnStyle = columnStyleFactory == null ? null : columnStyleFactory.getStyle(rowData);
        Style rowStyle = rowStyleFactory == null ? null : rowStyleFactory.getStyle(rowData);
        return getMergedStyle(columnStyle, rowStyle);
    }

    private Style getMergedStyle(Style columnStyle, Style rowStyle) {
        //1. Check if style needs merging at all.
        if (columnStyle == null && rowStyle == null){
            return null;
        }
        else if (columnStyle == null){
            return rowStyle;
        }
        else if (rowStyle == null){
            return columnStyle;
        }

        //2. Get cached or create new merged style.
        Map<Style, Style> columnStylesForRowStyle = mergedStyles.get(rowStyle);
        if (columnStylesForRowStyle == null){
            //Create first entry for this row style.
            Style mergedStyle = createMergedStyle(columnStyle, rowStyle);
            columnStylesForRowStyle = new HashMap<Style, Style>();
            columnStylesForRowStyle.put(columnStyle ,mergedStyle);
            mergedStyles.put(rowStyle, columnStylesForRowStyle);
            return mergedStyle;
        }
        else{
            Style mergedStyle = columnStylesForRowStyle.get(columnStyle);
            if (mergedStyle == null){
                mergedStyle = createMergedStyle(columnStyle, rowStyle);
                columnStylesForRowStyle.put(columnStyle, mergedStyle);
            }
            return mergedStyle;
        }
    }

    private Style createMergedStyle(Style columnStyle, Style rowStyle) {
        Style mergedStyle = new Style();
        mergedStyle.setBackground(columnStyle.getBackground() != null ? columnStyle.getBackground() : rowStyle.getBackground());
        mergedStyle.setForeground(columnStyle.getForeground() != null ? columnStyle.getForeground() : rowStyle.getForeground());
        mergedStyle.setHorizontalContentAlign(columnStyle.getHorizontalContentAlign() != null ? columnStyle.getHorizontalContentAlign() : rowStyle.getHorizontalContentAlign());
        mergedStyle.setVerticalContentAlign(columnStyle.getVerticalContentAlign() != null ? columnStyle.getVerticalContentAlign() : rowStyle.getVerticalContentAlign());
        return mergedStyle;
    }
}
