/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.switchable;

import java.awt.*;

/**
 * Holds information about one item of a switchable view.
 *
 * @author Aliaksandr.Chyrtsik, 26.10.11
 */
public interface SwitchableViewItem {
    /**
     * Called to get view name (to display this name in view switcher).
     * @return name of a view.
     */
    String getName();

    /**
     * Called when component is needed to be created (when user requested this this view to be shown).
     * @return view component.
     */
    Component getCreateViewComponent();
}
