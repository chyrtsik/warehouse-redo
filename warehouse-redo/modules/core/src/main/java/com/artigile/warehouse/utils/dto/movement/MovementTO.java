/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.movement;

import java.util.List;

/**
 * @author Shyrik, 22.11.2009
 */

public class MovementTO extends MovementTOForReport {
    private List<MovementItemTO> items;

    //====================== Getters and setters ===============================
    public List<MovementItemTO> getItems() {
        return items;
    }

    public void setItems(List<MovementItemTO> items) {
        this.items = items;
    }
}
