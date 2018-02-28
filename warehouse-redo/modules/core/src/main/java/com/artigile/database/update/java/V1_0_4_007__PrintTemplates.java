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

import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.googlecode.flyway.core.migration.java.JavaMigration;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.PropertyKey;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Aliaksandr.Chyrtsik, 13.07.11
 */
public class V1_0_4_007__PrintTemplates implements JavaMigration {
    private static final String sqlPrintTemplateInsert = "insert into PrintTemplate (templateType, name, description, templateData) values (?, ?, ?, ?);";
    private static final String sqlSelectPrintTemplateId = "select id from PrintTemplate where templateType = ?;";
    private static final String sqlPrintTemplateFieldInsert = "insert into PrintTemplateFieldMapping (printTemplate_id, objectField, reportField, image_id) values (?, ?, ?, ?);";

    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;

        //1. Template for printing completing tasks list.
        {
            long templateId = insertTemplate(PrintTemplateType.TEMPLATE_COMPLECTING_TASKS_FOR_WORKER, "print.template.complectingTask.name", "print.template.complectingTask.description");
            //Global fields.
            insertField(templateId, "warehouse.name", "print.template.complectingTask.field.warehouse.name", null);

            //Completing task items fields.
            insertField(templateId, "items[].parcelNo", "print.template.complectingTask.field.item.orderNo", null);
            insertField(templateId, "items[].itemNo", "print.template.complectingTask.field.item.number", null);
            insertField(templateId, "items[].itemType", "print.template.complectingTask.field.item.type", null);
            insertField(templateId, "items[].itemName", "print.template.complectingTask.field.item.name", null);
            insertField(templateId, "items[].itemMisc", "print.template.complectingTask.field.item.misc", null);
            insertField(templateId, "items[].notice", "print.template.complectingTask.field.item.notice", null);
            insertField(templateId, "items[].itemArticle", "print.template.complectingTask.field.item.article", null);
            insertField(templateId, "items[].itemBarCode", "print.template.complectingTask.field.item.barcode", null);
            insertField(templateId, "items[].neededCount", "print.template.complectingTask.field.item.count", null);
            insertField(templateId, "items[].itemMeas", "print.template.complectingTask.field.item.countMeas", null);
            insertField(templateId, "items[].warehouse.name", "print.template.complectingTask.field.item.warehouse", null);
            insertField(templateId, "items[].storagePlace", "print.template.complectingTask.field.item.storagePlace", null);
            insertField(templateId, "items[].warehouseNotice", "print.template.complectingTask.field.item.warehouseNotice", null);
        }

        //2. Template for printing completing tasks stickers.
        {
            long templateId = insertTemplate(PrintTemplateType.TEMPLATE_STICKER, "print.template.complectingTaskSticker.name", "print.template.complectingTaskSticker.description");
            //Global fields.
            insertField(templateId, "warehouse.name", "print.template.complectingTask.field.warehouse.name", null);

            //Completing task items fields.
            insertField(templateId, "items[].parcelNo", "print.template.complectingTask.field.item.orderNo", null);
            insertField(templateId, "items[].itemNo", "print.template.complectingTask.field.item.number", null);
            insertField(templateId, "items[].itemType", "print.template.complectingTask.field.item.type", null);
            insertField(templateId, "items[].itemName", "print.template.complectingTask.field.item.name", null);
            insertField(templateId, "items[].itemMisc", "print.template.complectingTask.field.item.misc", null);
            insertField(templateId, "items[].notice", "print.template.complectingTask.field.item.notice", null);
            insertField(templateId, "items[].itemArticle", "print.template.complectingTask.field.item.article", null);
            insertField(templateId, "items[].itemBarCode", "print.template.complectingTask.field.item.barcode", null);
            insertField(templateId, "items[].neededCount", "print.template.complectingTask.field.item.count", null);
            insertField(templateId, "items[].itemMeas", "print.template.complectingTask.field.item.countMeas", null);
            insertField(templateId, "items[].warehouse.name", "print.template.complectingTask.field.item.warehouse", null);
            insertField(templateId, "items[].storagePlace", "print.template.complectingTask.field.item.storagePlace", null);
            insertField(templateId, "items[].warehouseNotice", "print.template.complectingTask.field.item.warehouseNotice", null);
        }
    }

    private long insertTemplate(PrintTemplateType templateType,
                                @PropertyKey(resourceBundle = "i18n.warehouse") String templateNameRes,
                                @PropertyKey(resourceBundle = "i18n.warehouse") String templateDescriptionRes)
    {
        jdbcTemplate.update(sqlPrintTemplateInsert, templateType.name(), I18nSupport.message(templateNameRes), I18nSupport.message(templateDescriptionRes), null);
        return jdbcTemplate.queryForLong(sqlSelectPrintTemplateId, templateType.name());
    }

    private void insertField(long templateId,
                             String objectField,
                             @PropertyKey(resourceBundle = "i18n.warehouse") String reportFieldRes,
                             @Nullable Integer imageId)
    {
        jdbcTemplate.update(sqlPrintTemplateFieldInsert, templateId, objectField, I18nSupport.message(reportFieldRes), imageId);
    }
}
