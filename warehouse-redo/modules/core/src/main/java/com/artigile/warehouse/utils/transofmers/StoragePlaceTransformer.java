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

import com.artigile.warehouse.bl.warehouse.StoragePlaceService;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.domain.warehouse.Warehouse;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTO;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Borisok V.V., 22.12.2008
 */
public final class StoragePlaceTransformer {
    private StoragePlaceTransformer() {
    }

    private static StoragePlaceService getStoragePlaceService(){
        return SpringServiceContext.getInstance().getStoragePlaceService();
    }

    /**
     * Make correct full tree dependence transformation from storage place to TO even if storage place have parent element
     * childStoragePlace & childStoragePlaceTO are necessary only for do not recreate their in a subtree.
     *
     * @param storagePlace
     * @param childStoragePlace
     * @param childStoragePlaceTO
     */
    public static StoragePlaceTO transformStoragePlace(StoragePlace storagePlace, StoragePlace childStoragePlace, StoragePlaceTO childStoragePlaceTO) {
        if (storagePlace != null) {
            StoragePlaceTO storagePlaceTO = new StoragePlaceTO(storagePlace.getId(),
                    storagePlace.getSign(),
                    storagePlace.getFillingDegree(),
                    storagePlace.getNotice(),
                    storagePlace.getAvailableForPosting(),
                    null,
                    null);

            Warehouse parentWarehouse = storagePlace.getWarehouse();
            if (parentWarehouse != null) {
                storagePlaceTO.setWarehouse(WarehouseTransformer.transform(parentWarehouse, storagePlace, storagePlaceTO));
            }

            StoragePlace parentStoragePlace = storagePlace.getParentStoragePlace();
            if (parentStoragePlace != null) {
                storagePlaceTO.setParentStoragePlace(transformStoragePlace(parentStoragePlace, storagePlace, storagePlaceTO));
            }

            if (childStoragePlace == null || childStoragePlaceTO == null) {
                storagePlaceTO.setStoragePlaces(transformList(storagePlace.getStoragePlaces(), null, storagePlaceTO));
            } else {
                storagePlaceTO.setStoragePlaces(transformList(storagePlace.getStoragePlaces(), null, storagePlaceTO, childStoragePlace, childStoragePlaceTO));
            }

            return storagePlaceTO;
        }

        return null;
    }

    public static StoragePlaceTO transform(StoragePlace storagePlace) {
        return transformStoragePlace(storagePlace, null, null);
    }

    public static StoragePlaceTO transform(StoragePlace storagePlace, WarehouseTO parentWarehouseTO, StoragePlaceTO parentStoragePlaceTO) {
        if (storagePlace != null) {
            StoragePlaceTO storagePlaceTO = new StoragePlaceTO(storagePlace.getId(),
                    storagePlace.getSign(),
                    storagePlace.getFillingDegree(),
                    storagePlace.getNotice(),
                    storagePlace.getAvailableForPosting(),
                    parentWarehouseTO,
                    parentStoragePlaceTO);

            storagePlaceTO.setStoragePlaces(transformList(storagePlace.getStoragePlaces(), parentWarehouseTO, storagePlaceTO));

            return storagePlaceTO;
        }

        return null;
    }

    public static List<StoragePlaceTO> transformList(List<StoragePlace> storagePlaces) {
        List<StoragePlaceTO> list = new ArrayList<StoragePlaceTO>();
        for (StoragePlace storagePlace : storagePlaces) {
            list.add(transform(storagePlace));
        }
        return list;
    }

    public static List<StoragePlaceTO> transformList(List<StoragePlace> storagePlaces, WarehouseTO parentWarehouseTO, StoragePlaceTO parentStoragePlaceTO) {
        List<StoragePlaceTO> list = new ArrayList<StoragePlaceTO>();
        for (StoragePlace storagePlace : storagePlaces) {
            list.add(transform(storagePlace, parentWarehouseTO, parentStoragePlaceTO));
        }
        return list;
    }

    public static List<StoragePlaceTO> transformList(List<StoragePlace> storagePlaces, WarehouseTO parentWarehouseTO, StoragePlaceTO parentStoragePlaceTO, StoragePlace childStoragePlace, StoragePlaceTO childStoragePlaceTO) {
        List<StoragePlaceTO> list = new ArrayList<StoragePlaceTO>();
        for (StoragePlace storagePlace : storagePlaces) {
            if (storagePlace != childStoragePlace) {
                list.add(transform(storagePlace, parentWarehouseTO, parentStoragePlaceTO));
            } else {
                list.add(childStoragePlaceTO);
            }
        }
        return list;
    }

    public static List<StoragePlaceTO> transformList(List<StoragePlace> storagePlaces, WarehouseTO parentWarehouseTO) {
        return transformList(storagePlaces, parentWarehouseTO, null);
    }

    public static List<StoragePlaceTO> transformList(List<StoragePlace> storagePlaces, WarehouseTO parentWarehouseTO, StoragePlace childStoragePlace, StoragePlaceTO childStoragePlaceTO) {
        return transformList(storagePlaces, parentWarehouseTO, null, childStoragePlace, childStoragePlaceTO);
    }

    public static List<StoragePlaceTOForReport> transformListForReport(List<StoragePlace> storagePlaces) {
        List<StoragePlaceTOForReport> list = new ArrayList<StoragePlaceTOForReport>();
        for (StoragePlace place : storagePlaces){
            list.add(transformForReport(place));
        }
        return list;
    }

