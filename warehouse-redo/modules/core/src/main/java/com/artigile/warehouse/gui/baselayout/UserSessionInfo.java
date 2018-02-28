/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.baselayout;

import com.artigile.warehouse.bl.common.listeners.DataChangeAdapter;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

/**
 * @author IoaN, 01.12.2008
 */
public class UserSessionInfo {
    /**
     * Used, logged to the system.
     */
    private UserTO user;

    /**
     * Warehouse of the logged user.
     */
    private WarehouseTOForReport userWarehouse;

    /**
     * Name name of database user in this session.
     */
    private String databaseName;

    //====================== Constructors ======================================
    public UserSessionInfo(){
        //Register user this object as listener of changing user and warehouse info.
        GlobalDataChangeNotifier dataChangeNotifier = SpringServiceContext.getInstance().getDataChangeNolifier();

        //..listening changes in user data.
        dataChangeNotifier.addDataChangeListener(UserTO.class, new DataChangeAdapter(){
            @Override
            public void afterChange(Object changedData) {
                UserTO changedUser = (UserTO)changedData;
                if (user.getId() == changedUser.getId()){
                    user = changedUser;
                }
            }
        });

        //..listening changes in user's warehouse data.
        dataChangeNotifier.addDataChangeListener(WarehouseTOForReport.class, new DataChangeAdapter(){
            @Override
            public void afterChange(Object changedData) {
                WarehouseTOForReport changedWarehouse = (WarehouseTOForReport)changedData;
                if (userWarehouse.getId() == changedWarehouse.getId()){
                    userWarehouse = changedWarehouse;
                }
            }
        });
    }

    //==================== Getters and setters =================================
    public UserTO getUser() {
        return user;
    }

    public void setUser(UserTO user) {
        this.user = user;
    }

    public void setUserWarehouse(WarehouseTOForReport userWarehouse) {
        this.userWarehouse = userWarehouse;
    }

    public WarehouseTOForReport getUserWarehouse() {
        return userWarehouse;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
