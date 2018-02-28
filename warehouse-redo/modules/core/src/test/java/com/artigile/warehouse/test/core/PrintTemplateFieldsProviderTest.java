/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.test.core;

import com.artigile.warehouse.domain.printing.PrintTemplateFieldMapping;
import com.artigile.warehouse.gui.core.print.PrintTemplateFieldsProvider;
import com.artigile.warehouse.utils.reflect.fields.dynamic.DynamicObjectFieldsProvider;
import com.artigile.warehouse.utils.reflect.fields.dynamic.DynamicObjectFieldsProviderFactory;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Aliaksandr.Chyrtsik, 15.12.12
 */
public class PrintTemplateFieldsProviderTest {
    private PrintTemplateFieldsProvider provider;
    private Map<String, Integer> reportFields;
    
    @Before
    public void initialize(){
        //Register fields provider to be able to test dynamic fields.
        DynamicObjectFieldsProviderFactory.getInstance().registerProvider("testProvider", new DynamicObjectFieldsProvider() {
            @Override
            public List<String> getAvailableFields() {
                return PrintableObject.getDynamicFieldNames();
            }

            @Override
            public Class getFieldValueClass(String dynamicField) {
                return PrintableObject.getDynamicFieldValueType(dynamicField);
            }

            @Override
            public Object getFieldValue(Object object, String dynamicField) {
                if (object instanceof PrintableObject) {
                    PrintableObject printableObject = (PrintableObject) object;
                    if (printableObject.getDynamicFields() != null) {
                        return printableObject.getDynamicFields().get(dynamicField);
                    }
                }
                return null;
            }

            @Override
            public void setFieldValue(Object object, String dynamicField, Object value) {
                if (object instanceof PrintableObject) {
                    PrintableObject printableObject = (PrintableObject) object;
                    if (printableObject.getDynamicFields() != null) {
                        printableObject.getDynamicFields().put(dynamicField, value);
                    }
                }
            }
        });
    }

