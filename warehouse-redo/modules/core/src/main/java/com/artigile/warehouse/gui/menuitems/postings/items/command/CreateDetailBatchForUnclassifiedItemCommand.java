package com.artigile.warehouse.gui.menuitems.postings.items.command;

import com.artigile.warehouse.bl.postings.PostingService;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.MultipleAndCriteriaCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.details.batches.command.CreateDetailBatchCommand;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;

/**
 * Command for creation of detail batch for unclassified posting item.
 *
 * @author Aliaksandr.Chyrtsik, 24.11.12
 */
public class CreateDetailBatchForUnclassifiedItemCommand extends CustomCommand{
    public CreateDetailBatchForUnclassifiedItemCommand(AvailabilityStrategy availabilityStrategy) {
        super(
                new ResourceCommandNaming("posting.items.command.create.detail.for.unclassified.item"),
                new MultipleAndCriteriaCommandAvailability(availabilityStrategy, CreateDetailBatchCommand.getAvailabilityStrategy())
        );
    }

    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        PostingItemTO postingItemTO = (PostingItemTO)context.getCurrentReportItem();
        DetailBatchTO detailBatch = CreateDetailBatchCommand.createDetailBatch(postingItemTO.getUnclassifiedCatalogItem());
        if (detailBatch != null){
            PostingService postingService = SpringServiceContext.getInstance().getPostingsService();
            postingService.attachDetailBatchToUnclassifiedPostingItem(postingItemTO.getId(), detailBatch.getId());
            return true;
        }
        else{
            return false;
        }
    }
}
