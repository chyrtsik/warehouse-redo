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

public class V1_0_4_061__Stock_Change_Report_Print_Template implements JavaMigration {
    private static final String sqlPrintTemplateInsert = "insert into PrintTemplate (templateType, name) values (?, ?);";
    private static final String sqlSelectPrintTemplateId = "select id from PrintTemplate where templateType = ?;";
    private static final String sqlPrintTemplateFieldInsert = "insert into PrintTemplateFieldMapping (printTemplate_id, objectField, reportField, image_id) values (?, ?, ?, ?);";
    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;

        //Template for printing storage changes report.
        long templateId = insertTemplate(PrintTemplateType.TEMPLATE_STOCK_CHANGES_REPORT, "print.template.stock.changes.report.name");

        //Global fields.
        insertField(templateId, "periodStart", "print.template.stock.changes.report.field.period.start");
        insertField(templateId, "periodEnd", "print.template.stock.changes.report.field.period.end");

        //Stock report change item field.
        insertField(templateId, "items[].id", "print.template.stock.changes.report.field.item.id");
        insertField(templateId, "items[].dateTime", "print.template.stock.changes.report.field.item.date");
        insertField(templateId, "items[].detailBatch.type", "print.template.stock.changes.report.field.item.type");
        insertField(templateId, "items[].detailBatch.name", "print.template.stock.changes.report.field.item.name");
        insertField(templateId, "items[].detailBatch.{detailBatchDynamicFieldsProvider:*}", "print.template.stock.changes.report.field.item.dynamicField");
        insertField(templateId, "items[].detailBatch.countMeas.code", "print.template.stock.changes.report.field.item.meas.code");
        insertField(templateId, "items[].detailBatch.countMeas.name", "print.template.stock.changes.report.field.item.meas.name");
        insertField(templateId, "items[].detailBatch.countMeas.sign", "print.template.stock.changes.report.field.item.meas.sign");
        insertField(templateId, "items[].storagePlace.warehouse.name", "print.template.stock.changes.report.field.item.warehouse.name");
        insertField(templateId, "items[].storagePlace.warehouse.owner.name", "print.template.stock.changes.report.field.item.warehouse.owner.name");
        insertField(templateId, "items[].storagePlace.warehouse.owner.{contractorContactsProvider:*}", "print.template.stock.changes.report.field.item.warehouse.owner.contacts");
        insertField(templateId, "items[].storagePlace.warehouse.responsible.fullName", "print.template.stock.changes.report.field.item.warehouse.responsible");
        insertField(templateId, "items[].storagePlace.warehouse.address", "print.template.stock.changes.report.field.item.warehouse.address");
        insertField(templateId, "items[].storagePlace.sign", "print.template.stock.changes.report.field.item.storagePlace");
        insertField(templateId, "items[].documentNumber", "print.template.stock.changes.report.field.item.document.number");
        insertField(templateId, "items[].documentDate", "print.template.stock.changes.report.field.item.document.date");
        insertField(templateId, "items[].documentName", "print.template.stock.changes.report.field.item.document.name");
        insertField(templateId, "items[].documentContractorName", "print.template.stock.changes.report.field.item.document.contractor.name");
        insertField(templateId, "items[].initialCount", "print.template.stock.changes.report.field.item.initialCount");
        insertField(templateId, "items[].initialCost", "print.template.stock.changes.report.field.item.initialCost");
        insertField(templateId, "items[].finalCount", "print.template.stock.changes.report.field.item.endCount");
        insertField(templateId, "items[].finalCost", "print.template.stock.changes.report.field.item.endCost");
        insertField(templateId, "items[].postedCount", "print.template.stock.changes.report.field.item.postedCount");
        insertField(templateId, "items[].postedCost", "print.template.stock.changes.report.field.item.postedCost");
        insertField(templateId, "items[].chargedOffCount", "print.template.stock.changes.report.field.item.chargedOffCount");
        insertField(templateId, "items[].chargedOffCost", "print.template.stock.changes.report.field.item.chargedOffCost");
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
