/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.plugin;

import com.artigile.warehouse.bl.userprofile.FrameStateFilter;
import com.artigile.warehouse.bl.userprofile.FrameStateService;
import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.gui.baselayout.ReportComponentListener;
import com.artigile.warehouse.gui.baselayout.ReportTopComponent;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.userprofile.FrameStateTO;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.artigile.warehouse.utils.transofmers.FrameStateTransformer;
import com.artigile.warehouse.utils.transofmers.UserTransformer;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 08.01.2009
 */

/**
 * Plugins implementations, that shows plugin content in the child
 * MDI windows of the main application form.
 */
public abstract class FramePlugin implements Plugin, ReportComponentListener {

    private FramePlugin parentReport;

    private List<ReportComponentListener> listeners = new ArrayList<ReportComponentListener>();

    public FramePlugin(){
    }

    public FramePlugin(FramePlugin parentReport){
        this.parentReport = parentReport;
        if (parentReport != null){
            parentReport.addReportComponentListener(this);
        }
    }

    public FramePlugin getParentReport() {
        return parentReport;
    }

    @Override
    public void run(PluginParams params) {
        ReportTopComponent tc = new ReportTopComponent(getTitle(), getContentPanel());
        tc.setReportComponentListener(this);
        tc.open();
        tc.requestActive();
    }

    @Override
    public void componentOpened() {
        fireComponentOpened();
        onFrameOpened();
    }

    @Override
    public void componentClosed() {
        fireComponentClosed();
        onFrameClosed();
    }

    @Override
    public void componentShowing() {
        fireComponentShowing();
        onFrameShowing();
    }

    @Override
    public void componentHidden() {
        fireComponentHidden();
        onFrameHidden();
    }

    @Override
    public void componentActivated() {
        fireComponentActivated();
    }

    @Override
    public void componentDeactivated() {
        fireComponentDeactivated();
    }

    protected void onFrameShowing() {
    }

    protected void onFrameHidden() {
    }

    protected void onFrameOpened() {
    }

    protected void onFrameClosed() {
    }

    public void addReportComponentListener(ReportComponentListener listener) {
        listeners.add(listener);
    }

    public void removeReportComponentListener(ReportComponentListener listener) {
        listeners.remove(listener);
    }

    private void fireComponentShowing() {
        for (ReportComponentListener listener : listeners) {
            listener.componentShowing();
        }
    }

    private void fireComponentHidden() {
        for (ReportComponentListener listener : listeners) {
            listener.componentHidden();
        }
    }

    private void fireComponentOpened() {
        for (ReportComponentListener listener : listeners) {
            listener.componentOpened();
        }
    }

    private void fireComponentClosed() {
        for (ReportComponentListener listener : listeners) {
            listener.componentClosed();
        }
    }

    private void fireComponentActivated() {
        for (ReportComponentListener listener : listeners) {
            listener.componentActivated();
        }
    }

    private void fireComponentDeactivated() {
        for (ReportComponentListener listener : listeners) {
            listener.componentDeactivated();
        }
    }

    /**
     * Child classes might implement this method to return identifier for extended frame identification
     * @return
     */
    public String getFrameId(){
        return getClass().getCanonicalName();
    }

    /**
     * Util class for store/restore state position of internal frame  
     */
    private static class InternalFrameStateManager {

        private static UserTO getUserTO() {
            return WareHouse.getUserSession().getUser();
        }

        private static User getUser() {
            return UserTransformer.transformUser(getUserTO());
        }

        private static void makeMaximaze(JInternalFrame frame, boolean maximazed) {
            try {
                frame.setMaximum(maximazed);
            } catch (PropertyVetoException e) {
                LoggingFacade.logError(e);
            }
        }

        private static void restoreSettings(JInternalFrame frame, final String frameId) {
            FrameStateFilter filter = new FrameStateFilter(getUser(), frameId);
            FrameStateService service = FrameStateTransformer.getFrameStateService();
            List<FrameStateTO> list = service.getByFilter(filter);
            if(!list.isEmpty()) {
                FrameStateTO fs = list.get(0);
                boolean maximazed = fs.getMaximazed();
                makeMaximaze(frame, maximazed);
                if(!maximazed) {
                    Rectangle r = new Rectangle();
                    r.setFrame(fs.getLeft(), fs.getTop(), fs.getWitdh(), fs.getHeight());
                    frame.setPreferredSize(new Dimension(fs.getWitdh().intValue(), fs.getHeight().intValue()));
                    frame.setBounds(r);
                }
            } else {
                makeMaximaze(frame, true);
            }
        }

        private static void storeSettings(JInternalFrame frame, final String frameId) {
            FrameStateTO fs = new FrameStateTO();
            fs.setUser(getUserTO());
            fs.setFrameId(frameId);
            boolean maximazed = frame.isMaximum();
            fs.setMaximazed(maximazed);
            if(!maximazed) {
                fs.setBounds(frame.getBounds());
            }

            FrameStateService service = FrameStateTransformer.getFrameStateService();
            FrameStateFilter filter = new FrameStateFilter(getUser(), frameId);
            List<FrameStateTO> list = service.getByFilter(filter);
            if(!list.isEmpty()) {
                service.remove(list.get(0));
            }
            service.save(fs);
        }
    }

    //========================== Methods to implement by derived classes ==========================
    /**
     * @return must return title of the report window.
     */
    public abstract String getTitle();

    /**
     * @return must return panel with content of the plugin.
     */
    public abstract JPanel getContentPanel();
}
