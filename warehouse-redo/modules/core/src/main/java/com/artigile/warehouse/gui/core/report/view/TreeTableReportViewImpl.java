/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.view;

import com.artigile.warehouse.gui.core.report.view.cell.ReportCellEditor;
import com.artigile.warehouse.gui.core.report.view.cell.ReportCellRenderer;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.TreeTableModel;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Borisok V.V., 24.12.2008
 */

/**
 * View class for hierarchical reports.
 */
public class TreeTableReportViewImpl implements TreeTableReportView {
    private JXTreeTable treeTable;
    private JPanel mainTreeTableView;

    private Set<ReportSelectionListener> selectionListeners = new HashSet<ReportSelectionListener>();

    /**
     * Constructor. Used to create a tree table, based on programmer-defined model.
     *
     * @param treeTableModel - model, provided for the tree table.
     */
    public TreeTableReportViewImpl(TreeTableModel treeTableModel) {
        $$$setupUI$$$();
        treeTable.setTreeTableModel(treeTableModel);
        treeTable.putClientProperty("JTree.lineStyle", "Angled");

        treeTable.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                for (ReportSelectionListener listener : selectionListeners) {
                    listener.onSelectionChanged();
                }
            }
        });

        //Custom rendering and editing of table cells.
        for (int i = 0; i < treeTable.getColumnModel().getColumnCount(); i++) {
            treeTable.getColumn(i).setCellRenderer(new ReportCellRenderer());
            treeTable.getColumn(i).setCellEditor(new ReportCellEditor(new JTextField()));
        }

        MouseAdapter l = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JXTreeTable treeTable = (JXTreeTable) e.getSource();
                int row = treeTable.rowAtPoint(e.getPoint());
                if (row == -1) {
                    treeTable.clearSelection();
                }
            }
        };
        treeTable.addMouseListener(l);
    }

    @Override
    public JPanel getContentPanel() {
        return mainTreeTableView;
    }

    @Override
    public void addSelectionListener(ReportSelectionListener listener) {
        selectionListeners.add(listener);
    }

    @Override
    public void removeSelectionListener(ReportSelectionListener listener) {
        selectionListeners.remove(listener);
    }

    @Override
    public TreePath getSelectedPath() {
        return treeTable.getPathForRow(treeTable.getSelectedRow());
    }


    @Override
    public List getSelectedItems() {
        if (treeTable.getSelectedRow() == -1) {
            return null;
        }
        List<Object> items = new ArrayList<Object>();
        items.add(treeTable.getPathForRow(treeTable.convertRowIndexToModel(treeTable.getSelectedRow())).getLastPathComponent());
        return items;
    }

    @Override
    public List getDisplayedItems() {
        //Not used.
        return null;
    }

    @Override
    public Component getViewComponent() {
        return treeTable;
    }

    @Override
    public int getSelectedColumn() {
        if (treeTable.getSelectedColumnCount() != 1) {
            return -1;
        }
        return treeTable.getSelectedColumn();
    }

    private void createUIComponents() {
        treeTable = new JXTreeTable();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainTreeTableView = new JPanel();
        mainTreeTableView.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainTreeTableView.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(50, 50), null, null, 0, false));
        treeTable.setSelectionMode(0);
        treeTable.setShowHorizontalLines(false);
        treeTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        scrollPane1.setViewportView(treeTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainTreeTableView;
    }
}
