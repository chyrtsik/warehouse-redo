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
 * Create template for printing movements of ware between warehouse.
 * @author Aliaksandr Chyrtsik
 * @since 25.05.13
 */
public class V1_0_4_067__MovementPrintTemplate implements JavaMigration {
    private static final String sqlPrintTemplateInsert = "insert into PrintTemplate (templateType, name) values (?, ?);";
    private static final String sqlSelectPrintTemplateId = "select id from PrintTemplate where templateType = ?;";
    private static final String sqlPrintTemplateFieldInsert = "insert into PrintTemplateFieldMapping (printTemplate_id, objectField, reportField, image_id) values (?, ?, ?, ?);";

    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;

        //Create new print template type - for movements between warehouses.
        long templateId = insertTemplate(PrintTemplateType.TEMPLATE_MOVEMENT, "print.template.movement.name");

        //Global (document-level) fields.
        insertField(templateId, "createDate", "print.template.movement.field.document.date");
        insertField(templateId, "number", "print.template.movement.field.document.number");
        insertField(templateId, "fromWarehouse.owner.unp", "print.template.movement.field.shipper.unp");
        insertField(templateId, "fromWarehouse.owner.name", "print.template.movement.field.shipper.name");
        insertField(templateId, "fromWarehouse.owner.fullName", "print.template.movement.field.shipper.fullName");
        insertField(templateId, "fromWarehouse.owner.legalAddress", "print.template.movement.field.shipper.legalAddress");
        insertField(templateId, "fromWarehouse.owner.{contractorContactsProvider:*}", "print.template.movement.field.shipper.contacts");
        insertField(templateId, "fromWarehouse.responsible.appointment", "print.template.movement.field.shipper.responsible.appointment");
        insertField(templateId, "fromWarehouse.responsible.fullName", "print.template.movement.field.shipper.responsible.fullName");
        insertField(templateId, "fromWarehouse.name", "print.template.movement.field.shipper.warehouse.name");
        insertField(templateId, "fromWarehouse.address", "print.template.movement.field.shipper.warehouse.address");
        insertField(templateId, "toWarehouse.owner.unp", "print.template.movement.field.consignee.unp");
        insertField(templateId, "toWarehouse.owner.name", "print.template.movement.field.consignee.name");
        insertField(templateId, "toWarehouse.owner.fullName", "print.template.movement.field.consignee.fullName");
        insertField(templateId, "toWarehouse.owner.legalAddress", "print.template.movement.field.consignee.legalAddress");
        insertField(templateId, "toWarehouse.owner.{contractorContactsProvider:*}", "print.template.movement.field.consignee.contacts");
        insertField(templateId, "toWarehouse.responsible.appointment", "print.template.movement.field.consignee.responsible.appointment");
        insertField(templateId, "toWarehouse.responsible.fullName", "print.template.movement.field.consignee.responsible.fullName");
        insertField(templateId, "toWarehouse.address", "print.template.movement.field.consignee.warehouse.address");
        insertField(templateId, "toWarehouse.name", "print.template.movement.field.consignee.warehouse.name");

        //Items of delivery note.
        insertField(templateId, "items[].number", "print.template.movement.field.item.number");
        insertField(templateId, "items[].itemName", "print.template.movement.field.item.name");
        insertField(templateId, "items[].countMeas.id", "print.template.movement.field.item.measureUnit.id");
        insertField(templateId, "items[].countMeas.code", "print.template.movement.field.item.measureUnit.code");
        insertField(templateId, "items[].countMeas.sign", "print.template.movement.field.item.measureUnit.sign");
        insertField(templateId, "items[].countMeas.name", "print.template.movement.field.item.measureUnit.name");
        insertField(templateId, "items[].count", "print.template.movement.field.item.quantity");
        insertField(templateId, "items[].price", "print.template.movement.field.item.price");
        insertField(templateId, "items[].totalPrice", "print.template.movement.field.item.totalPrice");
        insertField(templateId, "items[].detailBatch.{detailBatchDynamicFieldsProvider:*}", "print.template.movement.field.item.dynamicField");
    }

    private long insertTemplate(PrintTemplateType templateType, @PropertyKey(resourceBundle = "i18n.warehouse") String templateNameRes) {
        jdbcTemplate.update(sqlPrintTemplateInsert, templateType.name(), I18nSupport.message(templateNameRes));
        return jdbcTemplate.queryForLong(sqlSelectPrintTemplateId, templateType.name());
    }

    private void insertField(long templateId, String objectField, @PropertyKey(resourceBundle = "i18n.warehouse") String reportFieldRes) {
        jdbcTemplate.update(sqlPrintTemplateFieldInsert, templateId, objectField, I18nSupport.message(reportFieldRes), null);
    }
}

