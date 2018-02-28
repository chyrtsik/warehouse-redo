/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.choose;

import javax.swing.*;
import java.util.List;

/**
 * @author Borisok V.V., 17.01.2009
 */
public interface ChooseList {

    public List getSourceList();

    public List getDestinationList();

    public ListSelectionModel getSourceSelectionModel();

    public ListSelectionModel getDestinationSelectionModel();

    /**
     * @return objects of items selected at source list
     */
    public Object[] getSourceSelectedItems();

    /**
     * @return objects of items selected at destination list
     */
    public Object[] getDestinationSelectedItems();

    /**
     * @return indexes of items selected at source list (order by asc)
     */
    public int[] getSourceSelectedIndexes();

    /**
     * @return indexes of items selected at destination list (order by asc)
     */
    public int[] getDestinationSelectedIndexes();

    /**
     * Decrease position of selected items by one
     */
    public void upward();

    /**
     * Increase position of selected items by one
     */
    public void downward();

    /**
     * Decrease position of selected items to first position
     */
    public void upwardTop();

    /**
     * Increase position of selected items to last position
     */
    public void downwardBottom();

    /**
     * Move selected items from available list to selected list
     */
    public void select();

    /**
     * Move selected items from select list to available list
     */
    public void unselect();

    /**
     * Move all items from available list to selected list
     */
    public void selectAll();

    /**
     * Move all items from select list to available list
     */
    public void unselectAll();

    public void addChooseListener(ChooseListener l);

    public void removeChooseListener(ChooseListener l);
}
