/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.nboptions;


import javax.swing.*;
import java.awt.*;

/**
 * Component that describes required functionality for the panels with system options.
 *
 * This class will be used as a parent for yours concrete implementation of panel.
 * As example, see <code>WarehouseViewOptionsPanel</code>.
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public abstract class OptionsPanel extends JPanel {

    /**
     * Loads options.
     */
    protected abstract void load();

    /**
     * Stores current options.
     */
    protected abstract void store();

    /**
     * @return True - options are valid and can be saved.
     *         False - options aren't valid and can't be saved.
     */
    protected abstract boolean valid();

    /**
     * Applies default layout for this panel.
     * Use this method before adding custom panel on the NetBeans option panel.
     * For example, see <code>WarehouseViewOptionsPanel</code>
     */
    protected void applyDefaultLayout() {
        this.setLayout(new GridLayout(1, 1));
    }
}
