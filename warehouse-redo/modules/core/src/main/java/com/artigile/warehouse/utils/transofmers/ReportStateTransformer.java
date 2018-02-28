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

import com.artigile.warehouse.bl.userprofile.ReportStateService;
import com.artigile.warehouse.domain.userprofile.ReportState;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.userprofile.ReportStateTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Borisok V.V., 16.02.2009
 */
public final class ReportStateTransformer {
    private ReportStateTransformer(){
    }

    public static ReportStateService getReportStateService(){
        return SpringServiceContext.getInstance().getReportStateService();
    }

    public static ReportStateTO transform(ReportState reportState) {
        if (reportState == null){
            return null;
        }
        ReportStateTO reportStateTO = new ReportStateTO();
        update(reportStateTO, reportState);
        return reportStateTO;
    }

    public static void update(ReportStateTO reportStateTO, ReportState reportState){
        reportStateTO.setId(reportState.getId());
        reportStateTO.setReportMajor(reportState.getReportMajor());
        reportStateTO.setReportMinor(reportState.getReportMinor());
        reportStateTO.setUser(UserTransformer.transformUser(reportState.getUser()));
        reportStateTO.setFilterVisible(reportState.getFilterVisible());
        reportStateTO.setColumnStates(ColumnStateTransformer.transformList(reportState.getColumnStates(), reportStateTO));
    }

    public static List<ReportStateTO> transformList(List<ReportState> reportStateList) {
        List<ReportStateTO> list = new ArrayList<ReportStateTO>();
        for (ReportState reportState : reportStateList) {
            list.add(transform(reportState));
        }
        return list;
    }

    public static ReportState transform(ReportStateTO reportStateTO) {
        if (reportStateTO == null){
            return null;
        }
        ReportState reportState = SpringServiceContext.getInstance().getReportStateService().getById(reportStateTO.getId());
        if (reportState == null){
            reportState = new ReportState();
        }
        return reportState;
    }

    public static void update(ReportState reportState, ReportStateTO reportStateTO){
        reportState.setId(reportStateTO.getId());
        reportState.setReportMajor(reportStateTO.getReportMajor());
        reportState.setReportMinor(reportStateTO.getReportMinor());
        reportState.setUser(UserTransformer.transformUser(reportStateTO.getUser()));
        reportState.setFilterVisible(reportStateTO.getFilterVisible());
        reportState.setColumnStates(ColumnStateTransformer.transformAndUpdateList(reportStateTO.getColumnStates(), reportState));
    }
}
