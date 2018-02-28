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
import javax.swing.event.EventListenerList;
import java.util.List;

/**
 * @author Borisok V.V., 17.01.2009
 */
@SuppressWarnings("unchecked")
public abstract class AbstractChooseList implements ChooseList {

    private EventListenerList listeners = new EventListenerList();

    @Override
    public Object[] getSourceSelectedItems() {
        return getSelectedValues(getSourceList(), getSourceSelectionModel());
    }

    @Override
    public Object[] getDestinationSelectedItems() {
        return getSelectedValues(getDestinationList(), getDestinationSelectionModel());
    }

    @Override
    public int[] getSourceSelectedIndexes() {
        return getSelectedIndices(getSourceSelectionModel());
    }

    @Override
    public int[] getDestinationSelectedIndexes() {
        return getSelectedIndices(getDestinationSelectionModel());
    }

    @Override
    public void upward() {
        int[] selected = getDestinationSelectedIndexes();
        if (selected.length > 0) {
            moveItems(getDestinationList(), getDestinationSelectionModel(), selected, -1);
            fireDestinationOrderChanged();
        }
    }

    @Override
    public void downward() {
        int[] selected = getDestinationSelectedIndexes();
        if (selected.length > 0) {
            moveItems(getDestinationList(), getDestinationSelectionModel(), selected, +1);
            fireDestinationOrderChanged();
        }
    }

    @Override
    public void upwardTop() {
        int[] selected = getDestinationSelectedIndexes();
        if (selected.length > 0) {
            moveItems(getDestinationList(), getDestinationSelectionModel(), selected, -selected[0]);
            fireDestinationOrderChanged();
        }
    }

    @Override
    public void downwardBottom() {
        int[] selected = getDestinationSelectedIndexes();
        if (selected.length > 0) {
            List list = getDestinationList();
            moveItems(list, getDestinationSelectionModel(), selected, list.size() - selected[selected.length - 1] - 1);
            fireDestinationOrderChanged();
        }
    }

    @Override
    public void select() {
        moveItems(getSourceList(), getDestinationList(), getSourceSelectedItems());
        fireChanged();
    }

    @Override
    public void unselect() {
        moveItems(getDestinationList(), getSourceList(), getDestinationSelectedItems());
        fireChanged();
    }

    @Override
    public void selectAll() {
        moveAll(getSourceList(), getDestinationList());
        fireChanged();
    }

    @Override
    public void unselectAll() {
        moveAll(getDestinationList(), getSourceList());
        fireChanged();
    }

    @Override
    public void addChooseListener(ChooseListener l) {
        listeners.add(ChooseListener.class, l);
    }

    @Override
    public void removeChooseListener(ChooseListener l) {
        listeners.remove(ChooseListener.class, l);
    }

    protected void fireChanged() {
        ChooseEvent event = new ChooseEvent(this);
        ChooseListener[] listenerList = listeners.getListeners(ChooseListener.class);
        for (ChooseListener listener : listenerList) {
            listener.sourceChanged(event);
            listener.destinationChanged(event);
        }
    }

    protected void fireSourceChanged() {
        ChooseEvent event = new ChooseEvent(this);
        ChooseListener[] listenerList = listeners.getListeners(ChooseListener.class);
        for (ChooseListener listener : listenerList) {
            listener.sourceChanged(event);
        }
    }

    protected void fireDestinationChanged() {
        ChooseEvent event = new ChooseEvent(this);
        ChooseListener[] listenerList = listeners.getListeners(ChooseListener.class);
        for (ChooseListener listener : listenerList) {
            listener.destinationChanged(event);
        }
    }

    protected void fireSelectionOrderChanged() {
        ChooseEvent event = new ChooseEvent(this);
        ChooseListener[] listenerList = listeners.getListeners(ChooseListener.class);
        for (ChooseListener listener : listenerList) {
            listener.sourceOrderChanged(event);
        }
    }

