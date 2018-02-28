/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.baselayout;

import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

import javax.swing.*;
import java.awt.*;

/**
 * Top component which displays something.
 */
public final class ReportTopComponent extends TopComponent {

    private ReportComponentListener listener;

    public ReportTopComponent(String title, JPanel contentPanel) {
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        //setName(NbBundle.getMessage(ReportTopComponent.class, "CTL_ReportTopComponent"));
        setName(title);
        setToolTipText(NbBundle.getMessage(ReportTopComponent.class, "HINT_ReportTopComponent"));
    }

    @Override
    protected void componentOpened() {
        if (listener != null) {
            listener.componentOpened();
        }
    }

    @Override
    protected void componentClosed() {
        if (listener != null) {
            listener.componentClosed();
            setReportComponentListener(null);
        }
    }

    @Override
    protected void componentShowing() {
        if (listener != null) {
            listener.componentShowing();
        }
    }

    @Override
    protected void componentHidden() {
        if (listener != null) {
            listener.componentHidden();
        }
    }

    @Override
    protected void componentActivated() {
        if (listener != null) {
            listener.componentActivated();
        }
    }

    @Override
    protected void componentDeactivated() {
        if (listener != null) {
            listener.componentDeactivated();
        }
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    public void setReportComponentListener(ReportComponentListener listener) {
        this.listener = listener;
    }
}
