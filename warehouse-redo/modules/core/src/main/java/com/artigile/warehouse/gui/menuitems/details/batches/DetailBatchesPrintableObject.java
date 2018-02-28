/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.batches;

/**
 * @author Aliaksandr Chyrtsik
 * @since 13.05.13
 */

import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.gui.core.print.PrintTemplateTypeDependent;
import com.artigile.warehouse.gui.menuitems.details.history.DetailOperationListForPrinting;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;

import java.util.List;

/**
 * Printable object for detail batches list that decides which data to pass to printing engine
 * depending on used template type.
 */
public class DetailBatchesPrintableObject implements PrintTemplateTypeDependent {
    private List<DetailBatchTO> detailBatchesToPrint;

    public DetailBatchesPrintableObject(List<DetailBatchTO> detailBatchesToPrint) {
        this.detailBatchesToPrint = detailBatchesToPrint;
    }

    @Override
    public Object prepareForTemplateType(PrintTemplateType templateType) {
        if (templateType.equals(PrintTemplateType.TEMPLATE_STOCK_CHANGES_REPORT)){
            return new DetailOperationListForPrinting(detailBatchesToPrint);
        }
        else{
            return new DetailBatchesListForPrinting(detailBatchesToPrint);
        }
    }
}
