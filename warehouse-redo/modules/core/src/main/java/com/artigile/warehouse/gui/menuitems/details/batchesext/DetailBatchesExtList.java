/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.batchesext;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.menuitems.details.batches.DetailBatchesList;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldValueTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldsHeaderMap;
import com.artigile.warehouse.utils.dto.details.DetailGroupTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.Iterator;
import java.util.List;

/**
 * This class builds the table for details with different fitldes names. First it builds one Map that contains all
 * possible columns for the talbes. After that this function rebuilds detail.mode.type[] array - inserts empty label
 * into each detail where it detail has field propery.
 * <p/>
 * Example:<br>
 * if we have two details: transistor and condensator and they both have <b>name</b> field.<br>
 * condensator has <b>conF1</b> and <b>conF2</b> fields<br>
 * transistor has <b>transF1</b> field and  <b>tranF2</b> fields.
 * so the table should look like:<br>
 * <table border=1>
 * <tr><td>Name</td><td>conF1</td><td>conF2</td><td>tranF1</td><td>tranF2</td></tr>
 * <tr><td>Condensator</td><td>f1</td><td>f2</td><td><p/></td><td><p/></td></tr>
 * <tr><td>Transistor</td><td></td><td></td><td>F1</td><td>F2</td></tr>
 * </table
 * <p/>
 * So every detail has to contain all it's own fields  and empty fields that come from other details.
 *
 * @author IoaN, Feb 26, 2009
 */

public class DetailBatchesExtList extends DetailBatchesList {

    private List<DetailBatchTO> details;

    private DetailBatchesEditExtStrategy detailBatchesEditExtStrategy;

    public DetailBatchesExtList() {
        this(null);
    }

    @Override
    public String getReportTitle() {
        return I18nSupport.message("detail.batchesExt.list.title");
    }

    public DetailBatchesExtList(DetailGroupTO catalogGroup) {
        this(catalogGroup, "0");
    }

    /**
     * @param catalogGroup catalog group for details filtering.
     * @param reportMinor id that used for tables header.
     */
    public DetailBatchesExtList(DetailGroupTO catalogGroup, String reportMinor) {
        super(catalogGroup, reportMinor);
    }


    /**
     * 1) Builds report info. The report info depends on {@code filter}. If {@code filter} is null the all DetailBatches
     * will be taken from DB. Otherwise the detailBatches list will be constructed according {@code filter}.<br>
     * 2) Builds table's header.<br>
     * 3) Fill the details types field with empty values where it necessary.
     * 4) Builds initial ReportInfo object filled by all possible columns that taken from Detail's types.
     *
     * @return
     */
    @Override
    public ReportInfo doGetReportInfo() {
        //1. Initialize report info as usial.
        ReportInfo reportInfo = super.doGetReportInfo();

        //2. Building header for all detail batch fields
        DetailFieldsHeaderMap headerMap = new DetailFieldsHeaderMap("model.fields", createDetailsFieldsIterable(details));
        detailBatchesEditExtStrategy = new DetailBatchesEditExtStrategy(headerMap);
        createEmptyFieldsForDetails();
        for (String headerMapKey : headerMap.getMappingKeySet()) {
            reportInfo.addColumn(new ColumnInfo(headerMapKey, headerMap.getMappingByFieldName(headerMapKey).getExpression()));
        }

        return reportInfo;
    }

    private Iterable<List<DetailFieldValueTO>> createDetailsFieldsIterable(final List<DetailBatchTO> details) {
        //Create iterable object to enumerating all fields of all products in the given list of products.
        return new Iterable<List<DetailFieldValueTO>>() {
            @Override
            public Iterator<List<DetailFieldValueTO>> iterator() {
                return new Iterator<List<DetailFieldValueTO>>() {
                    private Iterator<DetailBatchTO> iterator = details.iterator();
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }
                    @Override
                    public List<DetailFieldValueTO> next() {
                        return iterator.next().getModel().getFields();
                    }
                    @Override
                    public void remove() {
                        iterator.remove();
                    }
                };
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public List getReportData() {
        if (details == null){
            details = super.getReportData();
        }
        return details;
    }

    @Override
    public Object preProcessItem(Object reportItem) {
        return detailBatchesEditExtStrategy.prepareDetailForReport((DetailBatchTO) reportItem);
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return detailBatchesEditExtStrategy;
    }

    /**
     * For every detail calls empty field creator in {@link com.artigile.warehouse.gui.menuitems.details.batchesext.DetailBatchesEditExtStrategy} class
     */
    private void createEmptyFieldsForDetails() {
        for (DetailBatchTO detail : details) {
            detailBatchesEditExtStrategy.prepareDetailForReport(detail);
        }
    }
}
