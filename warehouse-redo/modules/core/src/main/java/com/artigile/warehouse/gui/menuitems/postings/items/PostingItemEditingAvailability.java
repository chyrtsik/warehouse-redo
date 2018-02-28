/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.postings.items;

import com.artigile.warehouse.bl.admin.UserService;
import com.artigile.warehouse.bl.postings.PostingService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.postings.PostingState;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;

/**
 * @author Shyrik, 17.02.2009
 */

/**
 * Availability of editing posting item.
 */
public class PostingItemEditingAvailability implements AvailabilityStrategy {
    @Override
    public boolean isAvailable(ReportCommandContext context) {
        return isAvailable((PostingItemTO) context.getCurrentReportItem());
    }

    public boolean isAvailable(PostingItemTO postingItem) {
        if (postingItem.getPosting().isPostingFromDocument()){
            //Posting items can be edited only when posting is being entered manually. In other cases all data
            //necessary is takes from related documents.
            return false;
        }
        //Posting item editing availability based both on permissions and current state of the posting.
        UserService userService = SpringServiceContext.getInstance().getUserService();
        return userService.checkPermission(WareHouse.getUserSession().getUser().getId(), PermissionType.EDIT_POSTING_ITEMS) &&
                postingItem.getPosting().getState().equals(PostingState.CONSTRUCTION);
    }

    public boolean isAvailable(long postingId) {
        //Posting item editing availability based both on permissions and current state of the posting.
        UserService userService = SpringServiceContext.getInstance().getUserService();
        PostingService postingService = SpringServiceContext.getInstance().getPostingsService();
        return userService.checkPermission(WareHouse.getUserSession().getUser().getId(), PermissionType.EDIT_POSTING_ITEMS) &&
               postingService.isPostingEditable(postingId);
    }
}
