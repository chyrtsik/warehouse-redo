/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.database.update.java;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.googlecode.flyway.core.migration.java.JavaMigration;
import org.jetbrains.annotations.PropertyKey;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Aliaksandr.Chyrtsik, 30.08.2012
 */
public class V1_0_4_030__EditWarehouseBatchItemQuantityPermission implements JavaMigration {

    private static final String sqlUserPermissionInsert = "insert into UserPermission (rightType, name) values (?, ?);";

    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;
        initUserPermission(PermissionType.EDIT_WAREHOUSE_BATCH_ITEM_QUANTITY, "permission.editWarehouseBatchItemQuantity");
    }

    private void initUserPermission(PermissionType permissionType,
                                    @PropertyKey(resourceBundle = "i18n.warehouse") String permissionNameRes) {
        jdbcTemplate.update(sqlUserPermissionInsert, permissionType.name(), I18nSupport.message(permissionNameRes));
    }
}
