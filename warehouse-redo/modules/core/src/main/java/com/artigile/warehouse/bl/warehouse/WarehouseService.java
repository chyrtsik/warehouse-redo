/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.warehouse;

import com.artigile.warehouse.bl.admin.UserService;
import com.artigile.warehouse.dao.StoragePlaceDAO;
import com.artigile.warehouse.dao.WarehouseDAO;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.domain.warehouse.Warehouse;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.transofmers.WarehouseTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Borisok V.V., 22.12.2008
 */

@Transactional
public class WarehouseService {
    private WarehouseDAO warehouseDAO;
    private StoragePlaceDAO storagePlaceDAO;
    private UserService userService;

    public WarehouseService() {
    }

    public void create(WarehouseTO warehouseTO) {
        Warehouse warehouse = WarehouseTransformer.transform(warehouseTO);
        WarehouseTransformer.update(warehouse, warehouseTO);
        warehouseDAO.save(warehouse);
        warehouseTO.setId(warehouse.getId());
    }

    public void update(WarehouseTO warehouseTO) {
        Warehouse warehouse = warehouseDAO.get(warehouseTO.getId());
        if (warehouse != null) {
            List<StoragePlace> placesToDelete = new ArrayList<StoragePlace>();
            WarehouseTransformer.update(warehouse, placesToDelete, warehouseTO);

            warehouseDAO.update(warehouse);

            for (StoragePlace deletedPlace : placesToDelete){
                storagePlaceDAO.remove(deletedPlace);
            }
        }
    }

    public void merge(Warehouse warehouse) {
        warehouseDAO.merge(warehouse);
    }

    public void remove(WarehouseTOForReport warehouseTO) {
        Warehouse warehouse = warehouseDAO.get(warehouseTO.getId());
        if (warehouse != null) {
            getUserService().saveUsersForWarehouse(warehouse.getId(), new ArrayList<UserTO>());
            warehouseDAO.remove(warehouse);
        }
    }

    public WarehouseTO find(long id) {
        return WarehouseTransformer.transform(warehouseDAO.get(id));
    }

    public List<WarehouseTO> getAll() {
        return WarehouseTransformer.transformList(warehouseDAO.getAllSortedByName());
    }

    public WarehouseTO getWarehouseFull(long warehouseId) {
        return WarehouseTransformer.transform(warehouseDAO.get(warehouseId));
    }

    public WarehouseTOForReport getWarehouseForReport(long warehouseId) {
        return WarehouseTransformer.transformForReport(warehouseDAO.get(warehouseId));
    }

    public List<WarehouseTOForReport> getAllForReport(){
        return WarehouseTransformer.transformListForReport(warehouseDAO.getAll());
    }

    public List<WarehouseTOForReport> getWarehousesByFilter(WarehouseFilter filter) {
        if (filter != null && filter.getComplectingUserId() != 0) {
            return getUserService().getUserMayComplectWarehouses(filter.getComplectingUserId());
        }

        return WarehouseTransformer.transformListForReport(warehouseDAO.getWarehousesByFilter(filter));
    }

    private UserService getUserService() {
        if (userService == null) {
            userService = SpringServiceContext.getInstance().getUserService();
        }
        return userService;
    }

    public boolean isUniqueWarehouseName(String name, long id) {
        Warehouse warehouse = warehouseDAO.getWarehouseByName(name);
        return warehouse == null || warehouse.getId() == id;
    }

    public List<UserTO> getAllowedUsers(long warehouseId) {
        return getUserService().getUsersForWarehouse(warehouseId);
    }

    public List<Long> getAllowedUserIds(long warehouseId) {
        List<UserTO> allowedUsers = getAllowedUsers(warehouseId);
        List<Long> allowedUserIds = new ArrayList<Long>();
        for (UserTO user : allowedUsers){
            allowedUserIds.add(user.getId());
        }
        return allowedUserIds;
    }

    public void saveAllowedUsers(long warehouseId, List<UserTO> allowedUsers) {
        getUserService().saveUsersForWarehouse(warehouseId, allowedUsers);        
    }

    public Warehouse getWarehouseById(long warehouseId) {
        return warehouseDAO.get(warehouseId); 
    }

    //========================= Spring setters ================================
    public void setWarehouseDAO(WarehouseDAO warehouseDAO) {
        this.warehouseDAO = warehouseDAO;
    }

    public void setStoragePlaceDAO(StoragePlaceDAO storagePlaceDAO) {
        this.storagePlaceDAO = storagePlaceDAO;
    }
}
