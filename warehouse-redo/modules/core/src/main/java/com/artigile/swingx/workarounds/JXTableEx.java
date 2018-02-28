/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.swingx.workarounds;

import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * @author Borisok V.V., 02.02.2009
 */
public class JXTableEx extends JXTable {

    private EventListenerList listeners = new EventListenerList();

    @Override
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        if (pressed) {
            if (e.getKeyCode() == KeyEvent.VK_C && ( (e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK)) {
                StringBuilder sbf = new StringBuilder();
                int colCount = getSelectedColumnCount();
                int rowCount = getSelectedRowCount();
                int[] selRows = getSelectedRows();
                int[] selCols = getSelectedColumns();
                if (colCount > 0 && rowCount > 0 &&
                    rowCount - 1 == selRows[selRows.length - 1] - selRows[0] && rowCount == selRows.length &&
                    colCount - 1 == selCols[selCols.length - 1] - selCols[0] && colCount == selCols.length) {
                    for (int i = 0; i < rowCount; ++i) {
                       for (int j = 0; j < colCount; ++j) {
                           Object value = getValueAt(selRows[i], selCols[j]);
                           sbf.append(value != null ? value : "");
                          if (j < colCount - 1)
                             sbf.append("\t");
                       }
                       sbf.append("\n");
                    }
                    StringSelection stringSelection = new StringSelection(sbf.toString());
                    Clipboard clipboard = getToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, stringSelection);
                } else {
                    MessageDialogs.showWarning(this, I18nSupport.message("this.command.is.not.applicable.to.unrelated.ranges"));
                }

                e.consume();
                return true;
            }
        }
    	return super.processKeyBinding(ks, e, condition, pressed);
    }

    public void addEnclosingScrollPaneListener(EnclosingScrollPaneListener l) {
        listeners.add(EnclosingScrollPaneListener.class, l);
    }

    public void removeEnclosingScrollPaneListener(EnclosingScrollPaneListener l) {
        listeners.remove(EnclosingScrollPaneListener.class, l);
    }

    @Override
    protected void configureEnclosingScrollPane() {
        super.configureEnclosingScrollPane();
        setTerminateEditOnFocusLost(false);
        fireConfigure();
    }

    @Override
    protected void unconfigureEnclosingScrollPane() {
        fireUnconfigure();
        super.unconfigureEnclosingScrollPane();
    }

    protected void fireConfigure() {
        EnclosingScrollPaneEvent event = new EnclosingScrollPaneEvent(this);
        EnclosingScrollPaneListener[] listenerList = listeners.getListeners(EnclosingScrollPaneListener.class);
        for (EnclosingScrollPaneListener listener : listenerList) {
            listener.configure(event);
        }
    }

    protected void fireUnconfigure() {
        EnclosingScrollPaneEvent event = new EnclosingScrollPaneEvent(this);
        EnclosingScrollPaneListener[] listenerList = listeners.getListeners(EnclosingScrollPaneListener.class);
        for (EnclosingScrollPaneListener listener : listenerList) {
            listener.unconfigure(event);
        }
    }

    @Override
    public void setTerminateEditOnFocusLost(boolean terminate) {
        // always false because if true then don't work properly
        super.setTerminateEditOnFocusLost(false);
    }

    /**
     * workaround for not properly focus work
     */
    @Override
    public Component prepareEditor(TableCellEditor editor, int row, int column) {
        Component comp = super.prepareEditor(editor, row, column);
        comp.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (getCellEditor() != null && !getCellEditor().stopCellEditing()) {
                    getCellEditor().cancelCellEditing();
                }
            }
        });
        return comp;
    }
}