    public static List<StoragePlace> transformListFromReport(List<StoragePlaceTOForReport> storagePlacesTO) {
        List<StoragePlace> list = new ArrayList<StoragePlace>();
        for (StoragePlaceTOForReport placeTO : storagePlacesTO){
            list.add(transform(placeTO));
        }
        return list;
    }

    public static StoragePlaceTOForReport transformForReport(StoragePlace place) {
        if (place == null){
            return null;
        }
        return new StoragePlaceTOForReport(place.getId(), place.getSign(), place.getFillingDegree(), place.getNotice(),
            place.getAvailableForPosting(), WarehouseTransformer.transformForReport(place.getWarehouse()));
    }

    /**
     * Make correct full tree dependence transformation from storage place TO to storage place even if
     * storage place TO have parent element
     * childStoragePlace & childStoragePlaceTO are necessary only for do not recreate their in a subtree
     *
     * @param storagePlaceTO
     */
    private static StoragePlace transformStoragePlaceTO(StoragePlaceTO storagePlaceTO) {
        if (storagePlaceTO != null) {
            StoragePlace storagePlace = getStoragePlaceService().getStoragePlaceById(storagePlaceTO.getId());
            if (storagePlace == null){
                storagePlace = new StoragePlace(storagePlaceTO.getId(), storagePlaceTO.getSign(),
                    storagePlaceTO.getFillingDegree(), storagePlaceTO.getNotice(), storagePlaceTO.isAvailableForPosting(), null, null);
            }

            storagePlace.setWarehouse(WarehouseTransformer.transform(storagePlaceTO.getWarehouse()));
            storagePlace.setParentStoragePlace(transformStoragePlaceTO(storagePlaceTO.getParentStoragePlace()));

            return storagePlace;
        }

        return null;
    }

    public static StoragePlace transform(StoragePlaceTOForReport storagePlaceTOForReport) {
        if (storagePlaceTOForReport == null){
            return null;
        }
        return getStoragePlaceService().getStoragePlaceById(storagePlaceTOForReport.getId());
    }

    private static StoragePlace transform(StoragePlaceTO storagePlaceTO, Warehouse parentWarehouse, StoragePlace parentStoragePlace) {
        if (storagePlaceTO != null) {
            StoragePlace storagePlace = getStoragePlaceService().getStoragePlaceById(storagePlaceTO.getId());
            if (storagePlace == null){
                storagePlace = new StoragePlace(storagePlaceTO.getId(), storagePlaceTO.getSign(),
                    storagePlaceTO.getFillingDegree(), storagePlaceTO.getNotice(), storagePlaceTO.isAvailableForPosting(),
                    parentWarehouse, parentStoragePlace);
            }

            storagePlace.setStoragePlaces(transformList(storagePlaceTO.getStoragePlaces(), parentWarehouse, storagePlace));

            return storagePlace;
        }

        return null;
    }

    public static List<StoragePlace> transformList(List<StoragePlaceTO> storagePlaceTOs, Warehouse parentWarehouse, StoragePlace parentStoragePlace) {
        List<StoragePlace> list = new ArrayList<StoragePlace>();
        for (StoragePlaceTO storagePlaceTO : storagePlaceTOs) {
            list.add(transform(storagePlaceTO, parentWarehouse, parentStoragePlace));
        }
        return list;
    }

    public static List<StoragePlace> transformList(List<StoragePlaceTO> storagePlaceTOs, Warehouse parentWarehouse, StoragePlace parentStoragePlace, StoragePlaceTO childStoragePlaceTO, StoragePlace childStoragePlace) {
        List<StoragePlace> list = new ArrayList<StoragePlace>();
        for (StoragePlaceTO storagePlaceTO : storagePlaceTOs) {
            if (storagePlaceTO != childStoragePlaceTO) {
                list.add(transform(storagePlaceTO, parentWarehouse, parentStoragePlace));
            } else {
                list.add(childStoragePlace);
            }
        }
        return list;
    }

    public static List<StoragePlace> transformList(List<StoragePlaceTO> storagePlaceTOs, Warehouse parentWarehouse) {
        return transformList(storagePlaceTOs, parentWarehouse, null);
    }

    public static List<StoragePlace> transformList(List<StoragePlaceTO> storagePlaceTOs, Warehouse parentWarehouse, StoragePlaceTO childStoragePlaceTO, StoragePlace childStoragePlace) {
        return transformList(storagePlaceTOs, parentWarehouse, null, childStoragePlaceTO, childStoragePlace);
    }

    public static List<StoragePlace> transformList(List<StoragePlaceTO> storagePlaceTOs, StoragePlace parentStoragePlace) {
        return transformList(storagePlaceTOs, null, parentStoragePlace);
    }

    /**
     * Updates storage place entity from it's TO. Transformation only applies to data fields (not to the referencies).
     *
     * @param storagePlace
     * @param storagePlaceTO
     */
    public static void update(StoragePlace storagePlace, StoragePlaceTO storagePlaceTO) {
        storagePlace.setSign(storagePlaceTO.getSign());
        storagePlace.setFillingDegree(storagePlaceTO.getFillingDegree());
        storagePlace.setNotice(storagePlaceTO.getNotice());
        storagePlace.setAvailableForPosting(storagePlaceTO.isAvailableForPosting());
        storagePlace.setWarehouse(WarehouseTransformer.transform(storagePlaceTO.getWarehouse()));
    }
}
