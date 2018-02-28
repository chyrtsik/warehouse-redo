/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.postings.items.command;

import com.artigile.warehouse.adapter.spi.DataImportContext;
import com.artigile.warehouse.bl.dataimport.DataImportFinishListener;
import com.artigile.warehouse.bl.postings.PostingItemsDataSaver;
import com.artigile.warehouse.bl.postings.PostingService;
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.ReadOnlyPropertiesDialog;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.postings.items.ImportPostingItemsForm;
import com.artigile.warehouse.gui.menuitems.postings.items.ImportPostingItemsResultForm;
import com.artigile.warehouse.gui.menuitems.postings.items.PostingItemEditingAvailability;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * Command for importing posting positions from the external source.
 *
 * @author Aliaksandr.Chyrtsik, 07.11.11
 */
public class ImportPostingItemsCommand extends CustomCommand {
    private final long postingId;

    public ImportPostingItemsCommand(final long postingId) {
        super(new ResourceCommandNaming("posting.items.command.import.items"), new AvailabilityStrategy() {
            private PostingItemEditingAvailability availability = new PostingItemEditingAvailability();

            @Override
            public boolean isAvailable(ReportCommandContext context) {
                return availability.isAvailable(postingId);
            }
        });
        this.postingId = postingId;
    }

    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        ImportPostingItemsForm prop = new ImportPostingItemsForm(postingId);
        PropertiesDialog propertiesDialog = new PropertiesDialog(prop, "posting.items.import.properties.ok.button");
        propertiesDialog.setResizable(true);
        if (propertiesDialog.run()) {
            //Perform import of posting items.
            PostingService postingService = SpringServiceContext.getInstance().getPostingsService();
            postingService.performPostingItemsImport(prop.getImportConfiguration(), new DataImportFinishListener() {
                @Override
                public void onImportFinished(long dataImportId, DataImportContext importContext) {
                    //Process results of the import.
                    PostingItemsDataSaver dataSaver = (PostingItemsDataSaver)importContext.getDataSaver();
                    if (dataSaver.getErrorItems().isEmpty()){
                        //Import succeeded.
                        MessageDialogs.showInfo(I18nSupport.message("posting.items.import.result.success", dataSaver.getImportedItemsCount()));
                    }
                    else{
                        //There were some errors. Suggest to view items with errors.
                        boolean showErrorItems = MessageDialogs.showConfirm(
                                I18nSupport.message("posting.items.import.result.errors.title"),
                                I18nSupport.message("posting.items.import.result.errors.message", dataSaver.getImportedItemsCount(), dataSaver.getErrorItems().size()));

                        if (showErrorItems){
                            ReadOnlyPropertiesDialog dialog = new ReadOnlyPropertiesDialog(new ImportPostingItemsResultForm(dataSaver.getErrorItems()));
                            dialog.setResizable(true);
                            dialog.run();
                        }
                    }
                }
            });
            return true;
        }
        return false;
    }
}
