/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.data.exchange;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 07.01.2009
 */

/**
 *  Utility class for exchanging data with UI components.
 */
public final class DataExchange {
    private DataExchange(){}

    /**
     * Selects given item in the comboBox.
     * @param comboBox
     * @param item
     */
    public static void selectComboItem(JComboBox comboBox, Object item) {
        for (int i=0; i<comboBox.getItemCount(); i++){
            if (comboBox.getItemAt(i).equals(item)){
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        comboBox.setSelectedIndex(-1);
    }

    /**
     * Selects given item in the comboBox but leaves combo selection unchanged, if item is null.
     * @param comboBox
     * @param item
     */
    public static void selectComboItemLeaveDefault(JComboBox comboBox, Object item) {
        if ( item != null ){
            selectComboItem(comboBox, item);            
        }
    }

    /**
     * Returns selected item in the combo box. Processes both combo boxes, that
     * user ListItem and usial combo boxes. 
     * @param comboBox
     * @return
     */
    public static Object getComboSelection(JComboBox comboBox){
        Object selItem = comboBox.getSelectedItem();
        if (selItem instanceof ListItem){
            //Getting custom value of the list item.
            ListItem item = (ListItem)selItem;
            return item.getValue();
        }
        else{
            return selItem;
        }
    }

    /**
     * Fills given list box with given values.
     * @param list
     * @param listItems
     */
    public static void setListItems(JList list, List<ListItem> listItems) {
        list.setListData(listItems == null ? new Object[]{} : listItems.toArray());
    }

    /**
     * Gets list of all items of the list box.
     * @param list
     * @return
     */
    public static List<ListItem> getListItems(JList list) {
        List<ListItem> items = new ArrayList<ListItem>();
        ListModel listModel = list.getModel();
        for (int i = 0; i < listModel.getSize(); i++) {
            items.add((ListItem) listModel.getElementAt(i));
        }
        return items;
    }

    /**
     * Gets list of data of all items of the list box. Combo box should consist of elements of type ListItem.
     * @param list
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getListItemsValues(JList list) {
        List<T> itemsValues = new ArrayList<T>();
        ListModel listModel = list.getModel();
        for (int i = 0; i < listModel.getSize(); i++) {
            ListItem listItem = (ListItem) listModel.getElementAt(i);
            itemsValues.add((T)listItem.getValue());
        }
        return itemsValues;
    }
}
