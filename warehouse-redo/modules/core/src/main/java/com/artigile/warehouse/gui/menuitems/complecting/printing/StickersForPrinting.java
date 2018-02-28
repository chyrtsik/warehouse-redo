/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.complecting.printing;

import com.artigile.warehouse.gui.menuitems.complecting.complectingTasks.ComplectingTaskList;
import com.artigile.warehouse.utils.custom.types.CompositeNumber;
import com.artigile.warehouse.utils.dto.complecting.ComplectingTaskTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 25.05.2009
 */

/**
 * Holder for printable stickers data.
 */
public class StickersForPrinting implements ComplectingTasksForPrinting {
    private WarehouseTOForReport warehouse;

    /**
     * Original list of complecting tasks, used to form list of stickers.
     */
    private List<ComplectingTaskTO> allStickers = new ArrayList<ComplectingTaskTO>();

    /**
     * Filtered list of complecting tasks to be printed.
     */
    private List<ComplectingTaskTO> stickersForPrinting = new ArrayList<ComplectingTaskTO>();


    public StickersForPrinting(ComplectingTaskList tasksList, ComplectingTasksToPrintFilterType whatToPrint) {
        this.warehouse = tasksList.getWarehouse();
        this.allStickers = tasksList.getFilteredTasksList(whatToPrint, true);

        //For same order item wares on different storage places only one sticker is needed.
        //So, we need to do such sub summing here.
        List<ComplectingTaskTO> tempAllStickers = new ArrayList<ComplectingTaskTO>(allStickers);
        for (int i=0; i< tempAllStickers.size(); i++){
            ComplectingTaskTO task = tempAllStickers.get(i);
            long summCount = task.getNeededCount();

            if (task.getOrderSubItem() != null){
                for (int j= tempAllStickers.size()-1; j > i; j--){
                    ComplectingTaskTO otherTask = tempAllStickers.get(j);
                    if (otherTask.getOrderSubItem() != null){
                        //Only complecting tasks for orders can be checked in this way for now. Movements are performed
                        //directly from storage places.
                        if (otherTask.getOrderSubItem().getOrderItem().getId() == task.getOrderSubItem().getOrderItem().getId() &&
                            otherTask.getOrderSubItem().getOrderItem().getNumber() == task.getOrderSubItem().getOrderItem().getNumber()){
                            summCount += otherTask.getNeededCount();
                            tempAllStickers.remove(j);
                        }
                    }
                }
            }

            if (summCount > task.getNeededCount()){
                //There are more than one task for same order item. So, we join them into one sticker.
                ComplectingTaskTO sticker = new ComplectingTaskTO(){
                    public CompositeNumber getItemNo(){
                        //Overriden for providing order item number for joined complecting tasks.
                        return new CompositeNumber("{0} [...]", new Long[]{getOrderSubItem().getOrderItem().getNumber()});
                    }
                };
                sticker.copyFrom(task);
                sticker.setNeededCount(summCount);
                stickersForPrinting.add(sticker);
            }
            else{
                //There is only one complecting task for order item. So it will be a one sticker.
                stickersForPrinting.add(task);
            }
        }
    }

    public List<ComplectingTaskTO> getAllItems(){
        return allStickers;
    }

    @Override
    public WarehouseTOForReport getWarehouse() {
        return warehouse;
    }

    @Override
    public List<ComplectingTaskTO> getItems() {
        return stickersForPrinting;
    }
}
