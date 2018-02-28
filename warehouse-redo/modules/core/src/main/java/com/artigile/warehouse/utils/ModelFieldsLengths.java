/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils;

/**
 * @author: Vadim.Zverugo, 04.10.2010
 */

/**
 * List of fixed values
 */
public final class ModelFieldsLengths {
    private ModelFieldsLengths() {}

    /**
     * Default max length of text in the text fields.
     */
    public static final int DEFAULT_MAX_TEXT_LENGTH = 255;

    public static final int DEFAULT_BIG_TEXT_LENGTH = 10000;

    /**
     * Max lengths of parts in double variables.
     */
    public static final int MAX_LENGTH_DOUBLE_PRECISION = 14;
    public static final int MAX_LENGTH_DOUBLE_SCALE = 4;

    /**
     * Length of text in the field with value of year
     */
    public static final int MAX_TEXT_LENGTH_YEAR = 4;

    /**
     * Length of text in the fields which presents variables of long type 
     */
    public static final int MAX_TEXT_LENGTH_LONG_VAR = 18;

    /**
     * Max sported length of bar code.
     */
    public static final int MAX_BAR_CODE_LENGTH = 64;

    /**
     * Max length of text in the field of detail batch nomenclature article.
     */
    public static final int MAX_TEXT_LENGTH_NOMENCLATURE_ARTICLE = 100;

    //========================== Administration  fields =====================================
    public static final int USER_LOGIN_MAX_LENGTH = 16;

    public static final int UID_LENGTH = 36;
}
