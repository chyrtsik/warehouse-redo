/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.postings;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 02.02.2009
 */

/**
 * Enumeration with all possible states of posting document.
 */
public enum PostingState {
    /**
     * Initial state of the posting. In this state user initially edit posting document (construct
     * it's set of elements).
     */
    CONSTRUCTION(I18nSupport.message("posting.state.name.construction")),

    /**
     * In this state posting considered to be completed. It is stored for history and not
     * available for editing.
     */
    COMPLETED(I18nSupport.message("posting.state.name.completed"));

    //====================== Construction and operations ===================================
    private String name;

    private PostingState(String name) {
        this.name = name;
    }

    /**
     * Use this method to obtain name of each posting state.
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the first state in the posting's lifecycle.
     * @return
     */
    public static PostingState getInitialState() {
        return CONSTRUCTION;
    }
}
