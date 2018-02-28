package com.artigile.warehouse.gui.menuitems.details.stickers;

import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Valery Barysok, 2013-10-20
 */
public class StickerPrintWizardUtils {

    public static String getDisplayName(DetailBatchTO detailBatchTO, boolean button) {
        String template = button ? "stickers.print.wizard.select.detail.field.button" : "stickers.print.wizard.select.detail.field";
        if (detailBatchTO != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(detailBatchTO.getName());
            if (StringUtils.hasValue(detailBatchTO.getMisc())) {
                sb.append("<br>").append(detailBatchTO.getMisc());
            }
            if (StringUtils.hasValue(detailBatchTO.getNotice())) {
                sb.append("<br>").append(detailBatchTO.getNotice());
            }
            return I18nSupport.message(template, sb.toString());
        }
        else {
            return I18nSupport.message(template, I18nSupport.message("stickers.print.wizard.select.detail.field.value.not.selected"));
        }
    }

}
