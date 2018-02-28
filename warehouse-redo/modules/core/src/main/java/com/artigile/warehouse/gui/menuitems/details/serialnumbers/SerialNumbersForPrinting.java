/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.serialnumbers;

import com.artigile.warehouse.utils.dto.details.DetailSerialNumberTO;

import java.util.List;

/**
 * Container for serial numbers to be passed to printing engine.
 * @author Aliaksandr Chyrtsik
 * @since 30.06.13
 */
public class SerialNumbersForPrinting {
    private final List<DetailSerialNumberTO> serialNumbers;

    public SerialNumbersForPrinting(List<DetailSerialNumberTO> serialNumbers) {
        this.serialNumbers = serialNumbers;
    }

    public List<DetailSerialNumberTO> getItems(){
        return serialNumbers;
    }
}
