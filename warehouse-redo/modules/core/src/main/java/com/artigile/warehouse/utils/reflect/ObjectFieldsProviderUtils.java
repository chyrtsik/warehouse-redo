/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.reflect;

/**
 * Utility class with common functions for object fields accessing.
 *
 * @author Aliaksandr.Chyrtsik, 18.12.12
 */
public final class ObjectFieldsProviderUtils {
    private ObjectFieldsProviderUtils(){
    }

    /**
     * Determine if given object field represents dynamic field.
     * @param objectField field field mapping to be checked.
     * @return true if field is dynamic.
     */
    public static boolean isDynamicField(String objectField) {
        return objectField.matches("^[^\\{]*\\{[^\\{]+\\:\\*\\}$");
    }

    /**
     * Determine if given object field mapping id dynamic field with formatted provider and field name.
     * @param objectField object field mapping to check.
     * @return true if field is dynamic.
     */
    public static boolean isDynamicFieldFormatted(String objectField) {
        return objectField.matches("^[^\\{]*\\{[^\\{]+\\:[^\\{]+\\}$");
    }

    /**
     * Parse name of dynamic object fields provider.
     * @param objectField object field access path (format is the same as for ObjectFieldsValueProvider).
     * @return parsed name of an provider.
     */
    public static String parseDynamicFieldProviderName(String objectField) {
        int begin = objectField.indexOf('{') + 1;
        return objectField.substring(begin, objectField.indexOf(':', begin));
    }

    /**
     * Parse name of dynamic field.
     * @param objectField full object field mapping with provider and field name.
     * @return dynamic field name.
     */
    public static String parseDynamicFieldName(String objectField) {
        int providerBegin = objectField.indexOf('{') + 1;
        int fieldBegin = objectField.indexOf(':', providerBegin) + 1;
        return objectField.substring(fieldBegin, objectField.indexOf('}', fieldBegin));
    }

    /**
     * Format object field to store information needed by objects fields provider to access dynamic field.
     * @param objectField original object field access string.
     * @param providerName provider to access field value.
     * @param dynamicField dynamic field name.
     * @return formatted object field mapping string.
     */
    public static String formatDynamicObjectFieldName(String objectField, String providerName, String dynamicField) {
        return objectField.replace("{" + providerName + ":*}", "{" + providerName + ":" +  dynamicField + "}");
    }

    /**
     * Format dynamic field to be presented to the end user (in settings windows and in reports designer).
     * @param reportField original report field name.
     * @param dynamicField dynamic field name.
     * @return user friendly name of dynamic field (all possible names to access this value).
     */
    public static String[] formatDynamicReportFieldName(String reportField, String dynamicField) {
        if (reportField.contains("{}")){
            return new String[]{
                    reportField.replace("{}", "{" + dynamicField + "}"),
                    reportField.replace("{}", "[" + dynamicField + "]"),
            };
        }
        else{
            return new String[] {
                    reportField + ".{" + dynamicField + "}",
                    reportField + ".[" + dynamicField + "]",
            };
        }
    }
}
