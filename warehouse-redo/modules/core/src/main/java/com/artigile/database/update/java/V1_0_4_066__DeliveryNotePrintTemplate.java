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
 * Reinitialize template for printing delivery notes.
 * @author Aliaksandr Chyrtsik
 * @since 25.05.13
 */
public class V1_0_4_066__DeliveryNotePrintTemplate implements JavaMigration {

    private static final String sqlSelectPrintTemplateId = "select id from PrintTemplate where templateType = ?;";

    private static final String sqlDeletePrintTemplateFields = "delete from PrintTemplateFieldMapping where printTemplate_id = ?;";

    private static final String sqlPrintTemplateFieldInsert = "insert into PrintTemplateFieldMapping (printTemplate_id, objectField, reportField, image_id) values (?, ?, ?, ?);";

    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;

        long templateId = getTemplate(PrintTemplateType.TEMPLATE_DELIVERY_NOTE);
        deleteTemplateFields(templateId);

        //Global (document-level) fields.
        insertField(templateId, "chargeOffDate", "print.template.delivery.note.field.date");
        insertField(templateId, "blankNumber", "print.template.delivery.note.field.blankNumber");
        insertField(templateId, "documentDate", "print.template.delivery.note.field.document.date");
        insertField(templateId, "documentNumber", "print.template.delivery.note.field.document.number");
        insertField(templateId, "documentName", "print.template.delivery.note.field.document.name");
        insertField(templateId, "currency.id", "print.template.delivery.note.field.currency.id");
        insertField(templateId, "currency.sign", "print.template.delivery.note.field.currency.sign");
        insertField(templateId, "currency.name", "print.template.delivery.note.field.currency.name");
        insertField(templateId, "shipper.unp", "print.template.delivery.note.field.shipper.unp");
        insertField(templateId, "shipper.fullName", "print.template.delivery.note.field.shipper.fullName");
        insertField(templateId, "shipper.name", "print.template.delivery.note.field.shipper.name");
        insertField(templateId, "shipper.legalAddress", "print.template.delivery.note.field.shipper.legalAddress");
        insertField(templateId, "shipper.{contractorContactsProvider:*}", "print.template.delivery.note.field.shipper.contacts");
        insertField(templateId, "chargeOff.warehouse.responsible.appointment", "print.template.delivery.note.field.shipper.warehouse.responsible.appointment");
        insertField(templateId, "chargeOff.warehouse.responsible.fullName", "print.template.delivery.note.field.shipper.warehouse.responsible.fullName");
        insertField(templateId, "contractor.unp", "print.template.delivery.note.field.consignee.unp");
        insertField(templateId, "contractor.name", "print.template.delivery.note.field.consignee.name");
        insertField(templateId, "contractor.fullName", "print.template.delivery.note.field.consignee.fullName");
        insertField(templateId, "contractor.legalAddress", "print.template.delivery.note.field.consignee.legalAddress");
        insertField(templateId, "destinationWarehouse.responsible.appointment", "print.template.delivery.note.field.consignee.warehouse.responsible.appointment");
        insertField(templateId, "destinationWarehouse.responsible.fullName", "print.template.delivery.note.field.consignee.warehouse.responsible.fullName");
        insertField(templateId, "contractor.{contractorContactsProvider:*}", "print.template.delivery.note.field.consignee.contacts");
        insertField(templateId, "shippingCustomer.unp", "print.template.delivery.note.field.shipping.customer.unp");
        insertField(templateId, "shippingCustomer.name", "print.template.delivery.note.field.shipping.customer.name");
        insertField(templateId, "shippingCustomer.fullName", "print.template.delivery.note.field.shipping.customer.fullName");
        insertField(templateId, "shippingCustomer.legalAddress", "print.template.delivery.note.field.shipping.customer.legalAddress");
        insertField(templateId, "shippingCustomer.{contractorContactsProvider:*}", "print.template.delivery.note.field.shipping.customer.contacts");
        insertField(templateId, "displayCar", "print.template.delivery.note.field.display.car");
        insertField(templateId, "driverFullName", "print.template.delivery.note.field.car.driver");
        insertField(templateId, "carTrailer", "print.template.delivery.note.field.car.trailer");
        insertField(templateId, "carOwner", "print.template.delivery.note.field.car.owner");
        insertField(templateId, "waybillNumber", "print.template.delivery.note.field.waybillNumber");
        insertField(templateId, "ridesCount", "print.template.delivery.note.field.ridesCount");
        insertField(templateId, "loadingPoint", "print.template.delivery.note.field.loading.point");
        insertField(templateId, "unloadingPoint", "print.template.delivery.note.field.unloading.point");

        //Items of delivery note.
        insertField(templateId, "items[].number", "print.template.delivery.note.field.item.number");
        insertField(templateId, "items[].itemName", "print.template.delivery.note.field.item.name");
        insertField(templateId, "items[].countMeas.id", "print.template.delivery.note.field.item.countMeasure.id");
        insertField(templateId, "items[].countMeas.code", "print.template.delivery.note.field.item.countMeasure.code");
        insertField(templateId, "items[].countMeas.sign", "print.template.delivery.note.field.item.countMeasure.sign");
        insertField(templateId, "items[].countMeas.name", "print.template.delivery.note.field.item.countMeasure.name");
        insertField(templateId, "items[].count", "print.template.delivery.note.field.item.quantity");
        insertField(templateId, "items[].price", "print.template.delivery.note.field.item.price");
        insertField(templateId, "items[].totalPrice", "print.template.delivery.note.field.item.cost");
        insertField(templateId, "items[].vatRate", "print.template.delivery.note.field.item.vatRate");
        insertField(templateId, "items[].totalVat", "print.template.delivery.note.field.item.vatRateSum");
        insertField(templateId, "items[].totalPriceWithVat", "print.template.delivery.note.field.item.costVatRate");
        insertField(templateId, "items[].packagesCount", "print.template.delivery.note.field.item.packagesCount");
        insertField(templateId, "items[].weight", "print.template.delivery.note.field.item.weight");
        insertField(templateId, "items[].warehouseBatchNotice", "print.template.delivery.note.field.item.note");
        insertField(templateId, "items[].detailBatch.{detailBatchDynamicFieldsProvider:*}", "print.template.delivery.note.field.item.dynamicField");
    }

    private void deleteTemplateFields(long templateId) {
        jdbcTemplate.update(sqlDeletePrintTemplateFields, templateId);
    }

    private long getTemplate(PrintTemplateType templateType) {
        return jdbcTemplate.queryForLong(sqlSelectPrintTemplateId, templateType.name());
    }

    private void insertField(long templateId, String objectField, @PropertyKey(resourceBundle = "i18n.warehouse") String reportFieldRes) {
        jdbcTemplate.update(sqlPrintTemplateFieldInsert, templateId, objectField, I18nSupport.message(reportFieldRes), null);
    }
}

