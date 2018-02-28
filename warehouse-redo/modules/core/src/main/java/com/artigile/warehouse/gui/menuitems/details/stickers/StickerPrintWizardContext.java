package com.artigile.warehouse.gui.menuitems.details.stickers;

import com.artigile.warehouse.gui.core.wizard.WizardContext;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailTypeTO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Valery Barysok, 2013-07-31
 */
public class StickerPrintWizardContext implements WizardContext {

    private DetailTypeTO detailTypeTO;

    private Map<DetailFieldTO, String> fieldValues = new HashMap<DetailFieldTO, String>();

    private Map<DetailFieldTO, String> serialFieldValues = new HashMap<DetailFieldTO, String>();

    private DetailBatchTO detailBatchTO;

    private int stickerCount = 1;

    public DetailTypeTO getDetailTypeTO() {
        return detailTypeTO;
    }

    public void setDetailTypeTO(DetailTypeTO detailTypeTO) {
        this.detailTypeTO = detailTypeTO;
    }

    public Map<DetailFieldTO, String> getFieldValues() {
        return fieldValues;
    }

    public Map<DetailFieldTO, String> getSerialFieldValues() {
        return serialFieldValues;
    }

    public DetailBatchTO getDetailBatchTO() {
        return detailBatchTO;
    }

    public void setDetailBatchTO(DetailBatchTO detailBatchTO) {
        this.detailBatchTO = detailBatchTO;
    }

    @Override
    public boolean isValid() {
        return detailTypeTO != null;
    }
}
