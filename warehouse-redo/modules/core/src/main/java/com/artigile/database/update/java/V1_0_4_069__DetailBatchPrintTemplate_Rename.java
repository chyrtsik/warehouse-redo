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

import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.googlecode.flyway.core.migration.java.JavaMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Aliaksandr Chyrtsik
 * @since 25.05.13
 */
public class V1_0_4_069__DetailBatchPrintTemplate_Rename implements JavaMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        //Just rename detail batch print template to conform new naming in UI.
        jdbcTemplate.update(
                "update PrintTemplate set name=? where templateType=?;",
                I18nSupport.message("print.template.detailBatches.name"),
                PrintTemplateType.TEMPLATE_DETAIL_BATCHES_LIST.name()
        );
    }
}

