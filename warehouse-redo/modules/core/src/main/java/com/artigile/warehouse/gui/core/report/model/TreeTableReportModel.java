/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.model;

import com.artigile.warehouse.gui.core.report.controller.ReportDataSource;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.view.TreeTableReportView;
import com.artigile.warehouse.utils.reflect.SimpleObjectsFieldsProvider;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import javax.swing.tree.TreePath;
import java.util.List;

/**
 * @author Borisok V.V., 24.12.2008
 */

/**
 * Class for tree table report models.
 */
public class TreeTableReportModel extends AbstractTreeTableModel implements ReportModel {
    /**
     * Information about instance of the report.
     */
    private ReportInfo reportInfo;

    /**
     * Provider to the fields of the  displaied objects.
     */
    private SimpleObjectsFieldsProvider fieldsProvider;

    /**
     * Model of the tree, that is been displaying as the hierarchical table.
     */
    private TreeReportModel treeModel;

    private TreeTableReportView treeTableReportView;

    /**
     * Constructor.
     *
     * @param reportInfo - information, that describes the report.
     */
    public TreeTableReportModel(ReportInfo reportInfo, TreeReportModel treeModel) {
        this.reportInfo = reportInfo;
        this.fieldsProvider = new SimpleObjectsFieldsProvider(reportInfo.getDataClass(), reportInfo.getColumnFields());
        this.treeModel = treeModel;
    }

    public TreeReportModel getTreeModel(){
        return treeModel;
    }

    //================= Abstract tree table model implementation =================================
    @Override
    public int getColumnCount() {
        return reportInfo.getColumns().size();
    }

    @Override
    public String getColumnName(int column) {
        return reportInfo.getColumns().get(column).getName();
    }

    @Override
    public Object getValueAt(Object object, int columnIndex) {
        if (fieldsProvider.canProcessObject(object)) {
            return fieldsProvider.getFieldValue(object, columnIndex);
        } else {
            return null;
        }
    }

    @Override
    public Object getChild(Object parent, int index) {
        List list = treeModel.getChildren(parent);
        if (list != null) {
            return list.get(index);
        }
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        List list = treeModel.getChildren(parent);
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        List list = treeModel.getChildren(parent);
        if (list != null) {
            return list.indexOf(child);
        }
        return -1;
    }

    @Override
    public boolean isLeaf(Object node) {
        List list = treeModel.getChildren(node);
        return list == null || list.size() == 0;
    }

    @Override
    public Object getRoot() {
        return treeModel.getRoot();
    }

    //================= Report model implementation ======================================
    @Override
    public void addItem(Object newItem) {
        treeModel.addItem(newItem);
        TreePath path = treeTableReportView.getSelectedPath();
        if (path == null){
            //New root item.
            modelSupport.fireNewRoot();
        }
        else{
            //Non root item.
            modelSupport.fireTreeStructureChanged(path);
        }
    }

    @Override
    public void insertItem(Object newItem, int insertIndex) {
        //TODO: implement this of perform refactoring to delete this method.
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void deleteItem(Object item) {
        treeModel.deleteItem(item);
        TreePath path = treeTableReportView.getSelectedPath();
        modelSupport.fireTreeStructureChanged(path.getParentPath());
    }

    @Override
    public Object getItem(int itemIndex) {
        return treeModel.getItem(itemIndex);
    }

    @Override
    public void setItem(Object item) {
        //Default implementation does nothing.
    }

    @Override
    public int getItemCount() {
        return treeModel.getItemCount();
    }

    @Override
    public void fireDataChanged() {
        treeModel.fireDataChanged();
    }

    @Override
    public void fireItemDataChanged(Object item) {
        treeModel.fireItemDataChanged(item);
        //TODO: implement tree model refreshing here.
    }

    @Override
    public ReportDataSource getReportDataSource() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void refresh() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    //===================== Helpers =======================================================

    public void setTreeReportTable(TreeTableReportView treeTableReportView) {
        //TODO: Remove direct dependency from model to view.
        this.treeTableReportView = treeTableReportView;
    }
}
