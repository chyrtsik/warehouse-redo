/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.batchesext;

import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.CreateCopyCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.details.batches.DetailBatchForm;
import com.artigile.warehouse.gui.menuitems.details.batches.DetailBatchesEditingStrategy;
import com.artigile.warehouse.gui.menuitems.details.batches.command.CreateDetailBatchCommand;
import com.artigile.warehouse.gui.menuitems.details.history.DetailBatchHistoryCommand;
import com.artigile.warehouse.gui.menuitems.details.reserves.DetailBatchReservesCommand;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldValueTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldsHeaderMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author IoaN, Feb 26, 2009
 */

public class DetailBatchesEditExtStrategy extends DetailBatchesEditingStrategy {

    private DetailFieldsHeaderMap headerMap;

    public DetailBatchesEditExtStrategy(DetailFieldsHeaderMap headerMap) {
        super();
        this.headerMap = headerMap;
    }

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateDetailBatchExtCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        boolean singleItemSelected = (context.getCurrentReportItems().size() == 1);
        if (singleItemSelected){
            commands.add(new CreateDetailBatchExtAsCopyCommand());
        }
        commands.add(new CreateDetailBatchExtCommand());
        commands.add(new DeleteDetailBatchCommand());
        if (singleItemSelected){
            commands.add(new OpenDetailBatchExtCommand());
            commands.add(new DetailBatchHistoryCommand());
            commands.add(new DetailBatchReservesCommand());
        }
    }

    protected class CreateDetailBatchExtAsCopyCommand extends CreateCopyCommand {
        protected CreateDetailBatchExtAsCopyCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            DetailBatchTO detailBatchSrcFromReport = (DetailBatchTO)context.getCurrentReportItem();

            //We should get fresh exemplar of detail batch (wihout modifications, have been done when
            //for displaying of detail batch in list).
            DetailBatchTO detailBatch = SpringServiceContext.getInstance().getDetailBatchesService().getBatch(detailBatchSrcFromReport.getId());
            detailBatch.setId(0);
            detailBatch.setSellPrice(null);

            PropertiesForm prop = new DetailBatchForm(detailBatch, true, DetailBatchForm.PropertiesType.CreateAsCopy);
            if (Dialogs.runProperties(prop)) {
                //Saving new detail batch.
                getDetailBatchesService().saveDetailBatch(detailBatch);
                return prepareDetailForReport(detailBatch);
            }
            return null;
        }
    }

    protected class CreateDetailBatchExtCommand extends CreateDetailBatchCommand {
        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            DetailBatchTO detailBatch = (DetailBatchTO)super.doCreate(context);
            if (detailBatch != null) {
                //Processing adding of new detail batch (we should prepair new item for the catalog).
                return prepareDetailForReport(detailBatch);
            }
            return null;
        }
    }

    private class OpenDetailBatchExtCommand extends OpenDetailBatchCommand{
        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            //We should convert detail batch item from it's catalog form to ordinary form (otherwise is cannot be edited).
            DetailBatchTO detailBatchFromCatalog = (DetailBatchTO)context.getCurrentReportItem();
            DetailBatchTO detailBatch = SpringServiceContext.getInstance().getDetailBatchesService().getBatch(detailBatchFromCatalog.getId());
            if (super.doProperties(detailBatch, context)){
                prepareDetailForReport(detailBatch);
                detailBatchFromCatalog.copyFrom(detailBatch);
                return true;
            }
            return false;
        }
    }

    /**
     * In every detail creates empty labels in corresponding places. <br>
     * for example if we have two details: transistor and condensator and they both have <b>name</b> field.<br>
     * condensator has <b>conF1</b> and <b>conF2</b> fields<br>
     * transistor has <b>transF1</b> field and  <b>tranF2</b> fields.
     * so the table should look like:<br>
     * <table border=1>
     * <tr><td>Name</td><td>conF1</td><td>conF2</td><td>tranF1</td><td>tranF2</td></tr>
     * <tr><td>Condensator</td><td>f1</td><td>f2</td><td><p/></td><td><p/></td></tr>
     * <tr><td>Transistor</td><td></td><td></td><td>F1</td><td>F2</td></tr>
     * </table
     * <p/>
     * So every detail has to contain all it's own fields  and empty fields that come from other details.
     *
     * @param detailBatchTO (in|out) -- detail back to be modified.
     * @return modified detail batch.
     */
    public DetailBatchTO prepareDetailForReport(DetailBatchTO detailBatchTO) {
        List<DetailFieldValueTO> detailsModelFieldList = new ArrayList<DetailFieldValueTO>();
        for (int i = 0; i < headerMap.getMappingSize(); i++) {
            detailsModelFieldList.add(getEmptyField());
        }
        int i = 0;
        for (DetailFieldValueTO detailFieldValueTO : detailBatchTO.getModel().getFields()) {
            String fieldName = detailFieldValueTO.getType().getName();
            DetailFieldsHeaderMap.FieldMapping fieldMapping = headerMap.getMappingByFieldName(fieldName);
            if (fieldMapping != null) {
                detailsModelFieldList.set(fieldMapping.getIndex(), detailFieldValueTO);
            }
        }
        detailBatchTO.getModel().setFields(detailsModelFieldList);
        return detailBatchTO;
    }

    /**
     * Creates an empty cell for displaying in table.
     */
    private static DetailFieldValueTO emptyField;
    static{
        DetailFieldTO detailFieldTO = new DetailFieldTO();
        detailFieldTO.setName("empty");
        emptyField = new DetailFieldValueTO(detailFieldTO, new DetailBatchTO());
    }
    private DetailFieldValueTO getEmptyField() {
        return emptyField;
    }
}