    protected void fireDestinationOrderChanged() {
        ChooseEvent event = new ChooseEvent(this);
        ChooseListener[] listenerList = listeners.getListeners(ChooseListener.class);
        for (ChooseListener listener : listenerList) {
            listener.destinationOrderChanged(event);
        }
    }

    protected void moveAll(List srcList, List dstList) {
        dstList.addAll(srcList);
        srcList.clear();
    }

    protected void moveItems(List srcList, List dstList, Object[] selected) {
        for (Object obj : selected) {
            srcList.remove(obj);
            dstList.add(obj);
        }
    }

    /**
     * Move item from source position to destination position
     * with shift items between them
     */
    protected void moveItem(List list, int srcIndex, int dstIndex) {
        if (srcIndex < dstIndex) {
            Object obj = list.get(srcIndex);
            for (int i = srcIndex + 1; i <= dstIndex; ++i) {
                list.set(i-1, list.get(i));
            }
            list.set(dstIndex, obj);
        } else {
            Object obj = list.get(srcIndex);
            for (int i = srcIndex - 1; i >= dstIndex; --i) {
                list.set(i+1, list.get(i));
            }
            list.set(dstIndex, obj);
        }
    }

    protected void moveItem(int index, List list, ListSelectionModel listSelectionModel, int[] selected, int delta) {
        int srcIndex = selected[index];
        int dstIndex = srcIndex + delta;
        moveItem(list, srcIndex, dstIndex);
        listSelectionModel.removeSelectionInterval(srcIndex, srcIndex);
        listSelectionModel.addSelectionInterval(dstIndex, dstIndex);
    }

    /**
     * Move selected items on delta positions at list
     */
    protected void moveItems(List list, ListSelectionModel listSelectionModel, int[] selected, int delta) {
        int minIndex = selected[0];
        int maxIndex = selected[selected.length - 1];
        if (delta < 0 && minIndex + delta >= 0) {
            for (int i = 0; i < selected.length; ++i) {
                moveItem(i, list, listSelectionModel, selected, delta);
            }
        } else if (delta > 0 && maxIndex + delta < list.size()) {
            for (int i = selected.length - 1; 0 <= i; --i) {
                moveItem(i, list, listSelectionModel, selected, delta);
            }
        }
    }

    /**
     * Returns an array of all of the selected indices, in increasing
     * order.
     *
     * @return all of the selected indices, in increasing order,
     *         or an empty array if nothing is selected
     */
    protected int[] getSelectedIndices(ListSelectionModel sm) {
        int iMin = sm.getMinSelectionIndex();
        int iMax = sm.getMaxSelectionIndex();

        if ((iMin < 0) || (iMax < 0)) {
            return new int[0];
        }

        int[] rvTmp = new int[1 + (iMax - iMin)];
        int n = 0;
        for (int i = iMin; i <= iMax; ++i) {
            if (sm.isSelectedIndex(i)) {
                rvTmp[n++] = i;
            }
        }
        int[] rv = new int[n];
        System.arraycopy(rvTmp, 0, rv, 0, n);
        return rv;
    }

    /**
     * Returns an array of all the selected values, in increasing order based
     * on their indices in the list.
     *
     * @return the selected values, or an empty array if nothing is selected
     */
    protected Object[] getSelectedValues(List list, ListSelectionModel sm) {
        int iMin = sm.getMinSelectionIndex();
        int iMax = sm.getMaxSelectionIndex();

        if ((iMin < 0) || (iMax < 0)) {
            return new Object[0];
        }

        Object[] rvTmp = new Object[1 + (iMax - iMin)];
        int n = 0;
        for (int i = iMin; i <= iMax; ++i) {
            if (sm.isSelectedIndex(i)) {
                rvTmp[n++] = list.get(i);
            }
        }
        Object[] rv = new Object[n];
        System.arraycopy(rvTmp, 0, rv, 0, n);
        return rv;
    }
}
