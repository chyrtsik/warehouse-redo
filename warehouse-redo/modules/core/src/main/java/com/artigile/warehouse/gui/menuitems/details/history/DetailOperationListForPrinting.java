/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.history;

import com.artigile.warehouse.bl.detail.DetailBatchHistoryFilter;
import com.artigile.warehouse.bl.detail.DetailBatchHistoryService;
import com.artigile.warehouse.gui.core.print.PrintTemplateRequiringInitialization;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailBatchOperationTO;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Wrapper for printable object to be passed to print templates when stock changes report is being printed.
 * @author Aliaksandr Chyrtsik
 * @since 13.05.13
 */
public class DetailOperationListForPrinting implements PrintTemplateRequiringInitialization {
    //Filter fields to load operations data.
    private List<Long> warehouseIds;
    private Date periodStart;
    private Date periodEnd;

    //Detail batches whose report will be printed.
    private List<DetailBatchTO> detailBatchesToPrint;

    //Detail operations to be printed (loaded according to filters).
    private List<DetailBatchOperationTO> detailBatchesHistoryForPrinting;

    public DetailOperationListForPrinting(List<DetailBatchTO> detailBatchesToPrint) {
        this.detailBatchesToPrint = detailBatchesToPrint;
    }

    @Override
    public boolean initialize() {
        DetailOperationHistoryFilterForm prop = new DetailOperationHistoryFilterForm();
        while(true){
            //Ask user to provide period and warehouses to report.
            if (!Dialogs.runProperties(prop)){
                //User cancelled printing by refusing to select filter options.
                return false;
            }
            warehouseIds = prop.getWarehouseIds();
            periodStart = prop.getPeriodStart();
            periodEnd = prop.getPeriodEnd();

            //Prepare this object to provide info about changes of detail in stock count.
            DetailBatchHistoryService historyService = SpringServiceContext.getInstance().getDetailBatchHistoryService();

            DetailBatchHistoryFilter filter = new DetailBatchHistoryFilter();
            filter.setAddSummaryForItemsWithoutOperations(prop.isPrintEmptyItems());

            detailBatchesHistoryForPrinting = new ArrayList<DetailBatchOperationTO>(detailBatchesToPrint.size());
            for (Long warehouseId : warehouseIds){
                filter.setWarehouseId(warehouseId);
                for (DetailBatchTO detailBatch : detailBatchesToPrint){
                    filter.setDetailBatchId(detailBatch.getId());
                    detailBatchesHistoryForPrinting.addAll(historyService.getDetailBatchHistoryForReport(periodStart, periodEnd, filter));
                }
            }

            if (detailBatchesHistoryForPrinting.isEmpty()){
                //Nothing to print. Show filter till user will select values giving any results.
                MessageDialogs.showInfo(I18nSupport.message("detail.batch.history.message.nothing.to.print"));
            }
            else{
                //Initialization completed. Go to printing.
                break;
            }
        }
        return true;
    }

    public List<DetailBatchOperationTO> getItems(){
        return detailBatchesHistoryForPrinting;
    }

    public Date getPeriodStart(){
        return periodStart;
    }

    public Date getPeriodEnd(){
        return periodEnd;
    }
}
