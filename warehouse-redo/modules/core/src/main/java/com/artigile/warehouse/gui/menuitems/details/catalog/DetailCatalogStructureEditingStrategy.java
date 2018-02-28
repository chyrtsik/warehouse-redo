/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.catalog;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.dto.details.DetailCatalogStructureTO;
import com.artigile.warehouse.utils.dto.details.DetailGroupTO;

/**
 * @author Shyrik, 04.01.2009
 */
public class DetailCatalogStructureEditingStrategy implements ReportEditingStrategy {
    /**
     * Editing catalog structure.
     */
    private DetailCatalogStructureTO catalogStructure;

    /**
     * This flag shows if the commands can be used on the tree. if enabled=false no commands allowed to user(e.g. Detail Catalog)
     * otherwise, if enabled=true all commands are allowed to user.
     */
    private boolean enabled;

    public DetailCatalogStructureEditingStrategy(DetailCatalogStructureTO catalogStructure, boolean enabled) {
        this.enabled = enabled;
        this.catalogStructure = catalogStructure;
    }

    public DetailCatalogStructureEditingStrategy(DetailCatalogStructureTO catalogStructure) {
        this(catalogStructure, true);
    }

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        if (enabled) {
            commands.add(new CreateRootGroupCommand());
        }
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        if (enabled) {
            if (context.getCurrentReportItems().size() == 1){
                DetailGroupTO currentGroup = (DetailGroupTO)context.getCurrentReportItem();
                if (!currentGroup.getEnableAutoSubGroups()){
                    //Subgroups are allowed only for groups without automatic grouping (there is only one
                    //alternative -- child groups or automatic grouping).
                    commands.add(new CreateGroupCommand());
                }
            }
            commands.add(new DeleteGroupCommand());
            if (context.getCurrentReportItems().size() == 1){
                commands.add(new OpenGroupCommand());
            }
        }
    }

    //=================================== Commands =============================================
    private class CreateRootGroupCommand extends CreateCommand {
        protected CreateRootGroupCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            //Creates new root detail group.
            DetailGroupTO rootGroup = new DetailGroupTO();
            PropertiesForm prop = new DetailCatalogGroupForm(rootGroup, true, new CatalogGroupChecker());
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                return rootGroup;
            }
            return null;
        }
    }

    private class CreateGroupCommand extends CreateCommand {
        protected CreateGroupCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            //Creates new child (non root) detail group.
            DetailGroupTO parentGroup = (DetailGroupTO) context.getCurrentReportItem();

            DetailGroupTO childGroup = new DetailGroupTO();
            childGroup.setParentGroup(parentGroup);

            PropertiesForm prop = new DetailCatalogGroupForm(childGroup, true, new CatalogGroupChecker());
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                return childGroup;
            }
            return null;
        }
    }

    private class DeleteGroupCommand extends DeleteCommand {
        protected DeleteGroupCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object item) throws ReportCommandException {
            return true;
        }
    }

    private class OpenGroupCommand extends PropertiesCommandBase {
        protected OpenGroupCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            DetailGroupTO group = (DetailGroupTO) editingItem;
            PropertiesForm prop = new DetailCatalogGroupForm(group, getEditAvailability().isAvailable(context), new CatalogGroupChecker());
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            return propDialog.run();
        }
    }

    //=============================== Helpers ============================================
    private AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_DETAIL_GROUPS);
    }

    public class CatalogGroupChecker {
        /**
         * Checks, if the given name of the detail group is unique for the it's level of tree.
         *
         * @param name         - new name of the group.
         * @param editingGroup - group, which name is being checked.
         * @return
         */
        public boolean isUniqueName(String name, DetailGroupTO editingGroup) {
            DetailGroupTO sameGroup = catalogStructure.findGroupByNameAtSameLevel(name, editingGroup);
            return sameGroup == null || sameGroup.equals(editingGroup);
        }
    }
}
