/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.controller;

import com.artigile.swingx.workarounds.EnclosingScrollPaneEvent;
import com.artigile.swingx.workarounds.EnclosingScrollPaneListener;
import com.artigile.swingx.workarounds.JXTableEx;
import com.artigile.warehouse.bl.userprofile.ReportStateFilter;
import com.artigile.warehouse.bl.userprofile.ReportStateService;
import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.choose.choosedialog.ChooseForm;
import com.artigile.warehouse.gui.core.report.model.ReportModel;
import com.artigile.warehouse.gui.core.report.view.TableReportView;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.userprofile.ColumnStateTO;
import com.artigile.warehouse.utils.dto.userprofile.ReportStateTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.transofmers.ReportStateTransformer;
import com.artigile.warehouse.utils.transofmers.UserTransformer;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.table.TableColumnModelExt;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.util.*;

/**
 * @author Borisok V.V., 12.02.2009
 */
public class TableHeaderMenuManager {

    public static void installTableHeaderHandler(TableReportView tableReportView, ReportDataSource dataSource) {
        installTableHeaderPopupMenu(tableReportView, dataSource);
        tableReportView.getTable().addEnclosingScrollPaneListener(new EnclosingScrollPaneHandler(tableReportView, dataSource));
    }

    private static void installTableHeaderPopupMenu(TableReportView tableReportView, ReportDataSource dataSource) {
        ReportInfo reportInfo = dataSource.getReportInfo();
        JPopupMenu menu = new JPopupMenu();
        menu.add(new TableHeaderHandler(tableReportView, reportInfo));
        tableReportView.getTable().getTableHeader().setComponentPopupMenu(menu);
    }

    private static void prepareColumnStateList(TableReportView tableReportView, ReportDataSource dataSource, ReportStateTO reportState) {
        List<ColumnStateTO> columnStateList = reportState.getColumnStates();

        List<ListItem> sourceList = new ArrayList<ListItem>();
        List<ListItem> destinationList = new ArrayList<ListItem>();
        buildSourceAndDestinationList(tableReportView, dataSource.getReportInfo(), sourceList, destinationList);

        for (ListItem item : destinationList) {
            FilterHelper helper = (FilterHelper) item.getValue();
            ColumnStateTO columnStateTO = new ColumnStateTO();
            columnStateTO.setIdentifier(helper.getColumnInfo().getField());
            columnStateTO.setReportState(reportState);
            columnStateTO.setFilterText(helper.getFilterText());
            columnStateTO.setWidth(helper.getWidth());
            columnStateTO.setVisible(true);
            columnStateList.add(columnStateTO);
        }

        for (ListItem item : sourceList) {
            FilterHelper helper = (FilterHelper) item.getValue();
            ColumnStateTO columnStateTO = new ColumnStateTO();
            columnStateTO.setIdentifier(helper.getColumnInfo().getField());
            columnStateTO.setReportState(reportState);
            columnStateTO.setFilterText(helper.getFilterText());
            columnStateTO.setWidth(helper.getWidth());
            columnStateTO.setVisible(false);
            columnStateList.add(columnStateTO);
        }
    }

    public static void storeReportState(TableReportView tableReportView, ReportDataSource dataSource) {
        UserTO userTO = WareHouse.getUserSession().getUser();
        User user = UserTransformer.transformUser(userTO);
        ReportStateFilter filter = new ReportStateFilter(user, dataSource.getReportMajor(),
                dataSource.getReportMinor());
        ReportStateService service = ReportStateTransformer.getReportStateService();
        List<ReportStateTO> list = service.getByFilter(filter);
        for (ReportStateTO reportState : list) {
            service.remove(reportState);
        }
        service.flush();
        service.clear();

        ReportStateTO reportState = new ReportStateTO();
        reportState.setReportMajor(filter.getMajor());
        reportState.setReportMinor(filter.getMinor());
        reportState.setUser(userTO);
        reportState.setFilterVisible(tableReportView.getFilterHeaderManager().getVisible());
        prepareColumnStateList(tableReportView, dataSource, reportState);
        service.save(reportState);
    }

    public static List<ReportStateTO> getDestinationList(TableReportView tableReportView, ReportDataSource dataSource) {
        List<ListItem> sourceList = new ArrayList<ListItem>();
        List<ListItem> destList = new ArrayList<ListItem>();
        buildSourceAndDestinationList(tableReportView, dataSource.getReportInfo(), sourceList, destList);
        UserTO userTO = WareHouse.getUserSession().getUser();
        User user = UserTransformer.transformUser(userTO);
        ReportStateFilter filter = new ReportStateFilter(user, dataSource.getReportMajor(),
                dataSource.getReportMinor());
        ReportStateService service = ReportStateTransformer.getReportStateService();
        return service.getByFilter(filter);
    }

