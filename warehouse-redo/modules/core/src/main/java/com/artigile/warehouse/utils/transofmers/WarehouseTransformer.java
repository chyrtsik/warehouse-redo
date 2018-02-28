/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.bl.contractors.ContractorService;
import com.artigile.warehouse.bl.warehouse.WarehouseService;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.domain.warehouse.Warehouse;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

import java.util.*;

/**
 * @author Borisok V.V., 22.12.2008
 */
public final class WarehouseTransformer {
    private WarehouseTransformer() {
    }

    private static WarehouseService getWarehouseService(){
        return SpringServiceContext.getInstance().getWarehouseService();
    }

    public static WarehouseTO transform(Warehouse warehouse, StoragePlace childStoragePlace, StoragePlaceTO childStoragePlaceTO) {
        if (warehouse == null){
            return null;
        }
        WarehouseTO warehouseTO = new WarehouseTO();
        update(warehouseTO, warehouse);
        warehouseTO.setStoragePlaces(StoragePlaceTransformer.transformList(warehouse.getStoragePlaces(), warehouseTO, childStoragePlace, childStoragePlaceTO));
        return warehouseTO;
    }

    private static void update(WarehouseTOForReport warehouseTO, Warehouse warehouse) {
        warehouseTO.setId(warehouse.getId());
        warehouseTO.setName(warehouse.getName());
        warehouseTO.setAddress(warehouse.getAddress());
        warehouseTO.setOwner(ContractorTransformer.transformContractor(warehouse.getOwner()));
        warehouseTO.setResponsible(ContractorTransformer.transformContact(warehouse.getResponsible()));
        warehouseTO.setNotice(warehouse.getNotice());
        warehouseTO.setUsualPrinter(warehouse.getUsualPrinter());
        warehouseTO.setStickerPrinter(warehouse.getStickerPrinter());
    }

    private static void transformWarehouseStoragePlaces(WarehouseTO warehouseTO, List<StoragePlace> storagePlaces) {
        List<StoragePlaceTO> list = warehouseTO.getStoragePlaces();
        for (StoragePlace storagePlace : storagePlaces) {
            if (storagePlace.getParentStoragePlace() == null) {
                list.add(StoragePlaceTransformer.transform(storagePlace, warehouseTO, null));
            }
        }
    }

    public static WarehouseTO transform(Warehouse warehouse) {
        if (warehouse == null) {
            return null;
        }
        WarehouseTO warehouseTO = new WarehouseTO();
        update(warehouseTO, warehouse);
        transformWarehouseStoragePlaces(warehouseTO, warehouse.getStoragePlaces());
        return warehouseTO;
    }

    public static List<WarehouseTO> transformList(List<Warehouse> warehouses) {
        List<WarehouseTO> list = new ArrayList<WarehouseTO>();
        for (Warehouse warehouse : warehouses) {
            list.add(transform(warehouse));
        }
        return list;
    }

    public static Warehouse transform(WarehouseTOForReport warehouseTO) {
        if (warehouseTO == null) {
            return null;
        }
        Warehouse warehouse = getWarehouseService().getWarehouseById(warehouseTO.getId());
        if (warehouse == null){
            warehouse = new Warehouse();
        }
        return warehouse;
    }

    public static List<WarehouseTOForReport> transformListForReport(Collection<Warehouse> warehouses) {
        List<WarehouseTOForReport> warehousesTO = new ArrayList<WarehouseTOForReport>();
        for (Warehouse warehouse : warehouses){
            warehousesTO.add(transformForReport(warehouse));
        }
        return warehousesTO;
    }

    public static WarehouseTOForReport transformForReport(Warehouse warehouse) {
        if (warehouse == null){
            return null;
        }
        WarehouseTOForReport warehouseTO = new WarehouseTOForReport();
        update(warehouseTO, warehouse);
        return warehouseTO;
    }

    /**
     * Synchronizes persistent warehouse with it's edited copy.
     * @param warehouse (in, out) warehouse entity to be updates.
     * @param warehouseTO (in) DTO with fresh data.
     */
    public static void update(Warehouse warehouse, WarehouseTO warehouseTO) {
        update(warehouse, new ArrayList<StoragePlace>(), warehouseTO);   
    }

