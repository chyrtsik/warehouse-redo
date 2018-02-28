/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.splitter;

import com.artigile.warehouse.gui.menuitems.complecting.ComplectingTaskWorkerList;
import com.artigile.warehouse.gui.menuitems.movement.items.MovementItemsEditor;
import com.artigile.warehouse.gui.menuitems.needs.WareNeedItemsEditor;
import com.artigile.warehouse.gui.menuitems.orders.items.OrderItemsEditor;
import com.artigile.warehouse.gui.menuitems.postings.items.PostingItemsEditor;
import com.artigile.warehouse.gui.menuitems.purchase.items.PurchaseItemsEditor;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Valery Barysok, 09.04.2010
 */
public final class SplitPaneManager {
    public static SplitPaneManager instance;

    private List<SplitPaneSettings> splitPaneSettings = new ArrayList<SplitPaneSettings>();

    private SplitPaneManager() {
    }

    public static SplitPaneManager getInstance() {
        if (instance == null) {
            instance = new SplitPaneManager();
            instance.initialize();
        }

        return instance;
    }

    private void initialize() {
        register(ComplectingTaskWorkerList.class, I18nSupport.message("splitpane.complecting.task.worker.list.title"));
        register(MovementItemsEditor.class, I18nSupport.message("splitpane.movement.items.editor.title"));
        register(OrderItemsEditor.class, I18nSupport.message("splitpane.order.items.editor.title"));
        register(PostingItemsEditor.class, I18nSupport.message("splitpane.posting.items.editor.title"));
        register(PurchaseItemsEditor.class, I18nSupport.message("splitpane.purchase.items.editor.title"));
        register(WareNeedItemsEditor.class, I18nSupport.message("splitpane.wareneed.items.editor.title"));
    }

    private void register(Class clazz, String title) {
        splitPaneSettings.add(new SplitPaneSettings(clazz, title));
    }

    public List<SplitPaneSettings> getSplitPaneSettings() {
        return Collections.unmodifiableList(splitPaneSettings);
    }
}