    public static void restoreReportState(TableReportView tableReportView, ReportDataSource dataSource) {
        UserTO userTO = WareHouse.getUserSession().getUser();
        User user = UserTransformer.transformUser(userTO);
        ReportStateFilter filter = new ReportStateFilter(user, dataSource.getReportMajor(),
                dataSource.getReportMinor());
        ReportStateService service = ReportStateTransformer.getReportStateService();
        List<ReportStateTO> list = service.getByFilter(filter);
        if (!list.isEmpty()) {
            ReportStateTO reportState = list.get(0);
            boolean b = reportState.getFilterVisible();
            customizeColumns(tableReportView, buildVisibleColumns(reportState, dataSource.getReportInfo()));
            tableReportView.resetFilter();
            if (b) {
                tableReportView.toggleFilter();
            }
            for (ReportStateTO reportStateTO : list) {
                if (reportStateTO != reportState) {
                    service.remove(reportState);
                }
            }
            service.flush();
            service.clear();
        }
    }

    public static List<FilterHelper> buildVisibleColumns(ReportStateTO reportState, ReportInfo reportInfo) {
        Map<String, ColumnInfo> fieldToInfo = new HashMap<String, ColumnInfo>();
        List<ColumnInfo> columnInfoList = reportInfo.getColumns();
        for (ColumnInfo info : columnInfoList) {
            fieldToInfo.put(info.getField(), info);
        }

        List<FilterHelper> dstList = new ArrayList<FilterHelper>();
        List<ColumnStateTO> columnStateList = reportState.getColumnStates();
        for (ColumnStateTO columnState : columnStateList) {
            if (columnState.isVisible()) {
                ColumnInfo info = fieldToInfo.get(columnState.getIdentifier());
                if (info != null) {
                    dstList.add(new FilterHelper(info, columnState.getFilterText(), columnState.getWidth()));
                }
            }
        }

        if (dstList.isEmpty()) {
            Set<String> exists = new HashSet<String>();
            for (ColumnStateTO columnState : columnStateList) {
                ColumnInfo info = fieldToInfo.get(columnState.getIdentifier());
                if (info != null) {
                    dstList.add(new FilterHelper(info, columnState.getFilterText(), columnState.getWidth()));
                    exists.add(columnState.getIdentifier());
                }
            }
            for (ColumnInfo columnInfo : columnInfoList) {
                if (!exists.contains(columnInfo.getName())) {
                    dstList.add(new FilterHelper(columnInfo, null, null));
                }
            }
        }

        return dstList;
    }

    private static void buildSourceAndDestinationList(TableReportView tableReportView,
                                                      ReportInfo reportInfo,
                                                      List<ListItem> sourceList,
                                                      List<ListItem> destinationList) {
        Map<Integer, Integer> view2Model = new TreeMap<Integer, Integer>();

        JTable table = tableReportView.getTable();
        JTableHeader header = table.getTableHeader();

        TableModel tableModel = table.getModel();
        List<ColumnInfo> columnInfoList = reportInfo.getColumns();
        for (int i = 0; i < tableModel.getColumnCount(); ++i) {
            int viewIndex = table.convertColumnIndexToView(i);
            if (viewIndex == -1) {
                FilterHelper helper = new FilterHelper(columnInfoList.get(i), null, null);
                sourceList.add(new ListItem(helper.getTitle(), helper));
            } else {
                view2Model.put(viewIndex, i);
            }
        }

        for (int i = 0; i < view2Model.size(); ++i) {
            FilterHelper helper = new FilterHelper(
                    columnInfoList.get(view2Model.get(i)),
                    tableReportView.getFilteredText(i),
                    header.getColumnModel().getColumn(i).getWidth()
                );
            destinationList.add(new ListItem(helper.getTitle(), helper));
        }
    }

    private static Map<Integer, String> createModelIndex2FieldName(ReportModel reportModel) {
        Map<Integer, String> result = new HashMap<Integer, String>();
        List<ColumnInfo> columns = reportModel.getReportDataSource().getReportInfo().getColumns();
        for (int i = 0; i < columns.size(); ++i) {
            ColumnInfo info = columns.get(i);
            result.put(i, info.getField());
        }
        return result;
    }

