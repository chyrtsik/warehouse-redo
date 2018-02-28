/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.priceimport.importing.styles;

import com.artigile.warehouse.gui.core.report.style.Alignment;
import com.artigile.warehouse.gui.core.report.style.Style;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;
import com.artigile.warehouse.utils.dto.priceimport.ContractorPriceImportTO;

import java.awt.*;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class PriceImportItemCountStyleFactory implements StyleFactory {

    /**
     * Predefined styles
     */
    private static final Style CONTRACTOR_RATING_1 = new Style(new Color(0, 240, 0), Alignment.RIGHT, Alignment.CENTER);
    private static final Style CONTRACTOR_RATING_2 = new Style(new Color(0, 220, 0), Alignment.RIGHT, Alignment.CENTER);
    private static final Style CONTRACTOR_RATING_3 = new Style(new Color(0, 200, 0), Alignment.RIGHT, Alignment.CENTER);
    private static final Style CONTRACTOR_RATING_4 = new Style(new Color(0, 180, 0), Alignment.RIGHT, Alignment.CENTER);
    private static final Style CONTRACTOR_RATING_5 = new Style(new Color(0, 160, 0), Alignment.RIGHT, Alignment.CENTER);


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public Style getStyle(Object rowData) {
        Integer contractorRating = ((ContractorPriceImportTO) rowData).getContractor().getRating();
        if (contractorRating != null) {
            switch (contractorRating) {
                case 1: return CONTRACTOR_RATING_1;
                case 2: return CONTRACTOR_RATING_2;
                case 3: return CONTRACTOR_RATING_3;
                case 4: return CONTRACTOR_RATING_4;
                case 5: return CONTRACTOR_RATING_5;
                default: return null;
            }
        }
        return null;
    }
    
    
    /* Getters
    ------------------------------------------------------------------------------------------------------------------*/
    public static int getContractorRatingOneRGB() {
        return CONTRACTOR_RATING_1.getBackground().getRGB();
    }
    
    public static int getContractorRatingTwoRGB() {
        return CONTRACTOR_RATING_2.getBackground().getRGB();
    }

    public static int getContractorRatingThreeRGB() {
        return CONTRACTOR_RATING_3.getBackground().getRGB();
    }

    public static int getContractorRatingFourRGB() {
        return CONTRACTOR_RATING_4.getBackground().getRGB();
    }

    public static int getContractorRatingFiveRGB() {
        return CONTRACTOR_RATING_5.getBackground().getRGB();
    }
}
