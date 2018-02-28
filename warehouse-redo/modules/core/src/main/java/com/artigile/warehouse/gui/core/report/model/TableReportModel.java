/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.model;

import com.artigile.warehouse.gui.core.report.controller.ReportDataSource;
import com.artigile.warehouse.utils.Copiable;
import com.artigile.warehouse.utils.reflect.SimpleObjectsFieldsProvider;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * @author Shyrik, 06.12.2008
 */

/**
 * This class implements table model, that can manipulate with the list of objects,
 * not with the table of Strings. This table model, that is used to manipulate objecs,
 * is called "Report".
 */
@SuppressWarnings("unchecked")
public class TableReportModel extends AbstractTableModel implements ReportModel {
    /**
     * List of objects, that are been displayed in the table.
     */
    private List data;

    /**
     * Provider to the fields of the  displaied objects.
     */
    private SimpleObjectsFieldsProvider fieldsProvider;

    /**
     * Source of the information about this instance of report.
     */
    private ReportDataSource reportDataSource;

    /**
     * Constructor.
     */
    public TableReportModel(ReportDataSource reportDataSource) {
        this.reportDataSource = reportDataSource;
        this.data = reportDataSource.getReportData();
        this.fieldsProvider = new SimpleObjectsFieldsProvider(reportDataSource.getReportInfo().getDataClass(), reportDataSource.getReportInfo().getColumnFields());
    }

    /**
     * Returns a name of the column.
     *@param column - the column index being queried
     * @return a string - containing the name of <code>column</code>
     */
    @Override
    public String getColumnName(int column) {
        return reportDataSource.getReportInfo().getColumns().get(column).getName();
    }

    /**
     * Returns value class for the column specified.
     * @param columnIndex
     * @return
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return fieldsProvider.getFieldValueClass(columnIndex);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return reportDataSource.getReportInfo().getColumns().size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return fieldsProvider.getFieldValue(data.get(rowIndex), columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return reportDataSource.getReportInfo().getColumns().get(columnIndex).getEditingStrategy().isEditable(data.get(rowIndex));
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        reportDataSource.getReportInfo().getColumns().get(columnIndex).getEditingStrategy().saveValue(data.get(rowIndex), aValue);
    }

    /**
     * Adding new item to the report's model. Calling to this method fires
     * table changed event.
     *
     * @param newItem
     */
    @Override
    public void addItem(Object newItem) {
        insertItem(newItem, data.size());
    }

    /**
     * Inserting new item into precified position. Calling this
     *
     * @param newItem - item to be inserted.
     * @param insertIndex - new item's location.
     */
    @Override
    public void insertItem(Object newItem, int insertIndex) {
        if ( !data.contains(newItem) ){
            if ( insertIndex <= data.size() ){
                //Insert new item into the middle of the list.
                data.add(insertIndex, newItem);
                fireTableRowsInserted(insertIndex, insertIndex);
            }
            else{
                //Add new item at the end of the list.
                data.add(newItem);
                fireTableRowsInserted(data.size() - 1, data.size() - 1);
            }
        }
        else{
            setItem(newItem);
        }
    }

    /**
     * Deleting item from the report's model. Calling to this method fiers
     * table changed event.
     *
     * @param item
     */
    @Override
    public void deleteItem(Object item) {
        if (data.remove(item)) {
            fireDataChanged();
        }
    }

    /**
     * Retrieves item of the report by it's index.
     *
     * @param itemIndex
     * @return
     */
    @Override
    public Object getItem(int itemIndex) {
        return data.get(itemIndex);
    }

    @Override
    public void setItem(Object item) {
        int index = data.indexOf(item);
        if (index != -1) {
            Object existingData = data.get(index);
            if (existingData instanceof Copiable){
                Copiable copiableData = (Copiable)existingData;
                copiableData.copyFrom(item);
            }
            else{
                data.set(index, item);
            }
            fireItemDataChanged(item);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void fireDataChanged() {
        fireTableDataChanged();
    }

    @Override
    public void fireItemDataChanged(Object item) {
        //TODO: fix refreshing here (row number in the data <> row index in the view)
        int itemRow = data.indexOf(item);
        fireTableRowsUpdated(itemRow, itemRow);
    }

    @Override
    public ReportDataSource getReportDataSource() {
        return reportDataSource;
    }

    @Override
    public void refresh() {
        this.data = reportDataSource.getReportData();
        fireDataChanged();
    }
}
