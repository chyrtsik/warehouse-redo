/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.details;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is a helper to map real fields to appropriate getter expressions to evaluate field values.
 * @author Aliaksandr.Chyrtsik, 26.02.11
 */
public class DetailFieldsHeaderMap {
    Map<String, FieldMapping> headerMap = new HashMap<String, FieldMapping>();

    /**
     * Initializes field mapping from given detail batches list.
     * @param fieldsProperty property name to get list of dynamic entity fields.
     * @param fieldsIterator iterator for enumerating all .
     */
    public DetailFieldsHeaderMap(String fieldsProperty, Iterable<List<DetailFieldValueTO>> fieldsIterator) {
        int i = 0;
        String fieldExpressionTemplate = fieldsProperty + "[{0}].{1}";
        for (List<DetailFieldValueTO> fields : fieldsIterator) {
            for (DetailFieldValueTO detailFieldValueTO : fields) {
                String fieldName = detailFieldValueTO.getType().getName();
                if (!headerMap.containsKey(fieldName)) {
                    String expression = MessageFormat.format(fieldExpressionTemplate, i, detailFieldValueTO.getTypifiedValueFieldName());
                    headerMap.put(fieldName, new FieldMapping(expression, i));
                    i++;
                }
            }
        }
    }

    public Set<String> getMappingKeySet() {
        return headerMap.keySet();
    }

    public FieldMapping getMappingByFieldName(String key) {
        return headerMap.get(key);
    }

    public int getMappingSize() {
        return headerMap.size();
    }

    /**
     * Holds field data (to access field value).
     */
    public static class FieldMapping {
        private String expression;
        private int index;

        public FieldMapping(String expression, int index){
            this.expression = expression;
            this.index = index;
        }

        public String getExpression() {
            return expression;
        }

        public int getIndex() {
            return index;
        }
    }
}
