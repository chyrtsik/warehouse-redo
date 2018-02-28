/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.utils.fields.editor.impl;

import com.artigile.warehouse.utils.dto.details.DetailFieldTO;

/**
 * Interface of the listener of the changing field value event.
 * @author Shyrik, 25.12.2008
 */
public interface FieldChangeListener {
    /**
     *  Usially event is triggered, when field value is being editing by the user.
     * @param field - field, it's editor fires the event.
     */
    void fieldValueChanged(DetailFieldTO field);
}
