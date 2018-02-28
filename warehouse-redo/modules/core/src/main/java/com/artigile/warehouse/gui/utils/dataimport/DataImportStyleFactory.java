/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.utils.dataimport;

import com.artigile.warehouse.domain.dataimport.ImportStatus;
import com.artigile.warehouse.gui.core.report.style.Style;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;
import com.artigile.warehouse.utils.dto.dataimport.DataImportTO;

import java.awt.*;

/**
 * Style factory for data import rows.
 *
 * @author Valery Barysok, 7/9/11
 */

public class DataImportStyleFactory implements StyleFactory {

    private Style notCompletedImportStyle = new Style(new Color(255, 200, 200));

    @Override
    public Style getStyle(Object rowData) {
        DataImportTO dataImport = (DataImportTO) rowData;
        if (dataImport.getImportStatus() == ImportStatus.NOT_COMPLETED) {
            return  notCompletedImportStyle;
        }

        return null;
    }
}
