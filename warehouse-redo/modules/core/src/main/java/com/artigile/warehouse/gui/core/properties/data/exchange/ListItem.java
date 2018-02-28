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

/**
 * @author Shyrik, 05.01.2009
 */

/**
 * Holds information about item in the list.
 */
public class ListItem implements Comparable {
    /**
     * Display name of the item.
     */
    private String displayName;

    /**
     * Value of the item, that is used by the application (not by the choose dialog).
     */
    private Object value;

    public ListItem(String displayName, Object value) {
        this.displayName = displayName;
        this.value = value;
    }

    public String toString(){
        return getDisplayName();
    }

    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }
	    else if (obj instanceof ListItem) {
            ListItem itemObj = (ListItem)obj;
	        return getDisplayName().equals(itemObj.getDisplayName());
        }
        else if (getValue() != null){
            return getValue().equals(obj);
        }
        else if (getValue() == null){
            return obj == null;
        }
        return false;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public int compareTo(Object o) {
        ListItem second = (ListItem) o;
        return this.getDisplayName().compareTo(second.getDisplayName());
    }
}
