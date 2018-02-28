/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.controller;

import com.artigile.warehouse.gui.core.report.format.ColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.MergedColumnStyleFactory;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;
import com.artigile.warehouse.gui.core.report.tooltip.ColumnTooltipFactory;

import java.util.Comparator;

/**
 * @author Shyrik, 06.12.2008
 */
public class ColumnInfo {
    /**
     * Name of the column;
     */
    private String name;

    /**
     * Object's field name, that is associated with this column.
     */
    private String field;

    /**
     * Editing strategy for cells in column.
     */
    private CellEditingStrategy editingStrategy;

    /**
     * Style factory for decoration cells in this column.
     */
    private StyleFactory columnStyleFactory;

    /**
     * Column style factory merged with row style factory (cached value to prevent creating new object each rendering).
     */
    private StyleFactory mergedColumnStyleFactory;

    /**
     * Format factory for formatting values in cells of this column
     */
    private ColumnFormatFactory columnFormatFactory;

    /**
     * Tooltip factory for getting tooltip text of the cells in this column
     */
    private ColumnTooltipFactory columnTooltipFactory;

    /**
     * Comparator that used for sorting values in this column.
     * It used when need special order of sorting.
     *
     * @see com.artigile.warehouse.gui.core.report.view.TableReportView#initTableCells()
     */
    private Comparator sortingComparator;


    /* Constructors
    ------------------------------------------------------------------------------------------------------------------*/
    /**
     * @param name - displayed name of the column.
     * @param fieldName - field name, associated with the column.
     */
    public ColumnInfo(String name, String fieldName){
        this.name = name;
        this.field = fieldName;
        this.editingStrategy = new ReadOnlyCellStrategy();
    }

    /**
     * @param name - displayed name of the column.
     * @param fieldName - field name, associated with the column.
     * @param columnStyleFactory - style factory for decorating values in this column.
     */
    public ColumnInfo(String name, String fieldName, StyleFactory columnStyleFactory){
        this.name = name;
        this.field = fieldName;
        this.editingStrategy = new ReadOnlyCellStrategy();
        this.columnStyleFactory = columnStyleFactory;
    }

    /**
     * @param name - displayed name of the column.
     * @param fieldName - field name, associated with the column.
     * @param columnFormatFactory Format factory for formatting values in cells of this column
     */
    public ColumnInfo(String name, String fieldName, ColumnFormatFactory columnFormatFactory){
        this.name = name;
        this.field = fieldName;
        this.editingStrategy = new ReadOnlyCellStrategy();
        this.columnFormatFactory = columnFormatFactory;
    }

    /**
     * @param name - displayed name of the column.
     * @param fieldName - field name, associated with the column.
     * @param sortingComparator Comparator that used for sorting values in this column
     */
    public ColumnInfo(String name, String fieldName, Comparator sortingComparator) {
        this.name = name;
        this.field = fieldName;
        this.editingStrategy = new ReadOnlyCellStrategy();
        this.sortingComparator = sortingComparator;
    }

    /**
     * @param name Displayed name of the column
     * @param fieldName Field name, associated with the column
     * @param columnStyleFactory Style factory for decorating values in this column
     * @param columnFormatFactory Format factory for formatting values in cells of this column
     */
    public ColumnInfo(String name, String fieldName, StyleFactory columnStyleFactory,
                      ColumnFormatFactory columnFormatFactory) {
        this.name = name;
        this.field = fieldName;
        this.editingStrategy = new ReadOnlyCellStrategy();
        this.columnStyleFactory = columnStyleFactory;
        this.columnFormatFactory = columnFormatFactory;
    }

    /**
     * @param name Displayed name of the column
     * @param fieldName Field name, associated with the column
     * @param editingStrategy Strategy for editing cells in the column
     * @param columnStyleFactory Style factory for decorating values in this column
     * @param columnFormatFactory Format factory for formatting values in cells of this column
     */
    public ColumnInfo(String name, String fieldName, CellEditingStrategy editingStrategy,
                      StyleFactory columnStyleFactory, ColumnFormatFactory columnFormatFactory) {
        this.name = name;
        this.field = fieldName;
        this.editingStrategy = editingStrategy;
        this.columnStyleFactory = columnStyleFactory;
        this.columnFormatFactory = columnFormatFactory;
    }

    /**
     * @param name Displayed name of the column
     * @param fieldName Field name, associated with the column
     * @param columnStyleFactory Style factory for decorating values in this column
     * @param columnFormatFactory Format factory for formatting values in cells of this column
     * @param columnTooltipFactory for getting tooltip text of the cells in this column
     */
    public ColumnInfo(String name, String fieldName, StyleFactory columnStyleFactory,
                      ColumnFormatFactory columnFormatFactory, ColumnTooltipFactory columnTooltipFactory) {
        this.name = name;
        this.field = fieldName;
        this.editingStrategy = new ReadOnlyCellStrategy();
        this.columnStyleFactory = columnStyleFactory;
        this.columnFormatFactory = columnFormatFactory;
        this.columnTooltipFactory = columnTooltipFactory;
    }

    //==================== Getters and setters ======================
    public String getName(){
        return name;
    }

    public String getField(){
        return field;
    }

    public CellEditingStrategy getEditingStrategy() {
        return editingStrategy;
    }

    public StyleFactory getColumnStyleFactory(StyleFactory rowStyleFactory) {
        if (mergedColumnStyleFactory == null){
            mergedColumnStyleFactory = new MergedColumnStyleFactory(columnStyleFactory, rowStyleFactory);
        }
        return mergedColumnStyleFactory;
    }

    public ColumnFormatFactory getColumnFormatFactory() {
        return columnFormatFactory;
    }

    public ColumnTooltipFactory getColumnTooltipFactory() {
        return columnTooltipFactory;
    }

    public Comparator getSortingComparator() {
        return sortingComparator;
    }
}
