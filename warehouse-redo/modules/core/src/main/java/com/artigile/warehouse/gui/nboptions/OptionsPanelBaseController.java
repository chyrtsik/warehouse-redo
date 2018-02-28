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

import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

import javax.swing.*;
import java.beans.PropertyChangeListener;

/**
 * Base controller for all panels with system options.
 * Each panel is have own concrete controller, that extends this controller.
 *
 * As example, see <code>WarehouseViewOptionsPanelController</code>
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public abstract class OptionsPanelBaseController extends OptionsPanelController {

    /**
     * Options panel.
     * Available for the all child classes.
     */
    protected OptionsPanel panel;


    /**
     * Each child class should implement this method and should return concrete realization of panel with system options.
     *
     * @return Panel with system options
     */
    protected abstract OptionsPanel getPanel();

    /**
     * Invokes when opening panel with options.
     */
    @Override
    public void update() {
        getPanel().load();
    }

    /**
     * Invokes when user clicks 'OK'.
     */
    @Override
    public void applyChanges() {
        if (getPanel().valid()) {
            getPanel().store();
        }
    }

    /**
     * Invokes when user clicks 'Cancel'
     */
    @Override
    public void cancel() {
    }

    public JComponent getComponent(Lookup masterLookup) {
        return getPanel(); // Returns custom panel
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isChanged() {
        return true;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
    }
}