    private static Map<String, Integer> createFieldName2ModelIndex(ReportModel reportModel) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        List<ColumnInfo> columns = reportModel.getReportDataSource().getReportInfo().getColumns();
        for (int i = 0; i < columns.size(); ++i) {
            ColumnInfo info = columns.get(i);
            result.put(info.getField(), i);
        }
        return result;
    }

    private static void customizeColumns(TableReportView tableReportView, List<FilterHelper> destinationList) {
        JXTableEx tableEx = tableReportView.getTable();
        TableColumnModel columnModel = tableEx.getColumnModel();
        TableColumnModelExt columnModelExt = (TableColumnModelExt) columnModel;
        ReportModel reportModel = (ReportModel) tableEx.getModel();
        Map<Integer, String> modelIndex2FieldName = createModelIndex2FieldName(reportModel);
        List<TableColumn> tableColumnList = columnModelExt.getColumns(true);
        // 1. show/hide each column at list
        for (TableColumn column : tableColumnList) {
            if (column instanceof TableColumnExt) {
                TableColumnExt columnExt = (TableColumnExt) column;
                columnExt.setVisible(false);
                String fieldName = modelIndex2FieldName.get(columnExt.getModelIndex());
                for (FilterHelper helper : destinationList){
                    String field =  helper.getColumnInfo().getField();
                    if (field.equals(fieldName)){
                        columnExt.setVisible(true);
                        tableReportView.setFilteredText(tableEx.convertColumnIndexToView(columnExt.getModelIndex()), helper.getFilterText());
                        if (helper.getWidth() != null){
                            columnExt.setPreferredWidth(helper.getWidth());
                        }
                        break;
                    }
                }
            }
        }
        // 2. reorder columns
        Map<String, Integer> fieldName2ModelIndex = createFieldName2ModelIndex(reportModel);
        for (int i = 0; i < destinationList.size(); ++i) {
            FilterHelper helper = destinationList.get(i);
            String field = helper.getColumnInfo().getField();
            int modelIndex = fieldName2ModelIndex.get(field);
            int viewIndex = tableEx.convertColumnIndexToView(modelIndex);
            columnModelExt.moveColumn(viewIndex, i);
        }
    }

    private static class TableHeaderHandler extends AbstractAction {

        public TableHeaderHandler(TableReportView tableReportView, ReportInfo reportInfo) {
            super(I18nSupport.message("header.menuitem.configure"));
            this.tableReportView = tableReportView;
            this.reportInfo = reportInfo;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            List<ListItem> sourceList = new ArrayList<ListItem>();
            List<ListItem> destinationList = new ArrayList<ListItem>();
            buildSourceAndDestinationList(tableReportView, reportInfo, sourceList, destinationList);
            if (Dialogs.runProperties(new ChooseForm(I18nSupport.message("header.configure"), sourceList, destinationList))) {
                customizeColumns(tableReportView, convert(destinationList));
            }
        }

        private List<FilterHelper> convert(List<ListItem> listItemList) {
            List<FilterHelper> helpersList = new ArrayList<FilterHelper>();
            for (ListItem item : listItemList){
                helpersList.add((FilterHelper)item.getValue());
            }
            return helpersList;
        }

        private TableReportView tableReportView;
        private ReportInfo reportInfo;
    }

    private static class EnclosingScrollPaneHandler implements EnclosingScrollPaneListener {

        private EnclosingScrollPaneHandler(TableReportView tableReportView, ReportDataSource dataSource) {
            this.tableReportView = tableReportView;
            this.dataSource = dataSource;
        }
        
        @Override
        public void configure(EnclosingScrollPaneEvent event) {
            tableReportView.getFilterHeaderManager().install(tableReportView.getTable());
            restoreReportState(tableReportView, dataSource);
        }

        @Override
        public void unconfigure(EnclosingScrollPaneEvent event) {
            storeReportState(tableReportView, dataSource);
            tableReportView.getFilterHeaderManager().uninstall();
        }

        private TableReportView tableReportView;
        private ReportDataSource dataSource;
    }


    public static class FilterHelper {
        private ColumnInfo columnInfo;
        private String filterText;
        private Integer width;

        private FilterHelper(ColumnInfo columnInfo, String filterText, Integer width) {
            this.columnInfo = columnInfo;
            this.filterText = filterText;
            this.width = width;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof FilterHelper && columnInfo.getName().equals(((FilterHelper) obj).getTitle());
        }

        public ColumnInfo getColumnInfo() {
            return columnInfo;
        }

        public void setColumnInfo(ColumnInfo columnInfo) {
            this.columnInfo = columnInfo;
        }

        public String getTitle() {
            return columnInfo.getName();
        }

        public String getFilterText() {
            return filterText;
        }

        public void setFilterText(String filterText) {
            this.filterText = filterText;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }
    }
}