    @Test
    public void testPrintTemplateFieldsProvider(){
        PrintableObject printableObject = new PrintableObject();
        provider = new PrintTemplateFieldsProvider(printableObject, getFieldMapping());

        reportFields = new HashMap<String, Integer>();
        List<String> fields = provider.getReportFields();
        for (int i = 0; i < fields.size(); i++){
            reportFields.put(fields.get(i), i);
        }

        //Verify all expected variants (add more variants when need to extent printing or object substitution facilities).

        //1. Simple fields.
        Assert.assertEquals(null,                   printValue("root -> null field", 0));
        Assert.assertEquals("field",                printValue("root -> field", 0));
        Assert.assertEquals(null,                   printValue("root -> null child -> field", 0));
        Assert.assertEquals(null,                   printValue("root -> child -> null field", 0));
        Assert.assertEquals("field",                printValue("root -> child -> field", 0));

        //2. Items.
        Assert.assertEquals(null,                   printValue("root -> null items [] -> field", 0));
        Assert.assertEquals(null,                   printValue("root -> items[] -> null field", 0));
        Assert.assertEquals("field",                printValue("root -> items[] -> field", 0));

        Assert.assertEquals(null,                   printValue("root -> null child -> items[] -> field", 0));
        Assert.assertEquals(null,                   printValue("root -> child -> items[] -> null field", 0));
        Assert.assertEquals("field",                printValue("root -> child -> items[] -> field", 0));

        Assert.assertEquals(null,                   printValue("root -> null items [] -> child -> field", 0));
        Assert.assertEquals(null,                   printValue("root -> items[] -> null child -> field", 0));
        Assert.assertEquals(null,                   printValue("root -> items[] -> child -> null field", 0));
        Assert.assertEquals("field",                printValue("root -> items[] -> child -> field", 0));

        //3. Dynamic fields.
        Assert.assertEquals(null,                   printValue("root -> {Invalid field name}", 0));
        Assert.assertEquals(null,                   printValue("root -> [Invalid field name]", 0));
        Assert.assertEquals("dynamicField",         printValue("root -> {Valid field name}", 0));
        Assert.assertEquals("dynamicField",         printValue("root -> [Valid field name]", 0));

        Assert.assertEquals(100,                    printValue("root -> {Int. field}", 0));
        Assert.assertEquals(100,                    printValue("root -> [Int. field]", 0));
        Assert.assertEquals(1000L,                  printValue("root -> {Long: field}", 0));
        Assert.assertEquals(1000L,                  printValue("root -> [Long: field]", 0));
        Assert.assertEquals(BigDecimal.ONE,         printValue("root -> {Big Decimal field}", 0));
        Assert.assertEquals(BigDecimal.ONE,         printValue("root -> [Big Decimal field]", 0));

        Assert.assertEquals(null,                   printValue("root -> null child -> {Valid field name}", 0));
        Assert.assertEquals(null,                   printValue("root -> null child -> [Valid field name]", 0));
        Assert.assertEquals(null,                   printValue("root -> child -> {Invalid field name}", 0));
        Assert.assertEquals(null,                   printValue("root -> child -> [Invalid field name]", 0));
        Assert.assertEquals("dynamicField",         printValue("root -> child -> {Valid field name}", 0));
        Assert.assertEquals("dynamicField",         printValue("root -> child -> [Valid field name]", 0));

        //4. Dynamic fields of items.
        Assert.assertEquals(null,                   printValue("root -> null items [] -> {Valid field name}", 0));
        Assert.assertEquals(null,                   printValue("root -> null items [] -> [Valid field name]", 0));
        Assert.assertEquals(null,                   printValue("root -> items[] -> {Invalid field name}", 0));
        Assert.assertEquals(null,                   printValue("root -> items[] -> [Invalid field name]", 0));
        Assert.assertEquals("dynamicField",         printValue("root -> items[] -> {Valid field name}", 0));
        Assert.assertEquals("dynamicField",         printValue("root -> items[] -> [Valid field name]", 0));

        Assert.assertEquals(null,                   printValue("root -> null child -> items[] -> {Valid field name}", 0));
        Assert.assertEquals(null,                   printValue("root -> null child -> items[] -> [Valid field name]", 0));
        Assert.assertEquals(null,                   printValue("root -> child -> items[] -> {Invalid field name}", 0));
        Assert.assertEquals(null,                   printValue("root -> child -> items[] -> [Invalid field name]", 0));
        Assert.assertEquals("dynamicField",         printValue("root -> child -> items[] -> {Valid field name}", 0));
        Assert.assertEquals("dynamicField",         printValue("root -> child -> items[] -> [Valid field name]", 0));

        Assert.assertEquals(null,                   printValue("root -> null items [] -> child -> {Valid field name}", 0));
        Assert.assertEquals(null,                   printValue("root -> null items [] -> child -> [Valid field name]", 0));
        Assert.assertEquals(null,                   printValue("root -> items[] -> null child -> {Valid field name}", 0));
        Assert.assertEquals(null,                   printValue("root -> items[] -> null child -> [Valid field name]", 0));
        Assert.assertEquals(null,                   printValue("root -> items[] -> child -> {Invalid field name}", 0));
        Assert.assertEquals(null,                   printValue("root -> items[] -> child -> [Invalid field name]", 0));
        Assert.assertEquals("dynamicField",         printValue("root -> items[] -> child -> {Valid field name}", 0));
        Assert.assertEquals("dynamicField",         printValue("root -> items[] -> child -> [Valid field name]", 0));
    }

    private Object printValue(String userFriendlyFieldName, int itemIndex) {
        Integer fieldIndex = reportFields.get(userFriendlyFieldName);
        return fieldIndex == null ? null : provider.getFieldValue(fieldIndex, itemIndex);
    }


