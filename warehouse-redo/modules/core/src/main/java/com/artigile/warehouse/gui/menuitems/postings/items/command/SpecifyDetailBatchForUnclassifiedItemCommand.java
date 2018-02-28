package com.artigile.warehouse.gui.menuitems.postings.items.command;

import com.artigile.warehouse.bl.postings.PostingService;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.browser.BrowseResult;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.MultipleAndCriteriaCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.details.batches.DetailBatchesList;
import com.artigile.warehouse.gui.menuitems.details.batches.command.CreateDetailBatchCommand;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Aliaksandr.Chyrtsik, 25.11.12
 */
public class SpecifyDetailBatchForUnclassifiedItemCommand extends CustomCommand {
    public SpecifyDetailBatchForUnclassifiedItemCommand(AvailabilityStrategy availabilityStrategy){
        super(
                new ResourceCommandNaming("posting.items.command.choose.detail.for.unclassified.item"),
                new MultipleAndCriteriaCommandAvailability(availabilityStrategy, CreateDetailBatchCommand.getAvailabilityStrategy())
        );
    }

    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        //Select detail batch for unclassified posting item.
        BrowseResult browseResult = Dialogs.runBrowser(new DetailBatchesList());
        if (browseResult.isOk()){
            DetailBatchTO detailBatch = (DetailBatchTO)browseResult.getSelectedItems().get(0);
            if (!StringUtils.isStringNullOrEmpty(detailBatch.getBarCode())){
                //Ask user before changing bar code.
                if (!MessageDialogs.showConfirm(I18nSupport.message("posting.items.command.detail.has.barcode.title"),
                        I18nSupport.message("posting.items.command.detail.has.barcode.message"))){
                    return false;
                }
            }
            //Assign appropriate detail batch to unclassified item.
            PostingItemTO postingItemTO = (PostingItemTO)context.getCurrentReportItem();
            PostingService postingService = SpringServiceContext.getInstance().getPostingsService();
            postingService.attachDetailBatchToUnclassifiedPostingItem(postingItemTO.getId(), detailBatch.getId());
            return true;
        }
        else{
            return false;
        }
    }
}
