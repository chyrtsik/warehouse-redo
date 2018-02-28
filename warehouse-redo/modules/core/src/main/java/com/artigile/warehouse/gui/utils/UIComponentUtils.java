/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.utils;

import com.artigile.core.ui.notifications.BalloonManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * @author Shyrik, 02.03.2009
 */

/**
 * Miscellaneous utils, connected with UI components.
 */
public final class UIComponentUtils {

    private UIComponentUtils(){
    }


    /**
     * Makes given text field selecting all text when it obtain focus.
     * @param field
     */
    public static void makeSelectingAllTextOnFocus(JTextField field){
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                ((JTextField)e.getComponent()).selectAll();
            }
        });
    }

    /**
     * Shows comment for given component.
     * @param component
     * @param message
     */
    public static void showCommentOnComponent(JComponent component, String message) {
        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };
        BalloonManager.show(component, new JLabel(message), action, null, 5000);
    }

    /**
     * Travels to the root of nested components and performs pack action, if
     * root parent is dialog window.
     * @param component
     */
    public static void packDialog(Component component){
        Dialog dialog = getDialog(component);
        if (dialog != null){
            dialog.pack();
        }
    }

    /**
     * Gets dialog of given component of null, if panel is not in dialog.
     * @param component
     * @return
     */
    public static Dialog getDialog(Component component) {
        Container parent = component.getParent();
        while (parent != null) {
            if (parent instanceof JDialog) {
                return (Dialog) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }
}
