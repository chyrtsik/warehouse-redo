/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.postings.items;

import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;

/**
 * @author Shyrik, 17.02.2009
 */

/**
 * Saver of count of posting item.
 */
public class PostingItemCountSaver extends PostingItemCellEditingStrategy {
    @Override
    protected void doSave(PostingItemTO postingItem, Object newValue) {
        String value = (String)newValue;
        if (value.isEmpty()){
            postingItem.setCount(null);
        }
        else{
            if (!StringUtils.isNumberLong(value)){
                return;
            }

            Long count = Long.valueOf(value);
            if (count <= 0){
                return;
            }

            postingItem.setCount(count);
        }
        SpringServiceContext.getInstance().getPostingsService().savePostingItem(postingItem);
    }
}
