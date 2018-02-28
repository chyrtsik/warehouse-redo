/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.priceimport;

import com.artigile.warehouse.utils.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public abstract class ContractorProductSourceDataParser {

    private static final String KEY_VALUE_SEPARATOR = "?";
    private static final String PAIRS_SEPARATOR = "||";
    
    private static StringBuilder strBuilder = new StringBuilder();


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    public static synchronized String format(Map<String, String> sourceData) {
        if (!sourceData.isEmpty()) {
            clearStrBuilder();
            for (Map.Entry<String, String> pair : sourceData.entrySet()) {
                strBuilder.append(pair.getKey()).append(KEY_VALUE_SEPARATOR).append(pair.getValue()).append(PAIRS_SEPARATOR);
            }
            return strBuilder.substring(0, strBuilder.length() - 2);
        }
        return null;
    }
    
    public static Map<String, String> parse(String sourceData) {
        Map<String, String> sourceDataMap = new LinkedHashMap<String, String>();
        if (StringUtils.containsSymbols(sourceData)) {
            StringTokenizer pairsTokenizer = new StringTokenizer(sourceData, PAIRS_SEPARATOR);
            while (pairsTokenizer.hasMoreTokens()) {
                String pair = pairsTokenizer.nextToken();
                int keyValueSeparatorIndex = pair.indexOf(KEY_VALUE_SEPARATOR);
                sourceDataMap.put(pair.substring(0, keyValueSeparatorIndex),
                        pair.substring(keyValueSeparatorIndex + KEY_VALUE_SEPARATOR.length(), pair.length()));
            }
        }
        return sourceDataMap;
    }


    /* Util methods
    ------------------------------------------------------------------------------------------------------------------*/
    private static void clearStrBuilder() {
        if (strBuilder.length() > 0) {
            strBuilder.delete(0, strBuilder.length());
        }
    }
}
