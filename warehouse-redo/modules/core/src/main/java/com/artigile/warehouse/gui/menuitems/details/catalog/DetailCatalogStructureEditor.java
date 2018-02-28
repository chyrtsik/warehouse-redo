/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.catalog;

import com.artigile.warehouse.bl.admin.UserService;
import com.artigile.warehouse.bl.detail.DetailCatalogService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.plugin.Plugin;
import com.artigile.warehouse.gui.core.plugin.PluginParams;
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailCatalogStructureTO;

/**
 * @author Shyrik, 03.01.2009
 */

/**
 * Plugin executes editor of the detail groups.
 */
public class DetailCatalogStructureEditor implements Plugin {
    @Override
    public void run(PluginParams params) {
        DetailCatalogStructureTO catalogStructure = getDetailCatalogService().getStructure();
        PropertiesForm prop = new DetailCatalogStructureForm(catalogStructure, hasEditPermission());
        PropertiesDialog propDialog = new PropertiesDialog(prop);
        if (propDialog.run()){
            getDetailCatalogService().saveStructure(catalogStructure);            
        }
    }

    //================================= Helpers ================================================
    private DetailCatalogService getDetailCatalogService() {
        return SpringServiceContext.getInstance().getDetailCatalogService();
    }

    private boolean hasEditPermission() {
        UserService userService = SpringServiceContext.getInstance().getUserService();
        return userService.checkPermission(WareHouse.getUserSession().getUser().getId(), PermissionType.EDIT_DETAIL_GROUPS);
    }
}
