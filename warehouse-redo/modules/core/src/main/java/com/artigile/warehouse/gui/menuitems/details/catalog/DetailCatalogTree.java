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

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.controller.TreeReportDataSource;
import com.artigile.warehouse.gui.core.report.model.TreeReportModel;
import com.artigile.warehouse.utils.dto.details.DetailCatalogStructureTO;
import com.artigile.warehouse.utils.dto.details.DetailGroupTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * Tree for working with details catalog groups.
 *
 * @author Shyrik, 04.01.2009
 */
public class DetailCatalogTree implements TreeReportDataSource {
    /**
     * Editing structure of the details catalog.
     */
    private DetailCatalogStructureTO catalogStructure;

    /**
     * This flag shows if the commands can be used on the tree. if enabled=false no commands allowed to user(e.g. Detail Catalog)
     * otherwise, if enabled=true all commands are allowed to user.
     * Business logic described in {@link DetailCatalogStructureEditingStrategy}
     */
    private boolean enabled;

    public DetailCatalogTree(DetailCatalogStructureTO catalogStructure, boolean enabled) {
        this.enabled = enabled;
        this.catalogStructure = catalogStructure;
    }

    public DetailCatalogTree(DetailCatalogStructureTO catalogStructure) {
        this(catalogStructure, true);
    }

    @Override
    public ReportInfo getReportInfo() {
        ReportInfo reportInfo = new ReportInfo(DetailGroupTO.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.catalog.group.name"), "name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.catalog.group.description"), "description"));
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new DetailCatalogStructureEditingStrategy(catalogStructure,enabled);
    }

    @Override
    public TreeReportModel getTreeReportModel() {
        return new DetailCatalogStructureTreeModel(catalogStructure);
    }
}
