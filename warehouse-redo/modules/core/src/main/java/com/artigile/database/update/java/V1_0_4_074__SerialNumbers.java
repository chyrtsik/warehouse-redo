/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.database.update.java;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.plugin.PluginType;
import com.artigile.warehouse.gui.menuitems.details.serialnumbers.SerialNumberList;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.googlecode.flyway.core.migration.java.JavaMigration;
import org.jetbrains.annotations.PropertyKey;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Create task and permissions to work with serial numbers.
 * @author Aliaksandr.Chyrtsik, 22.06.2013
 */
public class V1_0_4_074__SerialNumbers implements JavaMigration {

    private static final String sqlUserPermissionInsert = "insert into UserPermission (rightType, name) values (?, ?);";
    private static final String sqlMenuItemInsert = "insert into MenuItem (name, pluginType, pluginClassName, viewPermission_id) values (?, ?, ?, (select id from UserPermission where rightType=?));";

    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;

        //1. Create pesmissions and task for working with serial numbers.
        initUserPermission(PermissionType.VIEW_SERIAL_NUMBERS, "permission.viewSerialNumbers");
        initUserPermission(PermissionType.EDIT_SERIAL_NUMBERS, "permission.editSerialNumbers");
        initMenuItem("menutree.details.serial.numbers", PluginType.TABLE_REPORT, SerialNumberList.class, PermissionType.VIEW_SERIAL_NUMBERS);
    }

    private void initUserPermission(PermissionType permissionType, @PropertyKey(resourceBundle = "i18n.warehouse") String permissionNameRes) {
        jdbcTemplate.update(sqlUserPermissionInsert, permissionType.name(), I18nSupport.message(permissionNameRes));
    }

    private void initMenuItem(@PropertyKey(resourceBundle = "i18n.warehouse") String menuNameRes, PluginType pluginType, Class pluginClass, PermissionType viewPermission) {
        jdbcTemplate.update(sqlMenuItemInsert, I18nSupport.message(menuNameRes), pluginType.name(), pluginClass.getCanonicalName(), viewPermission.name());
    }
}
