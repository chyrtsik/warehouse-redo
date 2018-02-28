package com.artigile.warehouse.gui.menuitems.details.stickers;

import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;

import java.util.List;
import java.util.Map;

/**
 * @author Valery Barysok, 2013-07-31
 */
public class DetailFieldWizardStep extends AbstractDetailFieldWizardStep {

    public DetailFieldWizardStep(DetailFieldTO detailFieldTO) {
        super(detailFieldTO);
    }

    @Override
    protected List<String> getAvailableDetailFieldValues(long detailTypeId, DetailFieldTO detailField, Map<DetailFieldTO, String> fieldValues, Map<DetailFieldTO, String> serialFieldValues) {
        SpringServiceContext serviceContext = SpringServiceContext.getInstance();
        return serviceContext.getDetailModelsService().getAvailableDetailFieldValues(serviceContext.getDetailTypesService().getDetailTypeById(detailTypeId), detailField, fieldValues);
    }

    @Override
    protected Map<DetailFieldTO, String> getFieldValues() {
        return ((StickerPrintWizardContext) getWizard().getWizardContext()).getFieldValues();
    }

    @Override
    public void clearFieldValue() {
        ((StickerPrintWizardContext) getWizard().getWizardContext()).getFieldValues().remove(getDetailFieldTO());
    }
}
