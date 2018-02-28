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

import com.artigile.warehouse.utils.dto.details.DetailBatchTO;

import java.util.List;

/**
 * Printable instance of detail batches list.
 * All data is accessed by print engine using reflection.
 * See print template fields mapping for template type TEMPLATE_DETAIL_BATCHES_LIST in
 * {@link com.artigile.warehouse.domain.printing.PrintTemplateType}.
 */
public class DetailBatchesListForPrinting {
    private List<DetailBatchTO> detailBatchesToPrint;

    public DetailBatchesListForPrinting(List<DetailBatchTO> detailBatchesToPrint) {
        this.detailBatchesToPrint = detailBatchesToPrint;
    }

    public List<DetailBatchTO> getItems(){
        return detailBatchesToPrint;
    }
}