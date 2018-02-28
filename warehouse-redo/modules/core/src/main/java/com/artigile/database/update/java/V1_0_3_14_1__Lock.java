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

import com.googlecode.flyway.core.migration.java.JavaMigration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Valery Barysok, 10/2/11
 */

public class V1_0_3_14_1__Lock implements JavaMigration {
    private static final String ddlRemoveColumn = "alter table LockGroup_Owner_Inventorization drop column lockOwnerId";

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        try {
            jdbcTemplate.execute(ddlRemoveColumn);
        } catch (DataAccessException ignored) {
            // database was created from scratch
        }
    }
}
