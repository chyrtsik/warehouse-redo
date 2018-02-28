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
 * Create template for printing serial numbers.
 * @author Aliaksandr Chyrtsik
 * @since 25.05.13
 */
public class V1_0_4_075__SerialNumbers_PrintTemplate implements JavaMigration {
    private static final String sqlPrintTemplateInsert = "insert into PrintTemplate (templateType, name) values (?, ?);";
    private static final String sqlSelectPrintTemplateId = "select id from PrintTemplate where templateType = ?;";
    private static final String sqlPrintTemplateFieldInsert = "insert into PrintTemplateFieldMapping (printTemplate_id, objectField, reportField, image_id) values (?, ?, ?, ?);";

    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;

        long templateId = insertTemplate(PrintTemplateType.TEMPLATE_SERIAL_NUMBER_LIST, "print.template.serialNumbers.name");

        insertField(templateId, "items[].id", "print.template.serialNumbers.field.item.serialNumber.id");
        insertField(templateId, "items[].barCode", "print.template.serialNumbers.field.item.serialNumber.barCode");
        insertField(templateId, "items[].{serialNumberDynamicFieldsProvider:*}", "print.template.serialNumbers.field.item.serialNumber.dynamicField");
        insertField(templateId, "items[].detail.{detailBatchDynamicFieldsProvider:*}", "print.template.serialNumbers.field.item.detailBatch.dynamicField");
        insertField(templateId, "items[].detail.name", "print.template.serialNumbers.field.item.detailBatch.name");
        insertField(templateId, "items[].detail.type", "print.template.serialNumbers.field.item.detailBatch.type");
        insertField(templateId, "items[].detail.misc", "print.template.serialNumbers.field.item.detailBatch.misc");
        insertField(templateId, "items[].detail.notice", "print.template.serialNumbers.field.item.detailBatch.notice");
        insertField(templateId, "items[].detail.barCode", "print.template.serialNumbers.field.item.detailBatch.barcode");
        insertField(templateId, "items[].detail.barCodeImage", "print.template.serialNumbers.field.item.detailBatch.barcode.image");
        insertField(templateId, "items[].detail.nomenclatureArticle", "print.template.serialNumbers.field.item.detailBatch.article");
        insertField(templateId, "items[].detail.count", "print.template.serialNumbers.field.item.detailBatch.count");
        insertField(templateId, "items[].detail.reservedCount", "print.template.serialNumbers.field.item.detailBatch.reservedCount");
        insertField(templateId, "items[].detail.availCount", "print.template.serialNumbers.field.item.detailBatch.availableCount");
        insertField(templateId, "items[].detail.countMeas.id", "print.template.serialNumbers.field.item.detailBatch.countMeas.id");
        insertField(templateId, "items[].detail.countMeas.code", "print.template.serialNumbers.field.item.detailBatch.countMeas.code");
        insertField(templateId, "items[].detail.countMeas.sign", "print.template.serialNumbers.field.item.detailBatch.countMeas.sign");
        insertField(templateId, "items[].detail.countMeas.name", "print.template.serialNumbers.field.item.detailBatch.countMeas.name");
        insertField(templateId, "items[].detail.buyPrice", "print.template.serialNumbers.field.item.detailBatch.buyPrice");
        insertField(templateId, "items[].detail.sellPrice", "print.template.serialNumbers.field.item.detailBatch.sellPrice");
        insertField(templateId, "items[].detail.currency.id", "print.template.serialNumbers.field.item.detailBatch.currency.id");
        insertField(templateId, "items[].detail.currency.sign", "print.template.serialNumbers.field.item.detailBatch.currency.sign");
        insertField(templateId, "items[].detail.currency.name", "print.template.serialNumbers.field.item.detailBatch.currency.name");
        insertField(templateId, "items[].detail.sellPrice2", "print.template.serialNumbers.field.item.detailBatch.sellPrice2");
        insertField(templateId, "items[].detail.currency.id", "print.template.serialNumbers.field.item.detailBatch.currency2.id");
        insertField(templateId, "items[].detail.currency.sign", "print.template.serialNumbers.field.item.detailBatch.currency2.sign");
        insertField(templateId, "items[].detail.currency.name", "print.template.serialNumbers.field.item.detailBatch.currency2.name");
    }

    private long insertTemplate(PrintTemplateType templateType, @PropertyKey(resourceBundle = "i18n.warehouse") String templateNameRes) {
        jdbcTemplate.update(sqlPrintTemplateInsert, templateType.name(), I18nSupport.message(templateNameRes));
        return jdbcTemplate.queryForLong(sqlSelectPrintTemplateId, templateType.name());
    }

    private void insertField(long templateId, String objectField, @PropertyKey(resourceBundle = "i18n.warehouse") String reportFieldRes) {
        jdbcTemplate.update(sqlPrintTemplateFieldInsert, templateId, objectField, I18nSupport.message(reportFieldRes), null);
    }
}

