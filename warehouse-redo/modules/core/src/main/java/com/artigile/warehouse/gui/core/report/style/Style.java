/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.style;

import com.artigile.warehouse.utils.MiscUtils;

import java.awt.*;

/**
 * Holder of style information, used for displaying cells in the table.
 *
 * @author Shyrik, 20.05.2009
 */

public class Style {

    /**
     * Background color of the cell
     */
    private Color background;

    /**
     * Foreground color of the cell
     */
    private Color foreground;

    /**
     *  Horizontal content alignment in the cell
     */
    private Alignment horizontalContentAlign;

    /**
     * Vertical content alignment in the cell
     */
    private Alignment verticalContentAlign;


    //=================================== Constructors =====================================
    public Style() {}

    public Style(Color background) {
        this(background, null);
    }

    public Style(Color background, Color foreground) {
        this.background = background;
        this.foreground = foreground;
    }

    public Style(Alignment horizontalContentAlign, Alignment verticalContentAlign) {
        this.horizontalContentAlign = horizontalContentAlign;
        this.verticalContentAlign = verticalContentAlign;
    }

    public Style(Color background, Alignment horizontalContentAlign, Alignment verticalContentAlign) {
        this.background = background;
        this.horizontalContentAlign = horizontalContentAlign;
        this.verticalContentAlign = verticalContentAlign;
    }

    public Style(Color background, Color foreground, Alignment horizontalContentAlign, Alignment verticalContentAlign) {
        this.background = background;
        this.foreground = foreground;
        this.horizontalContentAlign = horizontalContentAlign;
        this.verticalContentAlign = verticalContentAlign;
    }


    //================================ Getters and setters =================================
    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public Alignment getHorizontalContentAlign() {
        return horizontalContentAlign;
    }

    public void setHorizontalContentAlign(Alignment horizontalContentAlign) {
        this.horizontalContentAlign = horizontalContentAlign;
    }

    public Alignment getVerticalContentAlign() {
        return verticalContentAlign;
    }

    public void setVerticalContentAlign(Alignment verticalContentAlign) {
        this.verticalContentAlign = verticalContentAlign;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Style)){
            return false;
        }
        Style objStyle = (Style)obj;
        return MiscUtils.objectsEquals(background, objStyle.background) &&
               MiscUtils.objectsEquals(foreground, objStyle.foreground) &&
               MiscUtils.objectsEquals(horizontalContentAlign, objStyle.horizontalContentAlign) &&
               MiscUtils.objectsEquals(verticalContentAlign, objStyle.verticalContentAlign);
    }

    @Override
    public int hashCode() {
        return MiscUtils.objectHashCode(background) ^
               MiscUtils.objectHashCode(foreground) ^
               MiscUtils.objectHashCode(horizontalContentAlign) ^
               MiscUtils.objectHashCode(verticalContentAlign);
    }
}
