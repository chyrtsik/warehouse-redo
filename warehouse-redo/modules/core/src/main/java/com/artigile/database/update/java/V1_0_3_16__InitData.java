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
public class V1_0_3_16__InitData implements JavaMigration {
    private static final String sqlPrintTemplateInsert = "insert into PrintTemplate (templateType, name, description, templateData) values (?, ?, ?, ?);";
    private static final String sqlSelectPrintTemplateId = "select id from PrintTemplate where templateType = ?;";
    private static final String sqlPrintTemplateFieldInsert = "insert into PrintTemplateFieldMapping (printTemplate_id, objectField, reportField, image_id) values (?, ?, ?, ?);";

    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;

        //Template for printing orders.
        {
            long templateId = insertTemplate(PrintTemplateType.TEMPLATE_ORDER, "print.template.order.name", "print.template.order.description");
            //Order fields.
            insertField(templateId, "number", "print.template.order.field.number", null);
            insertField(templateId, "createDate", "print.template.order.field.date", null);
            insertField(templateId, "itemsCount", "print.template.order.field.itemsCount", null);
            insertField(templateId, "totalPrice", "print.template.order.field.totalPrice", null);
            insertField(templateId, "totalPriceWords", "print.template.order.field.totalPriceWords", null);
            insertField(templateId, "vatRate", "print.template.order.field.vatRate", null);
            insertField(templateId, "vat", "print.template.order.field.vat", null);
            insertField(templateId, "vatWords", "print.template.order.field.vatWords", null);
            insertField(templateId, "totalPriceWithVat", "print.template.order.field.totalPriceWithVat", null);
            insertField(templateId, "totalPriceWithVatWords", "print.template.order.field.totalPriceWithVatWords", null);
            insertField(templateId, "notice", "print.template.order.field.notice", null);
            insertField(templateId, "contractor.name", "print.template.order.field.contractor.name", null);
            insertField(templateId, "contractor.address", "print.template.order.field.contractor.address", null);
            insertField(templateId, "contractor.unp", "print.template.order.field.contractor.unp", null);
            insertField(templateId, "contractor.okpo", "print.template.order.field.contractor.okpo", null);
            insertField(templateId, "contractor.bankAccount", "print.template.order.field.contractor.bank.account", null);
            insertField(templateId, "contractor.bankFullData", "print.template.order.field.contractor.bank.name", null);
            insertField(templateId, "contractor.bankAddress", "print.template.order.field.contractor.bank.address", null);
            insertField(templateId, "contractor.bankCode", "print.template.order.field.contractor.bank.code", null);

            //Order item fields.
            insertField(templateId, "items[].number", "print.template.order.field.item.number", null);
            insertField(templateId, "items[].name", "print.template.order.field.item.name", null);
            insertField(templateId, "items[].type", "print.template.order.field.item.type", null);
            insertField(templateId, "items[].misc", "print.template.order.field.item.misc", null);
            insertField(templateId, "items[].count", "print.template.order.field.item.count", null);
            insertField(templateId, "items[].measureSign", "print.template.order.field.item.countMeas", null);
            insertField(templateId, "items[].price", "print.template.order.field.item.price", null);
            insertField(templateId, "items[].totalPrice", "print.template.order.field.item.totalPrice", null);
            insertField(templateId, "items[].vatRate", "print.template.order.field.item.vatRate", null);
            insertField(templateId, "items[].vat", "print.template.order.field.item.vat", null);
            insertField(templateId, "items[].totalPriceWithVat", "print.template.order.field.item.totalPriceWithVat", null);
            insertField(templateId, "items[].notice", "print.template.order.field.item.notice", null);

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
