/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.print.images;

import com.artigile.warehouse.bl.print.PrintTemplateService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.printing.PrintTemplateImage;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;

/**
 * @author Shyrik, 25.01.2009
 */
public class ImagesListEditingStrategy implements ReportEditingStrategy {
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateImageCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateImageCommand());
        commands.add(new DeleteImageCommand());
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new OpenImageCommand());
        }
    }

    //==================== Commands ===========================================
    private class CreateImageCommand extends CreateCommand {
        protected CreateImageCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            PrintTemplateImage newImage = new PrintTemplateImage();
            PropertiesForm prop = new ImageForm(newImage, true);
            if (Dialogs.runProperties(prop)) {
                //Saving new image.
                getPrintTemplateService().saveImage(newImage);
                return newImage;
            }
            return null;
        }
    }

    private class DeleteImageCommand extends DeleteCommand {
        protected DeleteImageCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            //Deleting choosed image.
            PrintTemplateImage imageToDelete = (PrintTemplateImage) deletingItem;
            getPrintTemplateService().deleteImage(imageToDelete);
            return true;
        }
    }

    private class OpenImageCommand extends PropertiesCommandBase {
        protected OpenImageCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            //Edit properties of the image.
            PrintTemplateImage image = (PrintTemplateImage) editingItem;
            PropertiesForm prop = new ImageForm(image, getEditAvailability().isAvailable(context));
            if (Dialogs.runProperties(prop)) {
                //Saving edited detail batch.
                getPrintTemplateService().saveImage(image);
                return true;
            }
            return false;
        }
    }

    //====================================== Helpers ========================================
    private AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_PRINT_TEMPLATE_IMAGES_LIST);
    }

    private PrintTemplateService getPrintTemplateService() {
        return SpringServiceContext.getInstance().getPrintTemplateService();
    }
}
