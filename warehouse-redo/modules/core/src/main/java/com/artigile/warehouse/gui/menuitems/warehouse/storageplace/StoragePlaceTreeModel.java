/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.warehouse.storageplace;

import com.artigile.warehouse.gui.core.report.controller.ReportDataSource;
import com.artigile.warehouse.gui.core.report.model.TreeReportModel;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTO;

import java.util.List;

/**
 * @author Borisok V.V., 26.12.2008
 */

/**
 * Model of the storage places tree.
 */
public class StoragePlaceTreeModel implements TreeReportModel {
    /**
     * Root of the storage places tree -- warehouse.
     */
    private WarehouseTO warehouseTO;

    public StoragePlaceTreeModel(WarehouseTO warehouseTO) {
        this.warehouseTO = warehouseTO;
    }

    //================ Tree tableReport model implementation ============================================
    @Override
    public WarehouseTO getRoot() {
        return warehouseTO;
    }

    @Override
    public List getChildren(Object parent) {
        if (parent instanceof WarehouseTO) {
            //Children of the root item.
            WarehouseTO warehouseTO = (WarehouseTO) parent;
            return warehouseTO.getStoragePlaces();
        }
        else {
            //Children of one of the storage places.
            StoragePlaceTO storagePlaceTO = (StoragePlaceTO) parent;
            return storagePlaceTO.getStoragePlaces();
        }
    }

    @Override
    public void addItem(Object newItem) {
        StoragePlaceTO newStoragePlace = (StoragePlaceTO) newItem;
        if (newStoragePlace.getParentStoragePlace() == null) {
            //New root storage place
            newStoragePlace.setWarehouse(warehouseTO);
            warehouseTO.getStoragePlaces().add(newStoragePlace);
        } else {
            //New general storage place.
            newStoragePlace.getParentStoragePlace().getStoragePlaces().add(newStoragePlace);
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
        StoragePlaceTO deletingStoragePlace = (StoragePlaceTO) item;
        if (deletingStoragePlace.getParentStoragePlace() == null) {
            //Deletes root storage place
            warehouseTO.getStoragePlaces().remove(deletingStoragePlace);
        } else {
            //Deletes general storage place.
            deletingStoragePlace.getParentStoragePlace().getStoragePlaces().remove(deletingStoragePlace);
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
        //TODO: implement this.
    }

    @Override
    public ReportDataSource getReportDataSource() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void refresh() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
