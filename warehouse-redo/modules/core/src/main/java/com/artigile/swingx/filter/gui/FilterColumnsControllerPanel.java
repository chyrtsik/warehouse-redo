/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.swingx.filter.gui;

import com.artigile.swingx.filter.FilterObservable;
import com.artigile.swingx.filter.FilterObserver;
import com.artigile.swingx.filter.TableFilter;
import com.artigile.swingx.filter.TableFilterEditor;
import com.artigile.swingx.filter.gui.editor.TextFilterEditor;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class setting up together all the column filters
 * Note that, while the TableFilterHeader handles columns using their model
 * numbering, the FilterColumnsControllerPanel manages the columns as they are
 * sorted in the Table. That is, if the user changes the order of two or
 * more columns, this class reacts by reordering internal data structures
 *
 * @author Borisok V.V., 24.01.2009
 */
public class FilterColumnsControllerPanel extends JPanel implements TableColumnModelListener {

    /**
     * The list of filter columns, sorted in the view way
     */
    private List<FilterColumnPanel> filterColumns;
    /**
     * The panel must keep a reference to the TableColumnModel,
     * to be able to 'unregister' when the controller is destroyed.
     */
    private TableColumnModel tableColumnModel;
    private JXTable table;
    TableFilter filterHandler = new TableFilter();

    public FilterColumnsControllerPanel(JXTable table) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.table = table;
        filterHandler.setTable(this.table);
        this.tableColumnModel = table.getColumnModel();
        initialization();
    }

    private void addObserver(FilterObserver filterObserver) {
        for (FilterColumnPanel filterColumnPanel : filterColumns) {
            FilterObservable filterObservable = filterColumnPanel.getFilterEditor().getFilterObservable();
            filterObservable.addFilterObserver(filterObserver);
        }
    }

    private void removeObserver(FilterObserver filterObserver) {
        for (FilterColumnPanel filterColumnPanel : filterColumns) {
            FilterObservable filterObservable = filterColumnPanel.getFilterEditor().getFilterObservable();
            filterObservable.removeFilterObserver(filterObserver);
        }
    }

    /**
     * <p>Sets a new table filter.</p>
     * <p/>
     * <p>The filters associated to the initial TableFilter are transferred to the new
     * one.</p>
     */
    public void setTableFilter(TableFilter tableFilter) {
        removeObserver(filterHandler);
        tableFilter.setTable(table);
        filterHandler = tableFilter;
        addObserver(tableFilter);
    }

    private void initialization() {
        filterColumns = new ArrayList<FilterColumnPanel>();
        for (int i = 0, j = 0; i < tableColumnModel.getColumnCount(); ++i) {
            int m = table.convertColumnIndexToModel(i);
            if (m != -1) {
                createColumn(j++);
            }
        }

        tableColumnModel.addColumnModelListener(this);
    }

    public void finalization() {
        for (int i = filterColumns.size() - 1; 0 <= i; --i) {
            removeColumn(i);
        }
        tableColumnModel.removeColumnModelListener(this);
    }

    /**
     * Returns the editor for the given column,
     * or null if such editor/column does not exist
     */
    public TableFilterEditor getFilterEditor(int viewColumn) {
        if (viewColumn < filterColumns.size()) {
            return filterColumns.get(viewColumn).getFilterEditor();
        }
        return null;
    }

    private TableFilterEditor createFilterEditor(int modelColumn) {
        return new TextFilterEditor(modelColumn);
    }

    /**
     * Creates the FilterColumnPanel for the given column number
     */
    private void createColumn(int viewIndex) {
        int columnModel = table.convertColumnIndexToModel(viewIndex);
        TableFilterEditor filterEditor = createFilterEditor(columnModel);
        FilterColumnPanel filterColumn = new FilterColumnPanel(
                tableColumnModel.getColumn(viewIndex), filterEditor);
        filterEditor.getFilterObservable().addFilterObserver(filterHandler);
        filterColumns.add(viewIndex, filterColumn);
        add(filterColumn, viewIndex);
    }

    private void removeColumn(int viewIndex) {
        FilterColumnPanel filterColumn = filterColumns.remove(viewIndex);
        filterColumn.getFilterEditor().resetFilter();
        filterColumn.getFilterEditor().getFilterObservable().removeFilterObserver(filterHandler);
        filterColumn.finalization();
        remove(filterColumn);
    }

    public void columnAdded(TableColumnModelEvent e) {
        createColumn(e.getToIndex());
        revalidate();
    }

    public void columnRemoved(TableColumnModelEvent e) {
        removeColumn(e.getFromIndex());
        revalidate();
    }

    public void columnMoved(TableColumnModelEvent e) {
        int fromIndex = e.getFromIndex();
        int toIndex = e.getToIndex();
        if (fromIndex != toIndex) {
            FilterColumnPanel fcp = filterColumns.remove(fromIndex);
            filterColumns.add(toIndex, fcp);
            remove(fcp);
            add(fcp, toIndex);
            revalidate();
        }
    }

    public void columnMarginChanged(ChangeEvent e) {
    }

    public void columnSelectionChanged(ListSelectionEvent e) {
    }

    public void resetFilters() {
        for (FilterColumnPanel filterColumnPanel : filterColumns) {
            filterColumnPanel.getFilterEditor().resetFilter();
        }
    }

    public void restoreFilters() {
        for (FilterColumnPanel filterColumnPanel : filterColumns) {
            filterColumnPanel.getFilterEditor().updateFilter();
        }
    }

    public List<FilterColumnPanel> getFilteredColumns() {
        return filterColumns;
    }
}
