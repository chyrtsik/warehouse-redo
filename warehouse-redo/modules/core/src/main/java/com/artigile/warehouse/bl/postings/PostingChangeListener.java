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

/**
 * Interface of a listener of changes, made on posting documents.
 */
public interface PostingChangeListener {
    /**
     * Called, when new posting is created.
     * @param posting posting, that was just created.
     * @throws com.artigile.warehouse.bl.common.exceptions.BusinessException
     */
    void onPostingCreated(Posting posting) throws BusinessException;

    /**
     * Called, when new posting is deleted.
     * @param posting posting, that was just created.
     * @throws com.artigile.warehouse.bl.common.exceptions.BusinessException
     */
    void onPostingDeleted(Posting posting) throws BusinessException;

    /**
     * Called, when posting was just completed.
     * @param posting posting, which state was changed.
     * @throws com.artigile.warehouse.bl.common.exceptions.BusinessException
     */
    void onPostingCompleted(Posting posting) throws BusinessException;
}
