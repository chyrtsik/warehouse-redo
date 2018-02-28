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
 * Expression operation enumeration.
 *
 * @author ihar
 * 
 * $Id$
 */
public enum Expression {

    /** Equals to. */
    EQ("="),

    /** Not equals to. */
    NOT_EQ("!="),

    /** Less than. */
    LT("<"),

    /** Less than or equals. */
    LE("<="),

    /** Greater than. */
    GT(">"),

    /** Greater than or equals. */
    GE(">="),

    /** In set of values. */
    IN("IN"),

    /** Not in set of values. */
    NOT_IN("NOT IN"),

    /** Like. */
    LIKE("LIKE"),

    /** Like with following format '%value%'. */
    CONTAINS("LIKE"),

    /** Like with following format 'value%'. */
    STARTS_WITH("LIKE"),

    /** Like with following format '%value'. */
    ENDS_WITH("LIKE"),
    
    /** Is null. */
    IS_NULL("IS NULL"),
    
    /** Is not null. */
    IS_NOT_NULL("IS NOT NULL");
    

    /** Operation sign. */
    private String value;

    /**
     * Constructor.
     *
     * @param value the operation value
     */
    Expression(String value) {
        this.value = value;
    }

    /**
     * Gets the 'value' property value.
     *
     * @return the 'value' property value
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns {@code true} if current expression is collection based.
     *
     * @return {@code true} if current expression is collection based
     */
    public boolean isCollectionBased() {
    	switch (this) {
    		case IN:
    		case NOT_IN:
    			return true;
    		case EQ:
    		case NOT_EQ:
    		case LT:
    		case LE:
    		case GT:
    		case GE:
    		case LIKE:
    		case CONTAINS:
    		case STARTS_WITH:
    		case ENDS_WITH:
    		case IS_NULL:
    		case IS_NOT_NULL:
    		default:
    			return false;
    	}
    }
    
    /**
     * Returns {@code true} if current expression is check for null.
     *
     * @return {@code true} if current expression is is check for null
     */
    public boolean isNullCkeck() {
    	switch (this) {
    		case IS_NULL:
    		case IS_NOT_NULL:
    			return true;
    		case EQ:
    		case NOT_EQ:
    		case LT:
    		case LE:
    		case GT:
    		case GE:
    		case LIKE:
    		case CONTAINS:
    		case STARTS_WITH:
    		case ENDS_WITH:
    		case IN:
    		case NOT_IN:
    		default:
    			return false;
    	}
    }
}
