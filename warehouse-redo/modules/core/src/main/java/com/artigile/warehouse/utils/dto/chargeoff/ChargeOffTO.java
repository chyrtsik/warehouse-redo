/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.chargeoff;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 09.10.2009
 */

/**
 * Full data of charge off.
 */
public class ChargeOffTO extends ChargeOffTOForReport{
    private List<ChargeOffItemTO> items = new ArrayList<ChargeOffItemTO>();

    public List<ChargeOffItemTO> getItems() {
        return items;
    }

    public void setItems(List<ChargeOffItemTO> items) {
        this.items = items;
    }
}
