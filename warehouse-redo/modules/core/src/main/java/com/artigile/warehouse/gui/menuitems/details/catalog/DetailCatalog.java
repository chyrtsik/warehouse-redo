/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.catalog;

import com.artigile.warehouse.bl.detail.DetailCatalogService;
import com.artigile.warehouse.gui.core.plugin.FramePlugin;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSource;
import com.artigile.warehouse.gui.core.report.controller.TableReport;
import com.artigile.warehouse.gui.core.report.controller.TreeReport;
import com.artigile.warehouse.gui.core.report.controller.TreeReportViewType;
import com.artigile.warehouse.gui.core.report.view.ReportSelectionListener;
import com.artigile.warehouse.gui.menuitems.details.batchesext.DetailBatchesExtList;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailCatalogStructureTO;
import com.artigile.warehouse.utils.dto.details.DetailGroupTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.properties.savers.SplitPaneSaver;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author IoaN, Feb 28, 2009
 *         <p/>
 *         Details catalog class. Used to display Tree and details that corespond selected item in tree.
 *         The view devided in two parts(left and right).<br>
 *         Left represents treee of details. The right represents list of details that correspond selected tree item.
 *         At the initial time right part contails all the details and updates whe user selects any detal group in tree.
 */

public class DetailCatalog extends FramePlugin {
    private JPanel mainView;
    private JPanel catalogTreePanel;
    private JPanel detailsListPanel;
    private JSplitPane splitPane;

    /**
     * Service that will deal with db requests.
     */
    private DetailCatalogService detailCatalogService = SpringServiceContext.getInstance().getDetailCatalogService();

    /**
     * Left tree model.
     */
    private TreeReport detailGroupsTree;

    /**
     * If set then represents parent report in which this catalog is shown.
     */
    private FramePlugin parentReport;

    /**
     * Factory for detail bathes list creation in this catalog.
     */
    private DetailCatalogBatchesListFactory detailsListFactory;

    /**
     * Holds splitter orientation (true -- then tree under details list, false -- tree in the left and details in the right).
     */
    private boolean verticalSplit;

    /**
     * Currently shown detail batches list (list of details in the slected catalog group).
     */
    private TableReport currentDetailBatchesReport;

    /**
     * Default constructor. Builds left tree and initial data for details. When details catalog shown first time it
     * contains all the details.
     */
    public DetailCatalog() {
        this(null, new DefaultDetailCatalogBatchesListFactory(), false);
    }

    public DetailCatalog(FramePlugin parentReport, DetailCatalogBatchesListFactory detailsListFactory, boolean verticalSplit) {
        super(parentReport);

        this.parentReport = parentReport;
        this.detailsListFactory = detailsListFactory;
        this.verticalSplit = verticalSplit;

        initSplitPane();

        buildTree();
        refreshDetailsList();
        mainView.revalidate();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("detail.catalog.list.properties.title");
    }

    @Override
    public JPanel getContentPanel() {
        return mainView;
    }

    /**
     * Builds left part of the split panel: tree.<br>
     * Adds tree listeners to listen for any click on tree.
     * When click action is caught the right part of view is updated according selected in tree item.
     */

    private void buildTree() {
        DetailCatalogStructureTO tempCatalogStructure = new DetailCatalogStructureTO(detailCatalogService.getStructureWithAutoGroups());
        detailGroupsTree = new TreeReport(new DetailCatalogTree(tempCatalogStructure, false), TreeReportViewType.TREE); //Apply model to view
        detailGroupsTree.getReportView().addSelectionListener(new ReportSelectionListener() {
            @Override
            public void onSelectionChanged() {
                refreshDetailsList();
            }
        });
        catalogTreePanel.add(detailGroupsTree.getReportComponent(), GridLayoutUtils.getResizableAndFillingCellConstraints()); //put tree view into left part of the split panel
    }

    private DetailGroupTO getSelectedGroup() {
        if (detailGroupsTree.getSelectedItems() == null || detailGroupsTree.getSelectedItems().size() == 0) {
            return null;
        } else {
            return (DetailGroupTO) detailGroupsTree.getSelectedItems().get(0);
        }
    }

    /**
     * Refreshes right panel data according selected item(items) in tree.
     * <p/>
     * treeItemId - list of selected groups in left panel(selected tree items). For now only one item can be
     * selected on tree, but if in future tree will allow to select multiple items this filter will already support
     * this functionality.
     */
    private void refreshDetailsList() {
        detailsListPanel.removeAll();

        if (currentDetailBatchesReport != null) {
            //Free detail bathes list resources and remove references to prevent multiple instances of this
            //report to be held in memory.
            currentDetailBatchesReport.dispose();
            currentDetailBatchesReport = null;
        }

        DetailGroupTO selectedGroup = getSelectedGroup();
        if (selectedGroup != null) {
            //Show content of selected catalog groups.
            ReportDataSource detailBatchesList = detailsListFactory.createDetailBatchesDataSource(selectedGroup);
            currentDetailBatchesReport = new TableReport(detailBatchesList, this);
            detailsListPanel.add(currentDetailBatchesReport.getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
        } else {
            //When no groups are selected, just show instructions how to view catalog details.
            detailsListPanel.add(new JLabel(I18nSupport.message("detail.catalog.select.group")), GridLayoutUtils.getCenteredCellConstraints());
        }
        detailsListPanel.revalidate();
    }

    private void initSplitPane() {
        splitPane.setOrientation(verticalSplit ? JSplitPane.VERTICAL_SPLIT : JSplitPane.HORIZONTAL_SPLIT);
        SplitPaneSaver.restore(splitPane, getFrameId());
    }

    @Override
    protected void onFrameClosed() {
        SplitPaneSaver.store(splitPane, getFrameId());
        super.onFrameClosed();
    }

    @Override
    public String getFrameId() {
        if (parentReport != null) {
            //Use id of parent report (because when this report is aggregated when it's state is a part of parent state).
            return "DetailCatalog.in." + parentReport.getFrameId();
        } else {
            return super.getFrameId();
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainView = new JPanel();
        mainView.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane = new JSplitPane();
        splitPane.setDividerLocation(228);
        mainView.add(splitPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(100, 100), null, null, 0, false));
        catalogTreePanel = new JPanel();
        catalogTreePanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane.setLeftComponent(catalogTreePanel);
        detailsListPanel = new JPanel();
        detailsListPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane.setRightComponent(detailsListPanel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainView;
    }

    /**
     * Detail detail batches creation implementation. Creates extended price list.
     */
    public static class DefaultDetailCatalogBatchesListFactory implements DetailCatalogBatchesListFactory {
        @Override
        public ReportDataSource createDetailBatchesDataSource(DetailGroupTO catalogGroup) {
            return new DetailBatchesExtList(catalogGroup, catalogGroup.getIdPath());
        }
    }
}
