/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.utils;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Implementation for Double click listener. It allows to override default behavior when user should press
 * exactly the same pixel to fire doubleClick event.
 * This listener recommended to be used with {@link javax.swing.JTable} objects only.
 *
 * @author Aliaksandr.Chyrtsik, 10.07.11
 */

public abstract class TableDoubleClickListener extends MouseAdapter {
    /**
     * Timestamp of the first click.
     */
    private long firstClickTime = -1;

    /**
     * Table row and col clicked first time.
     */
    private int firstSelectedRow = -1;
    private int firstSelectedCol = -1;

    /**
     * Coordinates of the first click.
     */
    private int firstClickX = 0;
    private int firstClickY = 0;

    public abstract void onDoubleClick();

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!(e.getSource() instanceof JTable)){
            //Now only JTable components are supported.
            return;
        }
        JTable table = (JTable)e.getSource();

        //Maximum delay between double clicks.
        final int DOUBLE_CLICK_INTERVAL = 500;
        //Maximum coordinates difference between clicks to consider that clicks the made oder the same element.
        final int MAX_COORDINATES_DIFF = 24;

        if (e.getButton() == MouseEvent.BUTTON1) {
            if (firstClickTime == -1) {
                //First click.
                firstClickTime = e.getWhen();
                firstSelectedRow = table.getSelectedRow();
                firstSelectedCol = table.getSelectedColumn();
                firstClickX = e.getX();
                firstClickY = e.getY();
            }
            else{
                //Second click.
                int selectedRow = table.getSelectedRow();
                int selectedCol = table.getSelectedColumn();
                if (e.getWhen() - firstClickTime < DOUBLE_CLICK_INTERVAL &&
                    selectedRow == firstSelectedRow &&
                    selectedCol == firstSelectedCol &&
                    Math.abs(firstClickX - e.getX()) < MAX_COORDINATES_DIFF &&
                    Math.abs(firstClickY - e.getY()) < MAX_COORDINATES_DIFF)
                {
                    //Double click is performed!.
                    onDoubleClick();
                }
                else{
                    //Second click missed -- it is not a double click. Lets consider this as first click.
                    firstClickTime = e.getWhen();
                    firstSelectedRow = table.getSelectedRow();
                    firstSelectedCol = table.getSelectedColumn();
                    firstClickX = e.getX();
                    firstClickY = e.getY();
                }
            }
        }
        else{
            //Reset first click parameters.
            firstClickTime = -1;
        }
    }
}
