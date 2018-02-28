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

import com.artigile.warehouse.utils.reflect.ObjectFieldsProvider;
import com.artigile.warehouse.utils.reflect.ObjectFieldsProviderUtils;
import com.artigile.warehouse.utils.reflect.SimpleObjectsFieldsProvider;
import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests of objects field provider (for access to objects shown in reports).
 *
 * @author Shyrik, 30.01.2009
 */
public class ObjectFieldsProviderTest {
    public class DataObject {
        private String name;

        public DataObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private List<PrintableField> fields = new ArrayList<PrintableField>();

        public List<PrintableField> getFields() {
            return fields;
        }

        public class PrintableField{
            private String name;

            public PrintableField(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        public void addField(String fieldName){
            fields.add(new PrintableField(fieldName));
        }


    }

    /**
     * Test of accessing to the List<...> devived fields of the object.
     */
    @Test
    public void testFieldsProvider() {
        DataObject obj = new DataObject("Object1");
        obj.addField("item1");
        obj.addField("item2");
        obj.addField("item3");

        List<String> objectFields = new ArrayList<String>();
        objectFields.add("name");
        objectFields.add("fields");
        objectFields.add("fields[].name");
        ObjectFieldsProvider provider = new ObjectFieldsProvider(obj, objectFields);

        Assert.assertEquals(String.class, provider.getFieldValueClass(0));
        Assert.assertEquals(List.class, provider.getFieldValueClass(1));
        Assert.assertEquals(String.class, provider.getFieldValueClass(2));

        Assert.assertEquals(3, provider.getItemsCount());
        Assert.assertEquals("Object1", provider.getFieldValue(0, 0));
        Assert.assertEquals("Object1", provider.getFieldValue(0, 1));
        Assert.assertEquals("Object1", provider.getFieldValue(0, 2));
        Assert.assertEquals(obj.getFields(), provider.getFieldValue(1, 0));
        Assert.assertEquals("item1", provider.getFieldValue(2, 0));
        Assert.assertEquals("item2", provider.getFieldValue(2, 1));
        Assert.assertEquals("item3", provider.getFieldValue(2, 2));
    }

    /**
     * Test of the SimpleObjectsFieldProvider
     */
    @Test
    public void testSimpleFieldsProvider() {
        DataObject obj = new DataObject("Object1");
        obj.addField("item1");
        obj.addField("item2");
        obj.addField("item3");

        String []objectFields = {"name", "fields[0].name", "fields[1].name", "fields[2].name"};
        SimpleObjectsFieldsProvider provider = new SimpleObjectsFieldsProvider(obj.getClass(), objectFields);

        //Test of single field
        Assert.assertEquals(provider.getFieldValue(obj, 0), "Object1");
        //Test of array item field.
        Assert.assertEquals(provider.getFieldValue(obj, 1), "item1");
        Assert.assertEquals(provider.getFieldValue(obj, 2), "item2");
        Assert.assertEquals(provider.getFieldValue(obj, 3), "item3");
    }

    @Test
    public void testDynamicObjectFieldsProviderParsing(){
        Assert.assertTrue(ObjectFieldsProviderUtils.isDynamicField("root.items[].child.{someDynamicFieldProvider:*}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicField("root.items[].child.{someDynamicFieldProvider}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicField("root.items[].child.{}"));
        Assert.assertTrue(ObjectFieldsProviderUtils.isDynamicField("root.items[].{someDynamicFieldProvider:*}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicField("root.items[].{someDynamicFieldProvider}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicField("root.items[].{}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicField("root.items[].child.{}"));
        Assert.assertTrue(ObjectFieldsProviderUtils.isDynamicField("{someDynamicFieldProvider:*}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicField("{someDynamicFieldProvider}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicField("{}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicField("root.items[].child.{someDynamicFieldProvider:*}.child.field"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicField("root.items[].child.{someDynamicFieldProvider:*}.child.{otherDynamicField:*}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicField("root.items[].child.field"));

        Assert.assertTrue(ObjectFieldsProviderUtils.isDynamicFieldFormatted("root.items[].child.{someDynamicFieldProvider:Some field}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicFieldFormatted("root.items[].child.{someDynamicFieldProvider}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicFieldFormatted("root.items[].child.{}"));
        Assert.assertTrue(ObjectFieldsProviderUtils.isDynamicFieldFormatted("root.items[].{someDynamicFieldProvider:Some field}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicFieldFormatted("root.items[].{someDynamicFieldProvider}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicFieldFormatted("root.items[].{}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicFieldFormatted("root.items[].child.{}"));
        Assert.assertTrue(ObjectFieldsProviderUtils.isDynamicFieldFormatted("{someDynamicFieldProvider:Some field}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicFieldFormatted("{someDynamicFieldProvider}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicFieldFormatted("{}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicFieldFormatted("root.items[].child.{someDynamicFieldProvider:Some field}.child.field"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicFieldFormatted("root.items[].child.{someDynamicFieldProvider:Some field}.child.{otherDynamicField:Some other field}"));
        Assert.assertFalse(ObjectFieldsProviderUtils.isDynamicFieldFormatted("root.items[].child.field"));

        String parsedProviderName = ObjectFieldsProviderUtils.parseDynamicFieldProviderName("root.items[].child.{someDynamicFieldProvider:*}");
        Assert.assertEquals("someDynamicFieldProvider", parsedProviderName);

        String objectFieldFormatted = ObjectFieldsProviderUtils.formatDynamicObjectFieldName("root.items[].child.{someDynamicFieldProvider:*}", parsedProviderName, "Some fld. name");
        Assert.assertEquals("root.items[].child.{someDynamicFieldProvider:Some fld. name}", objectFieldFormatted);

        String secondParsedProviderName = ObjectFieldsProviderUtils.parseDynamicFieldProviderName(objectFieldFormatted);
        String parsedFieldName = ObjectFieldsProviderUtils.parseDynamicFieldName(objectFieldFormatted);
        Assert.assertEquals("someDynamicFieldProvider", secondParsedProviderName);
        Assert.assertEquals("Some fld. name", parsedFieldName);

        String[] reportFieldsFormatted = ObjectFieldsProviderUtils.formatDynamicReportFieldName("Order.Item.{}", "Some fld. name");
        Assert.assertEquals("Order.Item.{Some fld. name}", reportFieldsFormatted[0]);
        Assert.assertEquals("Order.Item.[Some fld. name]", reportFieldsFormatted[1]);
    }
}
