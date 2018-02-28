/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.database.update.java;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.plugin.PluginType;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.googlecode.flyway.core.migration.java.JavaMigration;
import org.jetbrains.annotations.PropertyKey;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Aliaksandr.Chyrtsik, 13.07.11
 */
public class V1_0_3_3__InitData implements JavaMigration {
    private static final String sqlUserPermissionInsert = "insert into UserPermission (rightType, name) values (?, ?);";
    private static final String sqlAddPermissionToAdminGroup = "insert into UserGroup_UserPermission (userGroup_Id, UserPermission_Id) select (select id from UserGroup where predefined=1), p.id from UserPermission p where p.rightType = ?;";
    private static final String sqlMenuItemInsert = "insert into MenuItem (name, pluginType, pluginClassName, viewPermission_id) values (?, ?, ?, (select id from UserPermission where rightType=?));";

    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;

        //Permission to view and edit license (story #872).
        initPermission(PermissionType.VIEW_LICENSES, "permission.viewLicenses");
        initPermission(PermissionType.EDIT_LICENSES, "permission.editLicenses");

        //Adding new permissions to admin group.
        addPermissionToAdmin(PermissionType.VIEW_LICENSES);
        addPermissionToAdmin(PermissionType.EDIT_LICENSES);

        //Create menu item for working with license.
        initMenuItem("menutree.admin.licenses.licenses", PluginType.TABLE_REPORT, "com.artigile.warehouse.gui.menuitems.admin.license.LicensesList", PermissionType.VIEW_LICENSES);
    }

    private void initPermission(PermissionType permissionType, @PropertyKey(resourceBundle = "i18n.warehouse") String permissionNameRes) {
        jdbcTemplate.update(sqlUserPermissionInsert, permissionType.name(), I18nSupport.message(permissionNameRes));
    }

    private void addPermissionToAdmin(PermissionType permissionType) {
        jdbcTemplate.update(sqlAddPermissionToAdminGroup, permissionType.name());
    }

    private void initMenuItem(@PropertyKey(resourceBundle = "i18n.warehouse") String menuNameRes, PluginType pluginType, String className, PermissionType viewPermission) {
        jdbcTemplate.update(sqlMenuItemInsert, I18nSupport.message(menuNameRes), pluginType.name(), className, viewPermission.name());
    }
}
