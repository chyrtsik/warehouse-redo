/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.style;

import javax.swing.*;

/**
 * Wrapper for Swing alignment.
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public enum Alignment {

    LEFT(SwingConstants.LEFT),

    // Center - for horizontal orientation
    // Middle - for vertical orientation
    CENTER(SwingConstants.CENTER),

    RIGHT(SwingConstants.RIGHT),

    TOP(SwingConstants.TOP),

    BOTTOM(SwingConstants.BOTTOM);


    /**
     * Swing constant, defining alignment type
     */
    private int swingAlign;

    
    Alignment(int swingAlign) {
        this.swingAlign = swingAlign;
    }
    

    /* Getters
    ------------------------------------------------------------------------------------------------------------------*/
    public int getAlignmentType() {
        return swingAlign;
    }
}
