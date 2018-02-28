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
import com.artigile.warehouse.gui.menuitems.settings.BarCodeGenerationSettings;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.googlecode.flyway.core.migration.java.JavaMigration;
import org.jetbrains.annotations.PropertyKey;
import org.springframework.jdbc.core.JdbcTemplate;

import java.security.SecureRandom;

/**
 * Create task and permissions to manage bar code generation settings.
 * @author Aliaksandr.Chyrtsik, 05.01.2012
 */
public class V1_0_4_071__BarCodeGenerationSettings implements JavaMigration {

    private static final String sqlUserPermissionInsert = "insert into UserPermission (rightType, name) values (?, ?);";
    private static final String sqlMenuItemInsert = "insert into MenuItem (name, pluginType, pluginClassName, viewPermission_id) values (?, ?, ?, (select id from UserPermission where rightType=?));";
    private static final String sqlPropertyInsert = "insert into Property (propKey, propValue) values (?, ?);";

    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;

        //1. Create task for setting bar code generation options.
        initUserPermission(PermissionType.VIEW_BARCODE_GENERATION_SETTINGS, "permission.viewBarCodeGenerationSettings");
        initUserPermission(PermissionType.EDIT_BARCODE_GENERATION_SETTINGS, "permission.editBarCodeGenerationSettings");
        initMenuItem("menutree.settings.barcode.generation", PluginType.CUSTOM, BarCodeGenerationSettings.class, PermissionType.VIEW_BARCODE_GENERATION_SETTINGS);

        //2. Initialize bar code generation parameters.
        initProperty("barcode.prefix", String.valueOf(Math.abs(new SecureRandom().nextInt())));
        initProperty("barcode.article.length", String.valueOf(6));
        initProperty("barcode.generate.control.number", "false");
    }

    private void initUserPermission(PermissionType permissionType, @PropertyKey(resourceBundle = "i18n.warehouse") String permissionNameRes) {
        jdbcTemplate.update(sqlUserPermissionInsert, permissionType.name(), I18nSupport.message(permissionNameRes));
    }

    private void initMenuItem(@PropertyKey(resourceBundle = "i18n.warehouse") String menuNameRes, PluginType pluginType, Class pluginClass, PermissionType viewPermission) {
        jdbcTemplate.update(sqlMenuItemInsert, I18nSupport.message(menuNameRes), pluginType.name(), pluginClass.getCanonicalName(), viewPermission.name());
    }

    private void initProperty(String propKey, String propValue) {
        jdbcTemplate.update(sqlPropertyInsert, propKey, propValue);
    }
}
