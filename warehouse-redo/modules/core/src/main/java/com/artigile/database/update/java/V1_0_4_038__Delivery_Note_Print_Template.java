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
import org.jetbrains.annotations.PropertyKey;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * User: ioanbsu
 * Date: 11/21/12
 * Time: 2:18 PM
 */
public class V1_0_4_038__Delivery_Note_Print_Template implements JavaMigration {
    private static final String sqlPrintTemplateInsert = "insert into PrintTemplate (templateType, name) values (?, ?);";
    private static final String sqlSelectPrintTemplateId = "select id from PrintTemplate where templateType = ?;";
    private static final String sqlPrintTemplateFieldInsert = "insert into PrintTemplateFieldMapping (printTemplate_id, objectField, reportField, image_id) values (?, ?, ?, ?);";
    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;

        //Template for printing completing tasks list.
        long templateId = insertTemplate(PrintTemplateType.TEMPLATE_DELIVERY_NOTE, "print.template.deliveryNote.name");
        //All fields removed from this migration. Delivery note will be reinitialized in migration 1.0.4.066
    }

    private long insertTemplate(PrintTemplateType templateType,
                                @PropertyKey(resourceBundle = "i18n.warehouse") String templateNameRes) {
        jdbcTemplate.update(sqlPrintTemplateInsert, templateType.name(), I18nSupport.message(templateNameRes));
        return jdbcTemplate.queryForLong(sqlSelectPrintTemplateId, templateType.name());
    }

    private void insertField(long templateId, String objectField, @PropertyKey(resourceBundle = "i18n.warehouse") String reportFieldRes) {
        jdbcTemplate.update(sqlPrintTemplateFieldInsert, templateId, objectField, I18nSupport.message(reportFieldRes), null);
    }
}
