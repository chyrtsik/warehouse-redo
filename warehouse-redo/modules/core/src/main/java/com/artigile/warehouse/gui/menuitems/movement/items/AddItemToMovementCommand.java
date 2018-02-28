/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.movement.items;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.chooseonealternative.ChooseOneAlternativeForm;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.movement.MovementItemTO;
import com.artigile.warehouse.utils.dto.movement.MovementTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 22.11.2009
 */

/**
 * Command for adding new item to the movement.
 */
public class AddItemToMovementCommand extends CustomCommand {
    private MovementTOForReport movement;

    protected AddItemToMovementCommand(MovementTOForReport movement) {
        super(new ResourceCommandNaming("movement.items.editor.commamd.addItem"), MovementItemsEditingStrategy.getAddItemAvailability(movement));
        this.movement = movement;
    }

    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        //Try to add new item to the movement.
        WarehouseBatchTO warehouseBatchToAdd = (WarehouseBatchTO) context.getCurrentReportItem();
        MovementItemTO newMovementItem = new MovementItemTO();
        newMovementItem.init(movement, warehouseBatchToAdd);

        Boolean shouldAddNewItem = shouldAddNewItem(newMovementItem);
        if (shouldAddNewItem == null) {
            return false;
        }

        if (shouldAddNewItem) {
            //Creating new item.
            PropertiesForm prop = new MovementItemForm(newMovementItem, true, false);
            if (Dialogs.runProperties(prop)) {
                try {
                    SpringServiceContext.getInstance().getMovementService().addItemToMovement(newMovementItem);
                } catch (BusinessException e) {
                    throw new ReportCommandException(e);
                }
            }
        }
        else{
            //Updating existing item.
            MovementItemTO sameItem = movement.getSameItem(newMovementItem);
            PropertiesForm prop = new MovementItemForm(sameItem, true, true);
            if ( Dialogs.runProperties(prop) ){
                try {
                    SpringServiceContext.getInstance().getMovementService().saveMovementItem(sameItem);
                } catch (BusinessException e) {
                    throw new ReportCommandException(e);
                }
            }
            return false; //Little hack to prevent report framework from adding new item.
        }

        return false;
    }

    private Boolean shouldAddNewItem(MovementItemTO newMovementItem) {
        //First we decide what to if, is the new item will be duplicated with the same one.
        final int addNewItem = 1;
        final int changeExistentItem = 2;
        int whatToDo = addNewItem;

        if (newMovementItem.getMovement().hasSameItem(newMovementItem)) {
            //Let user choose, what does he want to do with new item (because it will be duplicated).
            ChooseOneAlternativeForm chooseForm = new ChooseOneAlternativeForm();
            chooseForm.setTitle(I18nSupport.message("movement.items.editor.itemAlreadyExists.title"));
            chooseForm.setMessage(I18nSupport.message("movement.items.editor.itemAlreadyExists.message"));
            chooseForm.addChoice(new ListItem(I18nSupport.message("movement.items.editor.itemAlreadyExists.addNewItem"), addNewItem));
            chooseForm.addChoice(new ListItem(I18nSupport.message("movement.items.editor.itemAlreadyExists.changeExistentItem"), changeExistentItem));
            if (!Dialogs.runProperties(chooseForm)) {
                return null;
            }
            whatToDo = (Integer) chooseForm.getChoice().getValue();
        }

        if (whatToDo == addNewItem) {
            //Adding of new movement item.
            return true;
        } else if (whatToDo == changeExistentItem) {
            //Changing existing movement item instead of adding new one.
            return false;
        } else {
            throw new RuntimeException("AddItemToMovementCommand.shouldAddNewItem: unexpected user choice of action.");
        }

    }


}
