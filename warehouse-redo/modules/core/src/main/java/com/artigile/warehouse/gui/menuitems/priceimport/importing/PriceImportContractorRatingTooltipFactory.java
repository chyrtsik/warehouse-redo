/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.priceimport.importing;

import com.artigile.warehouse.gui.core.report.tooltip.ColumnTooltipFactory;
import com.artigile.warehouse.gui.menuitems.priceimport.importing.styles.PriceImportItemCountStyleFactory;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class PriceImportContractorRatingTooltipFactory implements ColumnTooltipFactory {

    /**
     * Pre-init tooltips
     */
    private static final String TOOLTIP_TEXT_PRE = I18nSupport.message("price.import.list.imported.item.count.tooltip");
    private static final String TOOLTIP_TEXT_CONTRACTOR_RATING_1 = TOOLTIP_TEXT_PRE + " 1";
    private static final String TOOLTIP_TEXT_CONTRACTOR_RATING_2 = TOOLTIP_TEXT_PRE + " 2";
    private static final String TOOLTIP_TEXT_CONTRACTOR_RATING_3 = TOOLTIP_TEXT_PRE + " 3";
    private static final String TOOLTIP_TEXT_CONTRACTOR_RATING_4 = TOOLTIP_TEXT_PRE + " 4";
    private static final String TOOLTIP_TEXT_CONTRACTOR_RATING_5 = TOOLTIP_TEXT_PRE + " 5";

    /**
     * Required to determine rating of contractor
     */
    private int contractorRatingOneRGB = PriceImportItemCountStyleFactory.getContractorRatingOneRGB();
    private int contractorRatingTwoRGB = PriceImportItemCountStyleFactory.getContractorRatingTwoRGB();
    private int contractorRatingThreeRGB = PriceImportItemCountStyleFactory.getContractorRatingThreeRGB();
    private int contractorRatingFourRGB = PriceImportItemCountStyleFactory.getContractorRatingFourRGB();
    private int contractorRatingFiveRGB = PriceImportItemCountStyleFactory.getContractorRatingFiveRGB();




    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public String getTooltipText(DefaultTableCellRenderer cellRenderer) {
        // Using background color of cells determines rating of contractor
        int cellBackgroundRGB = cellRenderer.getBackground().getRGB();
        if (cellBackgroundRGB == contractorRatingOneRGB) {
            return TOOLTIP_TEXT_CONTRACTOR_RATING_1;
        } else if (cellBackgroundRGB == contractorRatingTwoRGB) {
            return TOOLTIP_TEXT_CONTRACTOR_RATING_2;
        } else if (cellBackgroundRGB == contractorRatingThreeRGB) {
            return TOOLTIP_TEXT_CONTRACTOR_RATING_3;
        } else if (cellBackgroundRGB == contractorRatingFourRGB) {
            return TOOLTIP_TEXT_CONTRACTOR_RATING_4;
        } else if (cellBackgroundRGB == contractorRatingFiveRGB) {
            return TOOLTIP_TEXT_CONTRACTOR_RATING_5;
        } else {
            return null;
        }
    }
}
