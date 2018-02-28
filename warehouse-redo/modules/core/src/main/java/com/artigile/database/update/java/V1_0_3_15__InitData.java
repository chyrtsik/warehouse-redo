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
public class V1_0_3_15__InitData implements JavaMigration {
    private static final String sqlPrintTemplateInsert = "insert into PrintTemplate (templateType, name, description, templateData) values (?, ?, ?, ?);";
    private static final String sqlSelectPrintTemplateId = "select id from PrintTemplate where templateType = ?;";
    private static final String sqlPrintTemplateFieldInsert = "insert into PrintTemplateFieldMapping (printTemplate_id, objectField, reportField, image_id) values (?, ?, ?, ?);";

    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;

        //Template for printing detail batches list.
        {
            long templateId = insertTemplate(PrintTemplateType.TEMPLATE_DETAIL_BATCHES_LIST, "print.template.detailBatches.name", "print.template.detailBatches.description");
            insertField(templateId, "items[].name", "print.template.detailBatches.field.item.name", null);
            insertField(templateId, "items[].type", "print.template.detailBatches.field.item.type", null);
            insertField(templateId, "items[].misc", "print.template.detailBatches.field.item.misc", null);
            insertField(templateId, "items[].barCode", "print.template.detailBatches.field.item.barcode", null);
            insertField(templateId, "items[].nomenclatureArticle", "print.template.detailBatches.field.item.article", null);
            insertField(templateId, "items[].buyPrice", "print.template.detailBatches.field.item.buyPrice", null);
            insertField(templateId, "items[].sellPrice", "print.template.detailBatches.field.item.sellPrice", null);
            insertField(templateId, "items[].currency.sign", "print.template.detailBatches.field.item.currency", null);
            insertField(templateId, "items[].sellPrice2", "print.template.detailBatches.field.item.sellPrice2", null);
            insertField(templateId, "items[].currency2.sign", "print.template.detailBatches.field.item.currency2", null);
            insertField(templateId, "items[].availCount", "print.template.detailBatches.field.item.availableCount", null);
            insertField(templateId, "items[].reservedCount", "print.template.detailBatches.field.item.reservedCount", null);
            insertField(templateId, "items[].count", "print.template.detailBatches.field.item.count", null);
            insertField(templateId, "items[].countMeas.sign", "print.template.detailBatches.field.item.countMeas", null);
            insertField(templateId, "items[].notice", "print.template.detailBatches.field.item.notice", null);
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
