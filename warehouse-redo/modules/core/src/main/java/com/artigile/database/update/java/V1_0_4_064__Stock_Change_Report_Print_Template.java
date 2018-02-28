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

public class V1_0_4_064__Stock_Change_Report_Print_Template implements JavaMigration {
    private static final String sqlSelectPrintTemplateId = "select id from PrintTemplate where templateType = ?;";
    private static final String sqlPrintTemplateFieldUpdate = "update PrintTemplateFieldMapping set reportField = ? where printTemplate_id = ? and objectField = ?;";
    private static final String sqlPrintTemplateFieldInsert = "insert into PrintTemplateFieldMapping (printTemplate_id, objectField, reportField, image_id) values (?, ?, ?, ?);";
    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;

        //Template for printing storage changes report.
        long templateId = jdbcTemplate.queryForLong(sqlSelectPrintTemplateId, PrintTemplateType.TEMPLATE_STOCK_CHANGES_REPORT.name());

        //New stock report change item fields.
        insertField(templateId, "items[].storagePlace.warehouse.responsible.fullName", "print.template.stock.changes.report.field.item.warehouse.responsible.name");
        insertField(templateId, "items[].storagePlace.warehouse.responsible.appointment", "print.template.stock.changes.report.field.item.warehouse.responsible.appointment");
    }

    private void updateField(long templateId, String objectField, @PropertyKey(resourceBundle = "i18n.warehouse") String reportFieldRes) {
        jdbcTemplate.update(sqlPrintTemplateFieldUpdate, I18nSupport.message(reportFieldRes), templateId, objectField);
    }

    private void insertField(long templateId, String objectField, @PropertyKey(resourceBundle = "i18n.warehouse") String reportFieldRes) {
        jdbcTemplate.update(sqlPrintTemplateFieldInsert, templateId, objectField, I18nSupport.message(reportFieldRes), null);
    }
}
