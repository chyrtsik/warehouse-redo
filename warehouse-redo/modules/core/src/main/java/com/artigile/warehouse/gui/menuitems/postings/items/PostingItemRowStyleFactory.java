/*
 * Copyright (c) 2007-2012 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.postings.items;

import com.artigile.warehouse.gui.core.report.style.Style;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;

import java.awt.*;

/**
 * @author Aliaksandr.Chyrtsik, 18.11.12
 */
public class PostingItemRowStyleFactory implements StyleFactory {
    private static final Style LAST_EDITED_ITEM_STYLE = new Style(new Color(200, 255, 200));
    private static final Style LAST_EDITED_UNCLASSIFIED_ITEM_STYLE = new Style(new Color(200, 255, 200), new Color(255, 0, 0));
    private static final Style UNCLASSIFIED_ITEM_STYLE = new Style(null, new Color(255, 0, 0));

    private LastEditedItemProvider lastEditedItemProvider;

    public PostingItemRowStyleFactory(LastEditedItemProvider lastEditedItemProvider) {
        this.lastEditedItemProvider = lastEditedItemProvider;
    }

    @Override
    public Style getStyle(Object rowData) {
        PostingItemTO postingItem = (PostingItemTO)rowData;
        Long lastEditedItemId = lastEditedItemProvider == null ? null : lastEditedItemProvider.getLastEditedItemId();
        if (postingItem != null){
            if (lastEditedItemId != null && postingItem.getId() == lastEditedItemId){
                //Last edited item will be highlighted.
                return postingItem.isUnclassifiedItem() ? LAST_EDITED_UNCLASSIFIED_ITEM_STYLE : LAST_EDITED_ITEM_STYLE;
            }
            else if (postingItem.isUnclassifiedItem()){
                return UNCLASSIFIED_ITEM_STYLE;
            }
        }
        return null;
    }
}
