/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.column.relationship.gui;

import com.artigile.warehouse.adapter.spi.impl.DomainColumn;
import com.artigile.warehouse.adapter.spi.impl.column.relationship.RelationshipObservable;
import com.artigile.warehouse.adapter.spi.impl.column.relationship.RelationshipObserver;
import com.artigile.warehouse.adapter.spi.impl.column.relationship.TableRelationshipEditor;
import com.artigile.warehouse.adapter.spi.impl.column.relationship.gui.editor.ComboRelationshipEditor;
import com.artigile.warehouse.adapter.spi.impl.configuration.TableRelationship;
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
 * @author Valery Barysok, 6/20/11
 */

public class RelationshipColumnsControllerPanel extends JPanel implements TableColumnModelListener {

    /**
     * The list of relationship columns, sorted in the view way
     */
    private List<RelationshipColumnPanel> relationshipColumns;
    /**
     * The panel must keep a reference to the TableColumnModel,
     * to be able to 'unregister' when the controller is destroyed.
     */
    private TableColumnModel tableColumnModel;
    private JXTable table;
    TableRelationship relationshipHandler = new TableRelationship();

    private List<DomainColumn> domainColumns;

    public RelationshipColumnsControllerPanel(JXTable table, List<DomainColumn> domainColumns) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.table = table;
        this.tableColumnModel = table.getColumnModel();
        this.domainColumns = domainColumns;
        initialization();
    }

    private void addObserver(RelationshipObserver relationshipObserver) {
        for (RelationshipColumnPanel relationshipColumnPanel : relationshipColumns) {
            RelationshipObservable relationshipObservable = relationshipColumnPanel.getRelationshipEditor().getRelationshipObservable();
            relationshipObservable.addRelationshipObserver(relationshipObserver);
        }
    }

    private void removeObserver(RelationshipObserver relationshipObserver) {
        for (RelationshipColumnPanel relationshipColumnPanel : relationshipColumns) {
            RelationshipObservable relationshipObservable = relationshipColumnPanel.getRelationshipEditor().getRelationshipObservable();
            relationshipObservable.removeRelationshipObserver(relationshipObserver);
        }
    }

    public void setTableFilter(TableRelationship tableRelationship) {
        removeObserver(relationshipHandler);
        relationshipHandler = tableRelationship;
        addObserver(tableRelationship);
    }

    private void initialization() {
        relationshipColumns = new ArrayList<RelationshipColumnPanel>();
        for (int i = 0, j = 0; i < tableColumnModel.getColumnCount(); ++i) {
            int m = table.convertColumnIndexToModel(i);
            if (m != -1) {
                createColumn(j++);
            }
        }

        tableColumnModel.addColumnModelListener(this);
    }

    public void finalization() {
        for (int i = relationshipColumns.size() - 1; 0 <= i; --i) {
            removeColumn(i);
        }
        tableColumnModel.removeColumnModelListener(this);
    }

    /**
     * Returns the editor for the given column,
     * or null if such editor/column does not exist
     */
    public TableRelationshipEditor getRelationshipEditor(int viewColumn) {
        if (viewColumn < relationshipColumns.size()) {
            return relationshipColumns.get(viewColumn).getRelationshipEditor();
        }
        return null;
    }

    private ComboRelationshipEditor createRelationshipEditor(int modelColumn) {
        return new ComboRelationshipEditor(modelColumn, domainColumns);
    }

    /**
     * Creates the RelationshipColumnPanel for the given column number
     */
    private void createColumn(int viewIndex) {
        int columnModel = table.convertColumnIndexToModel(viewIndex);
        ComboRelationshipEditor relationshipEditor = createRelationshipEditor(columnModel);
        RelationshipColumnPanel relationshipColumn = new RelationshipColumnPanel(
                tableColumnModel.getColumn(viewIndex), relationshipEditor);
        relationshipEditor.getRelationshipObservable().addRelationshipObserver(relationshipHandler);
        relationshipColumns.add(viewIndex, relationshipColumn);
        add(relationshipColumn, viewIndex);
    }

    private void removeColumn(int viewIndex) {
        RelationshipColumnPanel relationshipColumn = relationshipColumns.remove(viewIndex);
        relationshipColumn.getRelationshipEditor().getRelationshipObservable().addRelationshipObserver(relationshipHandler);
        relationshipColumn.finalization();
        remove(relationshipColumn);
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
            RelationshipColumnPanel fcp = relationshipColumns.remove(fromIndex);
            relationshipColumns.add(toIndex, fcp);
            remove(fcp);
            add(fcp, toIndex);
            revalidate();
        }
    }

    public void columnMarginChanged(ChangeEvent e) {
    }

    public void columnSelectionChanged(ListSelectionEvent e) {
    }

    public void resetRelationships() {
        for (RelationshipColumnPanel relationshipColumnPanel : relationshipColumns) {
            relationshipColumnPanel.getRelationshipEditor().resetRelationship();
        }
    }

    public void restoreRelationships() {
        for (RelationshipColumnPanel relationshipColumnPanel : relationshipColumns) {
            relationshipColumnPanel.getRelationshipEditor().updateRelationship();
        }
    }

    public List<RelationshipColumnPanel> getRelationshipColumns() {
        return relationshipColumns;
    }
}
