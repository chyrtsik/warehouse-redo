/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.test.bl.priceimport;

import com.artigile.warehouse.bl.priceimport.ContractorProductSourceDataParser;
import junit.framework.TestCase;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class ContractorProductSourceDataParserTest extends TestCase {
    
    public void testParser() {
        Map<String, String> testMap = new LinkedHashMap<String, String>();
        testMap.put("0:NAME", "KT-33");
        testMap.put("1:QUANTITY", "40");
        testMap.put("2:WHOLESALE_PRICE", "50");
        testMap.put("3:RETAIL_PRICE", "50");
        testMap.put("4:NOT_DEFINED", "any");
        testMap.put("5:NOT_DEFINED", "hello world#@%!!!)@(#????#@##@41`");
        String testResult = "0:NAME?KT-33||1:QUANTITY?40||2:WHOLESALE_PRICE?50||3:RETAIL_PRICE?50||4:NOT_DEFINED?any||" +
                "5:NOT_DEFINED?hello world#@%!!!)@(#????#@##@41`";

        assertEquals(testResult, ContractorProductSourceDataParser.format(testMap));
        assertEquals(testMap, ContractorProductSourceDataParser.parse(testResult));
    }
}
