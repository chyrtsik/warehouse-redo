/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.nboptions.whview;

import com.artigile.warehouse.gui.nboptions.OptionsPanel;
import com.artigile.warehouse.gui.nboptions.OptionsPanelBaseController;
import org.netbeans.spi.options.OptionsPanelController;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
@OptionsPanelController.TopLevelRegistration(
        categoryName = "#warehouse.options.view.title",
        position = 1,
        iconBase = "images/options_view.png",
        keywords = "#warehouse.options.view.keywords",
        keywordsCategory = "#warehouse.options.view.keywords.category")
public final class WarehouseViewOptionsPanelController extends OptionsPanelBaseController {

    @Override
    protected OptionsPanel getPanel() {
        if (panel == null) {
            panel = new WarehouseViewOptionsPanel();
        }
        return panel;
    }
}
