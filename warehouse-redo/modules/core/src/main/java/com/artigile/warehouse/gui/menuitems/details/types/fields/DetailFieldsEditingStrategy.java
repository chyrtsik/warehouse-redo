/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.types.fields;

import com.artigile.warehouse.domain.details.DetailFieldType;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.core.report.model.ReportModel;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 14.12.2008
 */

/**
 * Strategy for editing list of detail type fields.
 * Fields data stored info array, not to the persistent storage.
 */
public class DetailFieldsEditingStrategy implements ReportEditingStrategy {
    private final boolean canEdit;
    private final boolean useSortAndGroupNumbers;
    private final int maxFieldsCount;
    private final DetailFieldType[] availableFields;

    public DetailFieldsEditingStrategy(boolean canEdit, boolean useSortAndGroupNumbers, int maxFieldsCount, DetailFieldType[] availableFields) {
        this.canEdit = canEdit;
        this.useSortAndGroupNumbers = useSortAndGroupNumbers;
        this.maxFieldsCount = maxFieldsCount;
        this.availableFields = availableFields;
    }

    //======================== Editing strategy implementation =======================
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateFieldCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateFieldCommand());
        commands.add(new DeleteFieldCommand());
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new OpenFieldCommand());
        }
    }

    //==================== Commands ===========================================
    private class CreateFieldCommand extends CreateCommand {
        protected CreateFieldCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            if (context.getReportModel().getItemCount() >= maxFieldsCount){
                throw new ReportCommandException(I18nSupport.message("detail.field.properties.cannot.create.one.more.field"));
            }

            //Try to create new detail field.
            DetailFieldTO field = new DetailFieldTO();
            PropertiesForm prop = new DetailFieldForm(field, useSortAndGroupNumbers, true,
                    availableFields, new DetailFieldChecker(context.getReportModel()));
            if (Dialogs.runProperties(prop)) {
                return field;
            }
            return null;
        }
    }

    private class DeleteFieldCommand extends DeleteCommand {
        protected DeleteFieldCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            //Searching for the field to delete in the data array
            DetailFieldTO fieldToDelete = (DetailFieldTO)deletingItem;
            if (fieldToDelete.isPredefined()){
                //We cannot remove predefined fields.
                throw new ReportCommandException(I18nSupport.message("detail.field.properties.cannot.delete.predefined.field"));
            }
            return true;
        }
    }

    private class OpenFieldCommand extends PropertiesCommandBase {
        protected OpenFieldCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            //Open detail field properties.
            DetailFieldTO field = (DetailFieldTO)editingItem;
            PropertiesForm prop = new DetailFieldForm(field, useSortAndGroupNumbers, getEditAvailability().isAvailable(context),
                    availableFields, new DetailFieldChecker(context.getReportModel()));
            return Dialogs.runProperties(prop);
        }
    }

    //====================================== Helpers ========================================
    public class DetailFieldChecker {
        /**
         * Model of the report for which this checkes is used.
         */
        private ReportModel reportModel;

        public DetailFieldChecker(ReportModel reportModel){
            this.reportModel = reportModel;
        }

        /**
         * Checks, if the given value will be unique in the list of detail type fields.
         *
         * @param name         - new name of the field.
         * @param editingField - field, which name is being checked.
         * @return true when field name is unique.
         */
        public boolean isUniqueFieldName(String name, DetailFieldTO editingField) {
            int count = reportModel.getItemCount();
            for (int i=0; i<count; i++){
                DetailFieldTO field = (DetailFieldTO)reportModel.getItem(i);
                if (field.getName().equals(name) && field != editingField) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Check whether given field type can be added to the existing list of fields.
         * @param fieldType field type to be checked.
         * @return true when field can be added.
         */
        public boolean isAvailableFieldType(DetailFieldType fieldType) {
            if (fieldType.isUnlimitedNumberOfFields()){
                //Field can be added as many time as use wants.
                return true;
            }
            else{
                //Field can be used only once.
                int count = reportModel.getItemCount();
                for (int i=0; i<count; i++){
                    DetailFieldTO field = (DetailFieldTO)reportModel.getItem(i);
                    if (field.getType().equals(fieldType)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    private AvailabilityStrategy getEditAvailability() {
        return new PredefinedCommandAvailability(canEdit);
    }
}
