/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.choose;

import java.util.EventListener;

/**
 * @author Borisok V.V., 17.01.2009
 */
public interface ChooseListener extends EventListener {
    public void sourceChanged(ChooseEvent event);

    public void destinationChanged(ChooseEvent event);

    public void sourceOrderChanged(ChooseEvent event);

    public void destinationOrderChanged(ChooseEvent event);
}
