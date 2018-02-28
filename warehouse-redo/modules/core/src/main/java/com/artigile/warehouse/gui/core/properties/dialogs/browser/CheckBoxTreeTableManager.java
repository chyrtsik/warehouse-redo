/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.browser;

import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.report.model.TreeReportModel;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTreeTable;

import javax.swing.table.TableCellRenderer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Borisok V.V., 23.09.2009
 *
 * Component-wrapper adding checkboxes to tree table compoment.
 */
public class CheckBoxTreeTableManager implements CheckBoxTreeCellModel {

    private boolean multiSelect;

    private boolean checkRecursively;

    private TreeReportModel treeModel;

    private Set<Object> checkedItems = new HashSet<Object>();

    private List<Object> nestedItems = new ArrayList<Object>();

    /**
     * @param treeTable tree table component itself.
     * @param multiSelect is true then multiple items are allowed to be selected.
     * @param checkRecursively is true then after user confirmation children items are selected after selection
     * of parent item.
     */
    public CheckBoxTreeTableManager(JXTreeTable treeTable, TreeReportModel treeModel, boolean multiSelect, boolean checkRecursively) {
        this.multiSelect = multiSelect;
        this.checkRecursively = checkRecursively;
        this.treeModel = treeModel;
        
        //Installing cell editor.
        treeTable.setTreeCellRenderer(new CheckBoxTreeCellRenderer(this));
        JXTree tree = getTree(treeTable);
        tree.setEditable(true);
        tree.setCellEditor(new CheckBoxTreeCellEditor(this));
        treeTable.updateUI();
    }

    public Set<Object> getCheckedItems(){
        return checkedItems;
    }

    /**
     * JXTreeTable has not implementing setTreeCellEditor
     * like setTreeCellRenderer which exists. Make a hack
     * to get a JXTree for having ability to make more tunes
     * @param treeTable
     * @return
     */
    private static JXTree getTree(JXTreeTable treeTable) {
        int hc = treeTable.getHierarchicalColumn();
        assert hc >= 0 : "must be not negative";
        if (hc >= 0) {
            TableCellRenderer renderer = treeTable.getCellRenderer(hc, 0);
            if (renderer instanceof JXTree) {
                return (JXTree) renderer;
            }
        }

        return null;
    }

    @Override
    public boolean isItemChecked(Object item) {
        return checkedItems.contains(item);
    }

    /**
     * Recursion check of nested nodes in the tree
      * @param items
     */
    private void recursionCheck(List <Object> items) {
        for (Object nestedItem: items) {
            checkedItems.add(nestedItem);
            if (!treeModel.getChildren(nestedItem).isEmpty()) recursionCheck(treeModel.getChildren(nestedItem));
        }
    }

    @Override
    public void checkItem(Object item) {
        checkedItems.add(item);
        nestedItems = treeModel.getChildren(item);
        if(!nestedItems.isEmpty()) {
            if (MessageDialogs.showConfirm(item.toString(), I18nSupport.message("inventarization.check.nested.storage.places"))) {
                recursionCheck(nestedItems);
            }
        } 
    }

    /**
     * Recursion un check of nested nodes in the tree
     * @param items
     */
    private void recursionUnCheck(List <Object> items) {
         for (Object nestedItem: items) {
            checkedItems.remove(nestedItem);
            if (!treeModel.getChildren(nestedItem).isEmpty()) recursionUnCheck(treeModel.getChildren(nestedItem));
        }
    }

    @Override
    public void unCheckItem(Object item) {
        checkedItems.remove(item);
        nestedItems = treeModel.getChildren(item);
        if(!nestedItems.isEmpty()) {
            if (MessageDialogs.showConfirm(item.toString(), I18nSupport.message("inventarization.uncheck.nested.storage.places"))) {
                recursionUnCheck(nestedItems);
            }
        }
    }
}
