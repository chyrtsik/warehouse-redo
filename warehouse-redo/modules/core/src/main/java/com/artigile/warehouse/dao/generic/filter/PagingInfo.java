/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao.generic.filter;

/**
 * Pagination info definition. Contains a page size and zero based index
 * of the first row. Allows to create instance for specific page.
 *
 * @author ihar
 */
public class PagingInfo {

	/** Default number of rows. */
	private static final int DEFAULT_NUMBER_OF_ROWS = 20;

	/** Start record index (zero based). */
	private int startIndex;

	/** Page size. */
	private int numberOfRows;

	/**
	 * Constructor.
	 *
	 * @param startIndex the start row index (zero based)
	 */
	public PagingInfo(int startIndex) {
		this(startIndex, DEFAULT_NUMBER_OF_ROWS);
	}

	/**
	 * Constructor.
	 *
	 * @param startIndex the start row index (zero based)
	 * @param numberOfRows max number of rows to fetch
	 */
	public PagingInfo(int startIndex, int numberOfRows) {
		this.startIndex = startIndex;
		this.numberOfRows = numberOfRows;
	}

	/**
	 * Gets a value of 'startIndex' property.
	 *
	 * @return the value of 'startIndex' property
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * Gets a value of 'numberOfRows' property.
	 *
	 * @return the value of 'numberOfRows' property
	 */
	public int getNumberOfRows() {
		return numberOfRows;
	}

}