    public static List<PrintTemplateFieldMapping> getFieldMapping() {
        List<PrintTemplateFieldMapping> fieldMapping = new ArrayList<PrintTemplateFieldMapping>();

        fieldMapping.add(new PrintTemplateFieldMapping("nullField", "root -> null field"));
        fieldMapping.add(new PrintTemplateFieldMapping("field", "root -> field"));
        fieldMapping.add(new PrintTemplateFieldMapping("nullChild.field", "root -> null child -> field"));
        fieldMapping.add(new PrintTemplateFieldMapping("child.nullField", "root -> child -> null field"));
        fieldMapping.add(new PrintTemplateFieldMapping("child.field", "root -> child -> field"));

        fieldMapping.add(new PrintTemplateFieldMapping("nullItems[].field", "root -> null items [] -> field"));
        fieldMapping.add(new PrintTemplateFieldMapping("items[].nullField", "root -> items[] -> null field"));
        fieldMapping.add(new PrintTemplateFieldMapping("items[].field", "root -> items[] -> field"));

        fieldMapping.add(new PrintTemplateFieldMapping("nullChild.items[].field", "root -> null child -> items[] -> field"));
        fieldMapping.add(new PrintTemplateFieldMapping("child.items[].nullField", "root -> child -> items[] -> null field"));
        fieldMapping.add(new PrintTemplateFieldMapping("child.items[].field", "root -> child -> items[] -> field"));

        fieldMapping.add(new PrintTemplateFieldMapping("nullItems[].child.field", "root -> null items [] -> child -> field"));
        fieldMapping.add(new PrintTemplateFieldMapping("items[].nullChild.field", "root -> items[] -> null child -> field"));
        fieldMapping.add(new PrintTemplateFieldMapping("items[].child.nullField", "root -> items[] -> child -> null field"));
        fieldMapping.add(new PrintTemplateFieldMapping("items[].child.field", "root -> items[] -> child -> field"));

        fieldMapping.add(new PrintTemplateFieldMapping("{testProvider:*}", "root -> {}"));

        fieldMapping.add(new PrintTemplateFieldMapping("nullChild.{testProvider:*}", "root -> null child -> {}"));
        fieldMapping.add(new PrintTemplateFieldMapping("child.{testProvider:*}", "root -> child -> {}"));

        fieldMapping.add(new PrintTemplateFieldMapping("nullItems[].{testProvider:*}", "root -> null items [] -> {}"));
        fieldMapping.add(new PrintTemplateFieldMapping("items[].{testProvider:*}", "root -> items[] -> {}"));

        fieldMapping.add(new PrintTemplateFieldMapping("nullChild.items[].{testProvider:*}", "root -> null child -> items[] -> {}"));
        fieldMapping.add(new PrintTemplateFieldMapping("child.items[].{testProvider:*}", "root -> child -> items[] -> {}"));

        fieldMapping.add(new PrintTemplateFieldMapping("nullItems[].child.{testProvider:*}", "root -> null items [] -> child -> {}"));
        fieldMapping.add(new PrintTemplateFieldMapping("items[].nullChild.{testProvider:*}", "root -> items[] -> null child -> {}"));
        fieldMapping.add(new PrintTemplateFieldMapping("items[].child.{testProvider:*}", "root -> items[] -> child -> {}"));

        return fieldMapping;
    }

    /**
     * Recursive object used for printing tests (this object contains isself as child, items and fields).
     * Can be used with any depth of recursion tests.
     */
    public static class PrintableObject {
        private String field = "field";
        private Map<String, Object> dynamicFields;
        private PrintableObject child = this;
        private List<PrintableObject> items = Arrays.asList(this);

        public PrintableObject() {
            dynamicFields = new HashMap<String, Object>();
            dynamicFields.put("Valid field name", "dynamicField");
            dynamicFields.put("Int. field", 100);
            dynamicFields.put("Long: field", 1000L);
            dynamicFields.put("Big Decimal field", BigDecimal.ONE);
        }

        @SuppressWarnings("unchecked")
        public static List<String> getDynamicFieldNames() {
            return Arrays.asList("Valid field name", "Int. field", "Long: field", "Big Decimal field");
        }

        public static Class getDynamicFieldValueType(String dynamicField) {
            return Object.class; //Just for test. We are so lazy to compare all values.
        }

        public String getNullField(){
            return null;
        }

        public String getField() {
            return field;
        }

        public PrintableObject getNullChild(){
            return null;
        }

        public PrintableObject getChild() {
            return child;
        }

        public List<PrintableObject> getNullItems(){
            return null;
        }

        public List<PrintableObject> getItems() {
            return items;
        }

        public Map<String, Object> getDynamicFields(){
            return dynamicFields;
        }
    }
}
