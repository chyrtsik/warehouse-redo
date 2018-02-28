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

import com.googlecode.flyway.core.migration.java.JavaMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Valery Barysok, 2013-01-26
 */
public class V1_0_4_049__DeliveryNotePrintTemplate implements JavaMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        //Do nothing. Migration was removed. This class was just kept to make list of migrations the same on all computes.
        //See migration 1.0.4.0.66
    }
}
