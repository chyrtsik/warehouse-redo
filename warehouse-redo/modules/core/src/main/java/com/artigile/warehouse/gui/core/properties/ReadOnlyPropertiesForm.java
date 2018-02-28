/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties;

import javax.swing.*;

/**
 * Interface of properties form for read only access.
 *
 * @author Aliaksandr.Chyrtsik, 07.11.11
 */
public interface ReadOnlyPropertiesForm {
    /**
     * @return should return title of the properties dialog.
     */
    public String getTitle();

    /**
     * @return should return panel, that is containing all components to be shown in the dialog.
     */
    public JPanel getContentPanel();

    /**
     * Called when it's time to load data into controls.
     */
    public void loadData();
}
