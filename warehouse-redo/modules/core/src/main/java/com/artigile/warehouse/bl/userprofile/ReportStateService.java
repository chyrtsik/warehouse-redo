/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.userprofile;

import com.artigile.warehouse.dao.ColumnStateDAO;
import com.artigile.warehouse.dao.ReportStateDAO;
import com.artigile.warehouse.domain.userprofile.ReportState;
import com.artigile.warehouse.utils.dto.userprofile.ReportStateTO;
import com.artigile.warehouse.utils.transofmers.ReportStateTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Borisok V.V., 15.02.2009
 */
@Transactional
public class ReportStateService {

    private ReportStateDAO reportStateDAO;

    private ColumnStateDAO columnStateDAO;

    public void save(ReportStateTO reportStateTO) {
        ReportState reportState = ReportStateTransformer.transform(reportStateTO);
        ReportStateTransformer.update(reportState, reportStateTO);
        reportStateDAO.save(reportState);
        ReportStateTransformer.update(reportStateTO, reportState);
    }

    public void remove(ReportStateTO reportStateTO) {
        ReportState reportState = ReportStateTransformer.transform(reportStateTO);
//        List<ColumnStateTO> columnStates = reportStateTO.getColumnStates();
//        if (!columnStates.isEmpty()) {
//            List<Long> ids = new ArrayList<Long>(columnStates.size());
//            for (ColumnStateTO columnState : columnStates) {
//                ids.add(columnState.getId());
//            }
//            columnStateDAO.removeByIds(ids);
//        }
        reportStateDAO.remove(reportState);
    }

    public void flush() {
        reportStateDAO.flush();
    }

    public void clear() {
        reportStateDAO.clear();
    }

    public ReportStateTO get(long id) {
        return ReportStateTransformer.transform(reportStateDAO.get(id));
    }

    public List<ReportStateTO> getAll() {
        return ReportStateTransformer.transformList(reportStateDAO.getAll());
    }

    public List<ReportStateTO> getByFilter(ReportStateFilter filter) {
        return ReportStateTransformer.transformList(reportStateDAO.getByFilter(filter));
    }

    public ReportState getById(long reportStateId) {
        return reportStateDAO.get(reportStateId);
    }
    
    //=========================== Spring setters ======================================
    public void setReportStateDAO(ReportStateDAO reportStateDAO) {
        this.reportStateDAO = reportStateDAO;
    }

    public void setColumnStateDAO(ColumnStateDAO columnStateDAO) {
        this.columnStateDAO = columnStateDAO;
    }
}
