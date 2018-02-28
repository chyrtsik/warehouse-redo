package com.artigile.warehouse.gui.menuitems.details.batches.command;

import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.CreateCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.details.batches.DetailBatchForm;
import com.artigile.warehouse.gui.menuitems.details.batches.DetailBatchesEditingStrategy;
import com.artigile.warehouse.gui.menuitems.details.batches.PriceUpdater;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.postings.UnclassifiedCatalogItemTO;

/**
 * Command for adding new detail batch.
 *
 * @author Aliaksandr.Chyrtsik, 25.11.12
 */
public class CreateDetailBatchCommand extends CreateCommand {
    public CreateDetailBatchCommand() {
        super(getAvailabilityStrategy());
    }

    @Override
    protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
        return createDetailBatch(null);
    }

    /**
     * Launch UI for creating detail batch.
     * @param unclassifiedCatalogItem null if just new detail batch is created and specified if detail batch should be
     * initialized from specified unclassified catalog item.
     * @return detail batch created or null if user cancelled creation.
     */
    public static DetailBatchTO createDetailBatch(UnclassifiedCatalogItemTO unclassifiedCatalogItem) {
        DetailBatchTO detailBatch = new DetailBatchTO();
        if (unclassifiedCatalogItem != null) {
            detailBatch.setBarCode(unclassifiedCatalogItem.getBarCode());
        }

        PropertiesForm prop = new DetailBatchForm(detailBatch, true, DetailBatchForm.PropertiesType.CreateNew);
        if (Dialogs.runProperties(prop)) {
            //Saving new detail batch.
            SpringServiceContext.getInstance().getDetailBatchesService().saveDetailBatch(detailBatch);
            return PriceUpdater.updatePrices(detailBatch);
        }
        return null;
    }

    public static AvailabilityStrategy getAvailabilityStrategy() {
        return DetailBatchesEditingStrategy.getEditAvailability();
    }
}
