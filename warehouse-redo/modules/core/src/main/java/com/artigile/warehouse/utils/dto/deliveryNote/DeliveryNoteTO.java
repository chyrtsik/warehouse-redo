/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.deliveryNote;

import java.util.List;

/**
 * @author Shyrik, 05.11.2009
 */
public class DeliveryNoteTO extends DeliveryNoteTOForReport {
    private List<DeliveryNoteItemTO> items;

    public List<DeliveryNoteItemTO> getItems() {
        return items;
    }

    public void setItems(List<DeliveryNoteItemTO> items) {
        this.items = items;
    }
}
