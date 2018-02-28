/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.test.core;

import com.artigile.warehouse.utils.parser.ParsedTemplate;
import com.artigile.warehouse.utils.parser.ParsedTemplateDataSource;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Tests of template based string calculation.
 *
 * @author Shyrik, 30.08.2009
 */
public class ParsedTemplateTest {
    private class TestFieldsDataSource implements ParsedTemplateDataSource{
        private ArrayList<String> fieldNames;
        private ArrayList<String> fieldValues;

        public TestFieldsDataSource(String []fieldNames, String []fieldValues){
            this.fieldNames = new ArrayList<String>(Arrays.asList(fieldNames));
            this.fieldValues = new ArrayList<String>(Arrays.asList(fieldValues));
        }

        @Override
        public int getFieldIndexByName(String fieldName) {
            return fieldNames.indexOf(fieldName);
        }

        @Override
        public String getFieldValue(int index) {
            return fieldValues.get(index);
        }
    }

    @Test
    public void testSimpleTemplateString(){
        ParsedTemplateDataSource dataSource = new TestFieldsDataSource(new String[]{"field1", "field2", "field3"}, new String[]{"1", "2", "3"});
        checkTemplateValue("{field1}+{field2}={field3}", "1+2=3", dataSource);
        checkTemplateValue("{field1}+2={field3}, 2 = {field2}", "1+2=3, 2 = 2", dataSource);
        checkTemplateValue("{field1}{field2}", "12", dataSource);
        checkTemplateValue("{field1}", "1", dataSource);
        checkTemplateValue("{error_field}", "{" + ParsedTemplate.getErrorMessage() + "}", dataSource);
    }

    @Test
    public void testTemplateStringWithHiddenFields(){
        //Field4 is empty!
        ParsedTemplateDataSource dataSource = new TestFieldsDataSource(new String[]{"field1", "field2", "field3", "field4"}, new String[]{"1", "2", "3", ""});
        checkTemplateValue("[{field1}]", "1", dataSource);
        checkTemplateValue("([{field4}some text])", "()", dataSource);
        checkTemplateValue("[some {field1} text]", "some 1 text", dataSource);
        checkTemplateValue("[{field1} + {field2}{field4} = {field3}]", "1 + 2 = 3", dataSource);
        checkTemplateValue("[{field4}{field1} + {field2} = {field3}]", "", dataSource);
        checkTemplateValue("[{field4}{field1} + ]{field2} = {field3}", "2 = 3", dataSource);
    }

    private void checkTemplateValue(String templateString, String templateValue, ParsedTemplateDataSource dataSource) {
        ParsedTemplate parsedTemplate = new ParsedTemplate();
        parsedTemplate.parse(templateString, dataSource);
        Assert.assertEquals(templateValue, parsedTemplate.calculate(dataSource));
    }
}
