/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs;

import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.properties.dialogs.bugreport.BugDlg;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import javax.swing.*;
import java.awt.*;

/**
 * @author Shyrik, 08.01.2009
 */

/**
 * Utility class for showing message boxes.
 */
public final class MessageDialogs {

    private MessageDialogs() {
    }

    //=============================== Warning messages =========================================
    public static void showWarning(Component ownerComponent, String message) {
        JOptionPane.showMessageDialog(getComponentWindow(ownerComponent), message, I18nSupport.message("warning"), JOptionPane.WARNING_MESSAGE);
    }

    public static void showWarning(String message) {
        showWarning(WareHouse.getMainFrame(), message);
    }

    //=============================== Error messages ==========================================
    public static void showError(Component ownerComponent, String message) {
        JOptionPane.showMessageDialog(getComponentWindow(ownerComponent), message, I18nSupport.message("error"), JOptionPane.ERROR_MESSAGE);
    }

    public static void showError(Component ownerComponent, Throwable e) {
        LoggingFacade.logError(e);
        new BugDlg(e).run();
    }

    public static void showError(Throwable e) {
        showError(WareHouse.getMainFrame(), e);
    }

    public static void showError(String message) {
        showError(WareHouse.getMainFrame(), message);
    }

    //===================================== Information messages ============================================
    public static void showInfo(Component ownerComponent, String message) {
        JOptionPane.showMessageDialog(getComponentWindow(ownerComponent), message, I18nSupport.message("information"), JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showInfo(String message) {
        JOptionPane.showMessageDialog(WareHouse.getMainFrame(), message, I18nSupport.message("information"), JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showMessage(Component ownerComponent, String title, String message) {
        JOptionPane.showMessageDialog(ownerComponent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showMessage(String title, String message) {
        showMessage(WareHouse.getMainFrame(), title, message);
    }

    //============================= Confirmation messages ==================================
    public static boolean showConfirm(String title, String message) {
        return showConfirm(WareHouse.getMainFrame(), title, message);
    }

    public static boolean showConfirm(Component ownerComponent, String title, String message) {
        return JOptionPane.showConfirmDialog(getComponentWindow(ownerComponent), message, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }


    //============================= Utils =================================================
    /**
     * Gets window, to which this component belongs to.
     * @param component
     * @return
     */
    private static Component getComponentWindow(Component component) {
        Component currentComponent = component;
        while ((currentComponent.getParent() != null) && !(currentComponent instanceof Dialog)){
            currentComponent  = currentComponent.getParent();
        }
        return currentComponent;
    }
}
