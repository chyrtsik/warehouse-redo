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

import com.intellij.uiDesigner.core.GridConstraints;

/**
 * @author Shyrik, 09.01.2009
 */

/**
 * Utility class for common grid layout manipulations.
 */
public final class GridLayoutUtils {
    private GridLayoutUtils() {
    }

    /**
     * @return constraints of grid layout cell, which content fills the cell
     * both horizontally and vertically (usually used for tables).
     */
    public static GridConstraints getGrowingAndFillingCellConstraints() {
        GridConstraints constraints = new GridConstraints();
        constraints.setAnchor(GridConstraints.ANCHOR_CENTER);
        constraints.setFill(GridConstraints.FILL_BOTH);
        constraints.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_GROW);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_CAN_GROW);
        return constraints;
    }

    /**
     * @return constraints of grid layout cell, which content fills the cell
     * both horizontally and vertically and can be resized both for growing and shrinking.
     */
    public static GridConstraints getResizableAndFillingCellConstraints() {
        GridConstraints constraints = new GridConstraints();
        constraints.setAnchor(GridConstraints.ANCHOR_CENTER);
        constraints.setFill(GridConstraints.FILL_BOTH);
        constraints.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_CAN_SHRINK);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_CAN_SHRINK);
        return constraints;
    }

    /**
     * @return constraints to make component centered in it's parent.
     */
    public static GridConstraints getCenteredCellConstraints() {
        GridConstraints constraints = new GridConstraints();
        constraints.setAnchor(GridConstraints.ANCHOR_CENTER);
        constraints.setVSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        return constraints;
    }
}
