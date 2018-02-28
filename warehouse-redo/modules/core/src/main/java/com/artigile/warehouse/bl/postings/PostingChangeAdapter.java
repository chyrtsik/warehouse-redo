/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.postings;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.domain.postings.Posting;

/**
 * @author Shyrik, 08.11.2009
 */
public class PostingChangeAdapter implements PostingChangeListener {
    @Override
    public void onPostingCreated(Posting posting) throws BusinessException {
    }

    @Override
    public void onPostingDeleted(Posting posting) throws BusinessException {
    }

    @Override
    public void onPostingCompleted(Posting posting) throws BusinessException {
    }
}
