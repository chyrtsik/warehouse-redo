/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.types.fields;

import com.artigile.warehouse.domain.details.DetailFieldType;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 14.12.2008
 */

/**
 * Class is used to edit list of fields of the detail type.
 */
public class DetailFieldsList extends ReportDataSourceBase {
    private List<DetailFieldTO> fields = new ArrayList<DetailFieldTO>();

    private final boolean canEdit;
    private final boolean useSortAndGroupNumbers;
    private final int maxFieldsCount;
    private final DetailFieldType[] availableFields;

    public DetailFieldsList(String reportName, List<DetailFieldTO> fields, boolean canEdit, boolean useSortAndGroupNumbers,
                            int maxFieldsCount, DetailFieldType[] availableFields) {
        super(reportName);
        this.fields = fields;
        this.canEdit = canEdit;
        this.useSortAndGroupNumbers = useSortAndGroupNumbers;
        this.maxFieldsCount = maxFieldsCount;
        this.availableFields = availableFields;
    }

    public List<DetailFieldTO> getEditedFields() {
        List<DetailFieldTO> fields = new ArrayList<DetailFieldTO>();
        for (int i=0; i<getReportModel().getItemCount(); i++){
            DetailFieldTO field = (DetailFieldTO)getReportModel().getItem(i);
            field.setDisplayOrder((long)i+1);
            fields.add(field);
        }
        return fields;
    }

    @Override
    public String getReportTitle() {
        return ""; //Not used
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(DetailFieldTO.class);

        reportInfo.getOptions().setDragRowsEnabled(true);
        reportInfo.getOptions().setShowToolbar(false);
        reportInfo.getOptions().setSortEnabled(false);

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.fields.list.field"), "name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.fields.list.type"), "type.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.fields.list.mandatory"), "mandatory"));
        if (useSortAndGroupNumbers){
            reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.fields.list.sort.num"), "sortNum"));
            reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.fields.list.group.num"), "catalogGroupNum"));
        }

        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new DetailFieldsEditingStrategy(canEdit, useSortAndGroupNumbers, maxFieldsCount, availableFields);
    }

    @Override
    public List getReportData() {
        return fields;
    }
}
