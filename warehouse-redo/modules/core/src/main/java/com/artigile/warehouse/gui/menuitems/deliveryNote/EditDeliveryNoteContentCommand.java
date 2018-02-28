/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.deliveryNote;

import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.plugin.Plugin;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.deliveryNote.items.DeliveryNoteItemsEditor;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteTOForReport;

/**
 * @author Shyrik, 11.03.2010
 */

/**
 * Opens delivery note content editor.
 */
public class EditDeliveryNoteContentCommand extends CustomCommand {
    public EditDeliveryNoteContentCommand() {
        super(new ResourceCommandNaming("deliveryNote.command.editItems"), DeliveryNoteEditingStrategy.getEditAvailability());
    }

    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        //Opening items of the selected delivery note.
        DeliveryNoteTOForReport deliveryNoteTO = (DeliveryNoteTOForReport)context.getCurrentReportItem();
        Plugin deliveryNoteContentEditor = new DeliveryNoteItemsEditor(deliveryNoteTO.getId());
        WareHouse.runPlugin(deliveryNoteContentEditor);
        return true;
    }
}
