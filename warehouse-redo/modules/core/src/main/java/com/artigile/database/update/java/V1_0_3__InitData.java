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
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.googlecode.flyway.core.migration.java.JavaMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Aliaksandr.Chyrtsik, 13.07.11
 */
public class V1_0_3__InitData implements JavaMigration {
    private static final String sqlUserPermissionInsert = "insert into UserPermission (rightType, name) values (?, ?);";

    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;
        //Permission to rollback price import (story #794).
        initPermission(PermissionType.EDIT_PRICE_IMPORT_ROLLBACK, "permission.editPriceImportRollback");
        //Permission to view and edit contractor balance (story #853).
        initPermission(PermissionType.VIEW_CONTRACTOR_BALANCE, "permission.viewContractorBalance");
        initPermission(PermissionType.EDIT_CONTRACTOR_BALANCE, "permission.editContractorBalance");
    }

    private void initPermission(PermissionType permissionType, String permissionNameRes) {
        jdbcTemplate.update(sqlUserPermissionInsert, permissionType.name(), I18nSupport.message(permissionNameRes));
    }
}
