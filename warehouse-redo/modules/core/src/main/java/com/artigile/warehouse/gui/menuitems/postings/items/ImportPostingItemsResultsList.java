/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.postings.items;

import com.artigile.warehouse.bl.postings.PostingItemImportError;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.*;

/**
 * @author Aliaksandr.Chyrtsik, 07.11.11
 */
public class ImportPostingItemsResultsList extends ReportDataSourceBase {
    private List<ColumnInfo> headerColumns;
    private List<PostingItemImportErrorForReport> reportItems;

    public ImportPostingItemsResultsList(List<PostingItemImportError> errorItems) {
        //1. Compute header columns count and domain columns ids.
        Set<String> domainColumnIds = new HashSet<String>();
        for (PostingItemImportError errorItem : errorItems){
            domainColumnIds.addAll(errorItem.getDataRow().keySet());
        }

        //2. Create data list.
        reportItems = new ArrayList<PostingItemImportErrorForReport>(errorItems.size());
        for (PostingItemImportError errorItem : errorItems){
            reportItems.add(new PostingItemImportErrorForReport(errorItem, domainColumnIds));
        }

        //3. Create columns list.
        headerColumns = new ArrayList<ColumnInfo>(domainColumnIds.size());
        int columnIndex = 0;
        for (String domainColumnId : domainColumnIds){
            headerColumns.add(new ColumnInfo(domainColumnId, "fieldValues[" + columnIndex + "]"));
            columnIndex++;
        }
        headerColumns.add(new ColumnInfo(I18nSupport.message("posting.items.import.result.list.errorMessage"), "errorMessage"));
    }

    @Override
    public String getReportTitle() {
        return null;  //Not used.
    }

    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(PostingItemImportErrorForReport.class);
        reportInfo.getColumns().addAll(headerColumns);
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return null;
    }

    @Override
    public List getReportData() {
        return reportItems;
    }

    /**
     * Wrapper for displaying error items in this report.
     */
    private class PostingItemImportErrorForReport {
        private String errorMessage;
        private List<String> fieldValues;

        PostingItemImportErrorForReport(PostingItemImportError error, Collection<String> domainColumnIds){
            errorMessage = error.getErrorMessage();
            fieldValues = new ArrayList<String>(domainColumnIds.size());
            for (String domainColumnId : domainColumnIds){
                fieldValues.add(error.getDataRow().get(domainColumnId));
            }
        }

        public String getErrorMessage(){
            return errorMessage;
        }

        public List<String> getFieldValues(){
            return fieldValues;
        }
    }
}
