/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.serialnumbers;

import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.gui.core.report.controller.*;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldValueTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldsHeaderMap;
import com.artigile.warehouse.utils.dto.details.DetailSerialNumberTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Report with serial numbers.
 * @author Aliaksandr Chyrtsik
 * @since 22.06.13
 */
public class SerialNumberList extends ReportDataSourceBase implements ReportPrintProvider {
    /**
     * Cached serial numbers.
     */
    List<DetailSerialNumberTO> serialNumbers;

    /**
     * Helper map to map serial number fields to report columns.
     */
    private DetailFieldsHeaderMap headerMap;

    @Override
    public String getReportTitle() {
        return I18nSupport.message("serial.number.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(DetailSerialNumberTO.class);
        reportInfo.getOptions().setPrintProvider(this);

        //1. Initialize predefined columns.
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("serial.number.list.id"), "id"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("serial.number.list.detail.type"), "detail.type"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("serial.number.list.detail.name"), "detail.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("serial.number.list.detail.misc"), "detail.misc"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("serial.number.list.detail.notice"), "detail.notice"));

        //2. Initialize user defined columns.
        for (String headerMapKey : getHeaderMap().getMappingKeySet()) {
            reportInfo.addColumn(new ColumnInfo(headerMapKey, getHeaderMap().getMappingByFieldName(headerMapKey).getExpression()));
        }

        return reportInfo;
    }

    private DetailFieldsHeaderMap getHeaderMap() {
        if (headerMap == null){
            //This is a tricky part. To create header map we have to load list of serial numbers first.
            //But list of serial numbers to be used in report depends on header map. So we initialize header
            //indirectly while loading list of serial numbers.
            getReportData();
        }
        return headerMap;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new SerialNumberEditingStrategy(this);
    }

    @Override
    public List<DetailSerialNumberTO> getReportData() {
        if (serialNumbers == null){
            // TODO: need to be updated on refresh (issue method: getHeaderMap)
            serialNumbers = prepareSerialNumbersForReport(SpringServiceContext.getInstance().getDetailSerialNumberService().getAll());
        }
        return serialNumbers;
    }

    @Override
    public PrintTemplateType[] getReportPrintTemplates() {
        return new PrintTemplateType[]{PrintTemplateType.TEMPLATE_SERIAL_NUMBER_LIST};
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getReportDataForPrinting() {
        return new SerialNumbersForPrinting(tableReport.getDisplayedReportItems());
    }

    private List<DetailSerialNumberTO> prepareSerialNumbersForReport(List<DetailSerialNumberTO> serialNumbers) {
        headerMap = new DetailFieldsHeaderMap("fields", createSerialNumbersFieldsIterable(serialNumbers));
        for (DetailSerialNumberTO serialNumber : serialNumbers){
            prepareSerialNumberForReport(serialNumber);
        }
        return serialNumbers;
    }

    /**
     * In every serial number creates empty labels in corresponding places. <br>
     * See DetailBatchesEditExtStrategy.prepareDetailForReport method for details.
     *
     * @param serialNumber (in|out) serial number to be modified.
     */
    public void prepareSerialNumberForReport(DetailSerialNumberTO serialNumber) {
        List<DetailFieldValueTO> changedFieldsList = new ArrayList<DetailFieldValueTO>(headerMap.getMappingSize());
        for (int i = 0; i < headerMap.getMappingSize(); i++) {
            changedFieldsList.add(getEmptyField());
        }
        int i = 0;
        for (DetailFieldValueTO detailFieldValueTO : serialNumber.getFields()) {
            String fieldName = detailFieldValueTO.getType().getName();
            DetailFieldsHeaderMap.FieldMapping fieldMapping = headerMap.getMappingByFieldName(fieldName);
            if (fieldMapping != null) {
                changedFieldsList.set(fieldMapping.getIndex(), detailFieldValueTO);
            }
        }
        serialNumber.setFields(changedFieldsList);
    }

    private Iterable<List<DetailFieldValueTO>> createSerialNumbersFieldsIterable(final List<DetailSerialNumberTO> serialNumbers) {
        //Create iterable object to enumerating all fields of all products in the given list of products.
        return new Iterable<List<DetailFieldValueTO>>() {
            @Override
            public Iterator<List<DetailFieldValueTO>> iterator() {
                return new Iterator<List<DetailFieldValueTO>>() {
                    private Iterator<DetailSerialNumberTO> iterator = serialNumbers.iterator();
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }
                    @Override
                    public List<DetailFieldValueTO> next() {
                        return iterator.next().getFields();
                    }
                    @Override
                    public void remove() {
                        iterator.remove();
                    }
                };
            }
        };
    }

    /**
     * Creates an empty cell for displaying in table.
     */
    private static DetailFieldValueTO emptyField;
    static{
        DetailFieldTO detailFieldTO = new DetailFieldTO();
        detailFieldTO.setName("empty");
        detailFieldTO.setFieldIndex(-1);
        emptyField = new DetailFieldValueTO(detailFieldTO, new DetailSerialNumberTO());
    }
    private DetailFieldValueTO getEmptyField() {
        return emptyField;
    }
}

