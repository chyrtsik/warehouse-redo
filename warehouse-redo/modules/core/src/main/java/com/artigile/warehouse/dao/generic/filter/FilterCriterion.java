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


import java.util.Collection;

/**
 * Filter criterion definition.
 *
 * @author ihar
 * 
 * $Id$
 */
public class FilterCriterion {
	
	/** Criterion name. */
	private String name;

    /** Column name. */
    private String column;

    /** Operation. */
    private Expression operation;

    /** Value. */
    private Object value;

    /**
     * Constructor.
     *
     * @param name name of the criterion
     * @param column the column name
     * @param operation the expression operation
     * @param value the value
     */
    public FilterCriterion(String name, String column, Expression operation,
    		Object value) {
    	this.name = name;
        this.column = column;
        this.operation = operation;
        this.value = value;
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
     * Gets the 'operation' property value.
     *
     * @return the 'operation' property value
     */
    public Expression getOperation() {
        return operation;
    }

    /**
     * Gets the 'value' property value.
     *
     * @return the 'value' property value
     */
    public Object getValue() {
        return value;
    }
    
   /**
	 * Gets a value of 'name' property.
	 *
	 * @return the value of 'name' property
	 */
	public String getName() {
		return name;
	}

	/**
     * Returns a string representation of the object.
     *
     * @return the string representation of the object
     */
    @Override
    public String toString() {
        return String.format("%1$s %2$s :%3$s", 
        			column, operation.getValue(), name);
    }

    /**
     * Returns a String that when added to the query will add
     * appropriate restrictions. Results in <code>objectAlias.COLUMN OPERATION (?, ?, ..., ?)</code>
     * in case of collection value, or <code>objectAlias.COLUMN OPERATION ?</code> otherwise.
     *
     * @param uniqueKey unique key
     * @return part of query that represents restrictions of this filter
     * */
    @SuppressWarnings("unchecked")
	public String buildQuerySegment(String uniqueKey) {
    	if (operation.isCollectionBased()) {
    		StringBuilder buffer = new StringBuilder();
    		
    		int parameterCount = ((Collection) value).size();
    		if (parameterCount > 0) {
    			buffer.append(String.format("%1$s %2$s (", column, operation.getValue()));
    			int counter = 1;
    			while (counter < parameterCount) {
    				buffer.append(String.format(":%1$s, ",
    						getQueryParameterName(uniqueKey, counter)));
    				counter++;
    			}    			
    			buffer.append(String.format(":%1$s )", getQueryParameterName(uniqueKey, counter)));
    		} 
    		
    		return buffer.toString();
    	} else if (operation.isNullCkeck()) {
    		return String.format("(%1$s %2$s)", column, operation.getValue());
    	} else {
    		return String.format("(%1$s %2$s :%3$s)", column, operation.getValue(), 
    										getQueryParameterName(uniqueKey));
    	}
    }
    
	/**
	 * Evaluates query parameter by operation.
	 *
	 * @param value parameter value
	 * @return parameter value
	 * */
	public Object evaluateQueryParam(Object value) {
		Object queryParam;
    	switch (operation) {
    		case CONTAINS:
    			queryParam = "%" + value + "%";
    			break;
    		case STARTS_WITH:
    			queryParam = value + "%";
    			break;
    		case ENDS_WITH:
    			queryParam = "%" + value;
    			break;
    		case EQ:
			case NOT_EQ:
			case GE:
			case GT:
			case LE:
			case LT:
			case LIKE:
			case IN:
			case NOT_IN:
			case IS_NULL:
			case IS_NOT_NULL:
    		default:
    			queryParam = value;
    			break;
    	}
    	return queryParam;
	}
	
	/**
	 * Formats query parameter name for the criterion.
	 * 
	 * @param uniqueKey unique key
	 * @return parameter name
	 * */
	public String getQueryParameterName(String uniqueKey) {
		return String.format("%1$s_%2$s", getName(), uniqueKey);
		
	}
	
	/**
	 * Formats query parameter name for the collection-based (IN, NOT_IN) criterion.
	 * 
	 * @param uniqueKey unique key
	 * @param index index of the element in the collection
	 * @return parameter name
	 * */
	public String getQueryParameterName(String uniqueKey, int index) {
		return String.format("%1$s_%2$s", getQueryParameterName(uniqueKey), index);
	}
}
