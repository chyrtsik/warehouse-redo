/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.controller;

import com.artigile.warehouse.gui.core.report.style.DefaultStyleFactory;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 06.12.2008
 */

/**
 * Information, that is used for parametrization of the report editor.
 */
public class ReportInfo {
    /**
     * Class of the report's data item.
     */
    private Class dataClass;

    /**
     * Information about each column in the report.
     */
    private List<ColumnInfo> columns = new ArrayList<ColumnInfo>();

    /**
     * Factory for obtaining styles of table rows (for coloring rows, for example).
     */
    private StyleFactory rowStyleFactory = DefaultStyleFactory.getInstance();

    /**
     * Miscellaneous options of the report.
     */
    private ReportOptions reportOptions = new ReportOptions();

    /**
     * Constructor.
     * @param dataClass - class of te report's item.
     */
    public ReportInfo(Class dataClass){
        this.dataClass = dataClass;
    }

    //=================== Manipulators ====================
    public void addColumn(ColumnInfo newColumn){
        columns.add(newColumn);        
    }

    //===================== Getters =======================
    public Class getDataClass() {
        return dataClass;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }

    /**
     * Returns a list of field names, represented by each column.
     * @return
     */
    public String[] getColumnFields() {
        String[] fields = new String[columns.size()];
        for (int i=0; i<columns.size(); i++){
            fields[i] = columns.get(i).getField();
        }
        return fields;
    }

    public void setRowStyleFactory(StyleFactory rowStyleFactory) {
        if (rowStyleFactory != null){
            this.rowStyleFactory = rowStyleFactory;
        }
        else{
            this.rowStyleFactory = DefaultStyleFactory.getInstance();
        }
    }

    public ReportOptions getOptions() {
        return reportOptions;
    }

    /**
     * Returns style factory for the column.
     * @param column - column to get style factory for.
     * @return style factory of column.
     */
    public StyleFactory getColumnStyleFactory(int column) {
        StyleFactory columnStyleFactory = columns.get(column).getColumnStyleFactory(rowStyleFactory);
        return columnStyleFactory == null ? rowStyleFactory : columnStyleFactory;
    }
}