    /**
     * Synchronizes persistent warehouse with it's edited copy.
     * @param warehouse (in, out) warehouse entity to be updates.
     * @param placesToDelete (in, out) storage places list, that should be deleted from warehouse.
     * @param warehouseTO (in) DTO with fresh data.
     */
    public static void update(Warehouse warehouse, List<StoragePlace> placesToDelete, WarehouseTO warehouseTO) {
        ContractorService contractorService = SpringServiceContext.getInstance().getContractorService();

        warehouse.setName(warehouseTO.getName());
        warehouse.setAddress(warehouseTO.getAddress());
        warehouse.setOwner(warehouseTO.getOwner() == null ? null : contractorService.getContractorById(warehouseTO.getOwner().getId()));
        warehouse.setResponsible(warehouseTO.getResponsible() == null ? null : contractorService.getContractorContractById(warehouseTO.getResponsible().getId()));
        warehouse.setNotice(warehouseTO.getNotice());
        warehouse.setUsualPrinter(warehouseTO.getUsualPrinter());
        warehouse.setStickerPrinter(warehouseTO.getStickerPrinter());

        updateStoragePlaces(warehouse, placesToDelete, warehouseTO);
    }

    private static void updateStoragePlaces(Warehouse destWarehouse, List<StoragePlace> placesToDelete, WarehouseTO srcWarehouseTO) {
        Map<Long, StoragePlace> destPlaces = getAllStoragePlaces(destWarehouse); //All storage places of the existing warehouse.

        Map<Long, StoragePlaceTO> editedPlaces = new HashMap<Long, StoragePlaceTO>(); //Map of edited storage places.
        List<StoragePlaceTO> newPlaces = new ArrayList<StoragePlaceTO>(); //List of new storage places.
        enumAllStoragePlacesFromTO(srcWarehouseTO, editedPlaces, newPlaces);

        //Synchronizes storage places of persistent and edited warehouse by comparing ald and new lists of the
        //storage places.
        //1. Updating edited storage places.
        for (StoragePlaceTO editedPlace : editedPlaces.values()) {
            StoragePlaceTransformer.update(destPlaces.get(editedPlace.getId()), editedPlace);
            destPlaces.remove(editedPlace.getId());
        }
        //2. Deleting deleted storage places.
        for (StoragePlace deletedPlace : destPlaces.values()) {
            deletedPlace.deleteStoragePlace();
            placesToDelete.add(deletedPlace);
        }
        //3. Creating new storage places.
        for (StoragePlaceTO newPlaceTO : newPlaces) {
            StoragePlace newPlace = new StoragePlace();
            StoragePlaceTransformer.update(newPlace, newPlaceTO);
            newPlace.addStoragePlace(destWarehouse, newPlaceTO.getParentStoragePlace() == null ? null : newPlaceTO.getParentStoragePlace().getSign());
        }
    }

    private static void enumAllStoragePlacesFromTO(WarehouseTO srcWarehouseTO, Map<Long, StoragePlaceTO> editedPlaces, List<StoragePlaceTO> newPlaces) {
        for (StoragePlaceTO place : srcWarehouseTO.getStoragePlaces()) {
            enumAllStoragePlacesFromTO(place, editedPlaces, newPlaces);
        }
    }

    private static void enumAllStoragePlacesFromTO(StoragePlaceTO place, Map<Long, StoragePlaceTO> editedPlaces, List<StoragePlaceTO> newPlaces) {
        //To simplify storing of new places we use preorder walk during the tree traversal.
        if (place.isNew()) {
            newPlaces.add(place);
        }
        else{
            editedPlaces.put(place.getId(), place);
        }
        for (StoragePlaceTO child : place.getStoragePlaces()) {
            enumAllStoragePlacesFromTO(child, editedPlaces, newPlaces);
        }
    }

    private static Map<Long, StoragePlace> getAllStoragePlaces(Warehouse destWarehouse) {
        Map<Long, StoragePlace> places = new HashMap<Long, StoragePlace>();
        for (StoragePlace place : destWarehouse.getStoragePlaces()) {
            enumAllStoragePlaces(place, places);
        }
        return places;
    }

    private static void enumAllStoragePlaces(StoragePlace place, Map<Long, StoragePlace> places) {
        places.put(place.getId(), place);
        for (StoragePlace child : place.getStoragePlaces()) {
            enumAllStoragePlaces(child, places);
        }
    }
}
