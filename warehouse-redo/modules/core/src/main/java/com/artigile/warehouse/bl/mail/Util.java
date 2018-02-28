/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.mail;

import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import org.hibernate.exception.SQLGrammarException;

import java.util.Hashtable;

/**
 * Utils for exception processing
 *
 * @author Borisok V.V., 16.09.2009
 */
public class Util {
    private static UserTO getCurrentUser() {
        return WareHouse.getUserSession().getUser();
    }

    /**
     * Detail message contains from 5 parts:<br>
     * 1) Common information: (Such as warehouse version, etc.)<br>
     * 2) Caused: the reason why the exception was thrown.<br>
     * 3) Stack trace.<br>
     * 4) System information.<br>
     * 5) addidtional information.<br>
     *
     * @param e the excxeption information container.
     * @param appVersion the current version of application 
     */
    public static String getDetailMessage(Throwable e, String appVersion) {
        StringBuilder detailMessage = new StringBuilder();
        UserTO user = getCurrentUser();

        // 1) Common information:(Such as warehouse version, etc.)
        detailMessage.append("1) Warehouse version: ").append(appVersion);
        detailMessage.append("\n User login: ").append(user.getLogin());
        detailMessage.append("\n User first name: ").append(user.getFirstName());
        detailMessage.append("\n User last name: ").append(user.getLastName());
        WarehouseTOForReport userWarehouse = WareHouse.getUserSession().getUserWarehouse();
        if (userWarehouse != null) {
            detailMessage.append("\n Warehouse name name: ").append(userWarehouse.getName());
        }

        // 2) Caused: the reason why the exception was thrown.<br>
        detailMessage.append("\n\n2) Caused:\n").append(e.getCause());

        // 3) Stack trace.<br>
        detailMessage.append("\n\n3) Stack Trace:\n");
        for (StackTraceElement element : e.getStackTrace()) {
            detailMessage.append(element).append("\n");
        }

        // 4) System information.<br>
        detailMessage.append("\n\n4) System information:\n");
        Hashtable sysProps = System.getProperties();
        for (Object propertyKey : sysProps.keySet()) {
            detailMessage.append(propertyKey).append(":\b\b\b").append(sysProps.get(propertyKey)).append("\n");
        }

        // 5) addidtional information.<br>
        if (e instanceof SQLGrammarException) {
            detailMessage.append("\n\n5) SQL:\n").append(((SQLGrammarException) e).getSQL());
        }

        return detailMessage.toString();
    }
}
