/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

/*
 * @(#) SortInfo.java created on Jan 18, 2008
 */
package com.artigile.warehouse.dao.generic.filter;

import java.io.Serializable;

/**
 * The sorting info holder class.
 *
 * @author ioan
 */
public final class SortingInfo implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Sorting type.
     */
    private SortingOrder type;

    /**
     * Column name for sorting.
     */
    private String column;

    /**
     * Constructor.
     *
     * @param type   the sorting type
     * @param column the column name.
     */
    public SortingInfo(SortingOrder type, String column) {
        this.type = type;
        this.column = column;
    }

    /**
     * Gets the 'type' property value.
     *
     * @return the 'type' property value
     */
    public SortingOrder getType() {
        return type;
    }

    /**
     * Gets the 'column' property value.
     *
     * @return the 'column' property value
     */
    public String getColumn() {
        return column;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        if (type != null && type.getValue() != null) {
            result = prime * result + type.getValue().hashCode();
        }
        if (column != null) {
            result = prime * result + column.hashCode();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final SortingInfo sortingInfo = (SortingInfo) obj;

        return type.equals(sortingInfo.getType())
                && column.equals(sortingInfo.getColumn());
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
		return column + " " + type;
	}



}
