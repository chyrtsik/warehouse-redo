/*
 * Copyright (c) 2007-2012 Artigile.
 * Software art development company.
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
public class V1_0_4_035__Print_Template_Add_BarCode implements JavaMigration {
    private static final String sqlSelectPrintTemplateId = "select id from PrintTemplate where templateType = ?;";
    private static final String sqlPrintTemplateFieldInsert = "insert into PrintTemplateFieldMapping (printTemplate_id, objectField, reportField, image_id) values (?, ?, ?, ?);";

    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;

        //Add bar code to template for printing detail batches list.
        long templateId = jdbcTemplate.queryForLong(sqlSelectPrintTemplateId, PrintTemplateType.TEMPLATE_DETAIL_BATCHES_LIST.name());
        insertField(templateId, "items[].barCodeImage", "print.template.detailBatches.field.item.barcode.image", null);
    }

    private void insertField(long templateId,
                             String objectField,
                             @PropertyKey(resourceBundle = "i18n.warehouse") String reportFieldRes,
                             @Nullable Integer imageId)
    {
        jdbcTemplate.update(sqlPrintTemplateFieldInsert, templateId, objectField, I18nSupport.message(reportFieldRes), imageId);
    }
}
