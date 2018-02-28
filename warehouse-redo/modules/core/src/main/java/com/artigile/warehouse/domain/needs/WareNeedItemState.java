/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.needs;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 25.02.2009
 */

/**
 * States of the ware need items.
 */
public enum WareNeedItemState {
    /**
     * The initial state of the need item. In this state ware need is noted, but not found.
     */
    NOT_FOUND,

    /**
     * This state means, that there are supplier of the need, who can sell it. In this state
     * need is in the purchase list for some supplier.
     */
    FOUND,

    /**
     * Means, that purchase with this need has been ordered at the supplier.
     */
    ORDERED,

    /**
     * This state means, that need has been shipped from the supplier.
     */
    SHIPPED,

    /**
     * This state means, that need has been shipped from the supplier and has been received on the stock.
     *  This is the end state of the ware need, in which is stays forever and don't display in ware need list.
     */
    CLOSED;

    /**
     * Use this method to obtain name of ware need items states.
     * @return
     */
    public String getName() {
        if (this.equals(NOT_FOUND)){
            return I18nSupport.message("wareNeed.item.state.name.notFound");
        }
        else if (this.equals(FOUND)){
            return I18nSupport.message("wareNeed.item.state.name.found");
        }
        else if (this.equals(ORDERED)){
            return I18nSupport.message("wareNeed.item.state.name.ordered");
        }
        else if (this.equals(SHIPPED)){
            return I18nSupport.message("wareNeed.item.state.name.shipped");
        }
        else if (this.equals(CLOSED)) {
            return I18nSupport.message("wareNeed.item.state.name.closed");
        }
        else {
            throw new RuntimeException("Invalid ware need item state value.");
        }
    }

    /**
     * Get name of enum consatnt.
     * @return name of enum object.
     */
    public String getEnumConstantName() {
        if (this.equals(NOT_FOUND)){
            return NOT_FOUND.name();
        }
        else if (this.equals(FOUND)){
            return FOUND.name();
        }
        else if (this.equals(ORDERED)){
            return ORDERED.name();
        }
        else if (this.equals(SHIPPED)){
            return SHIPPED.name();
        }
        else if (this.equals(CLOSED)) {
            return CLOSED.name();
        }
        else {
            throw new RuntimeException("Invalid ware need item state value.");
        }
    }

    public static WareNeedItemState getInitialState() {
        return NOT_FOUND;
    }
}
