/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.userprofile.ColumnState;
import com.artigile.warehouse.domain.userprofile.ReportState;
import com.artigile.warehouse.utils.dto.userprofile.ColumnStateTO;
import com.artigile.warehouse.utils.dto.userprofile.ReportStateTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Borisok V.V., 16.02.2009
 */
public final class ColumnStateTransformer {
    private ColumnStateTransformer(){
    }

    public static ColumnStateTO transform(ColumnState columnState, ReportStateTO reportState) {
        ColumnStateTO columnStateTO = new ColumnStateTO();
        columnStateTO.setId(columnState.getId());
        columnStateTO.setIdentifier(columnState.getIdentifier());
        columnStateTO.setVisible(columnState.getVisible());
        columnStateTO.setReportState(reportState);
        columnStateTO.setFilterText(columnState.getFilterText()==null?"":columnState.getFilterText());
        columnStateTO.setWidth(columnState.getWidth());
        return columnStateTO;
    }

    public static List<ColumnStateTO> transformList(List<ColumnState> columnStateList, ReportStateTO reportState) {
        List<ColumnStateTO> list = new ArrayList<ColumnStateTO>();
        for (ColumnState columnState : columnStateList) {
            list.add(transform(columnState, reportState));
        }
        return list;
    }

    public static ColumnState transformAndUpdate(ColumnStateTO columnStateTO, ReportState reportState) {
        ColumnState columnState = new ColumnState();
        columnState.setId(columnStateTO.getId());
        columnState.setIdentifier(columnStateTO.getIdentifier());
        columnState.setVisible(columnStateTO.isVisible());
        columnState.setReportState(reportState);
        columnState.setFilterText(columnStateTO.getFilterText());
        columnState.setWidth(columnStateTO.getWidth());
        return columnState;
    }

    public static List<ColumnState> transformAndUpdateList(List<ColumnStateTO> columnStateList, ReportState reportState) {
        List<ColumnState> list = new ArrayList<ColumnState>();
        for (ColumnStateTO columnState : columnStateList) {
            list.add(transformAndUpdate(columnState, reportState));
        }
        return list;
    }
}
