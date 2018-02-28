/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.catalog;

import com.artigile.warehouse.gui.core.report.controller.ReportDataSource;
import com.artigile.warehouse.gui.core.report.model.TreeReportModel;
import com.artigile.warehouse.utils.dto.details.DetailCatalogStructureTO;
import com.artigile.warehouse.utils.dto.details.DetailGroupTO;

import java.util.List;

/**
 * @author Shyrik, 04.01.2009
 */

/**
 * Model for working with details catalog structure.
 */
public class DetailCatalogStructureTreeModel implements TreeReportModel {
    /**
     * Catalog structure, access to which is provided by the model.
     */
    DetailCatalogStructureTO catalogStructure;

    public DetailCatalogStructureTreeModel(DetailCatalogStructureTO catalogStructure) {
        this.catalogStructure = catalogStructure;
    }

    @Override
    public Object getRoot() {
        return catalogStructure;
    }

    @Override
    public List getChildren(Object parent) {
        if (parent instanceof DetailCatalogStructureTO){
            //Root groups of the catalog are needed.
            DetailCatalogStructureTO catalogRoot = (DetailCatalogStructureTO)parent;
            return catalogRoot.getRootGroups();
        }
        else{
            //Subgroups of one of the groups are needed.
            DetailGroupTO group = (DetailGroupTO)parent;
            return group.getChildGroups();
        }
    }

    @Override
    public void addItem(Object newItem) {
        DetailGroupTO newGroup = (DetailGroupTO)newItem;
        if (newGroup.isRoot()){
            //Adding new root group.
            catalogStructure.getRootGroups().add(newGroup);
        }
        else{
            //Adding new child group of some group.
            newGroup.getParentGroup().getChildGroups().add(newGroup);
        }
        fireDataChanged();
    }

    @Override
    public void insertItem(Object newItem, int insertIndex) {
        //TODO: implement this of perform refactoring to delete this method.
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void deleteItem(Object item) {
        DetailGroupTO group = (DetailGroupTO)item;
        if (group.isRoot()){
            //Deleting root group.
            catalogStructure.getRootGroups().remove(group);
        }
        else{
            //Adding new child group of some group.
            group.getParentGroup().getChildGroups().remove(group);
        }
        fireDataChanged();
    }

    @Override
    public Object getItem(int itemIndex) {
        //TODO: this method is not used. It's a sign to refactor class model.
        return null;
    }

    @Override
    public void setItem(Object item) {
        //TODO: now this method does nothign. Think about implementing it.
    }

    @Override
    public int getItemCount() {
        //TODO: this method not used...
        return 0;
    }

    @Override
    public void fireDataChanged() {
        //TODO: implement this.
    }

    @Override
    public void fireItemDataChanged(Object item) {
        //TODO: impelement this.
    }

    @Override
    public ReportDataSource getReportDataSource() {
        return null; //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void refresh() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
