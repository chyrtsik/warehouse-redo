/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.preferences;

import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.properties.Properties;


/**
 * Utils to work with system options, that are stored in the database.
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public abstract class SystemPreferencesUtils {

    public static String getSysOption(SystemPreferences option) {
        return Properties.getProperty(option.getOptionName()); // loads value of option from the database
    }

    public static Boolean getSysOptionBoolean(SystemPreferences option){
        String value = getSysOption(option);
        return value == null ? null : Boolean.valueOf(value);
    }

    public static Integer getSysOptionInteger(SystemPreferences option){
        String value = getSysOption(option);
        return StringUtils.isNumberInteger(value) ? Integer.valueOf(value) : null;
    }

    public static <T> T getSysOptionEnum(SystemPreferences option, Class<T> enumClass) {
        String value = getSysOption(option);
        if (value != null) {
            for (T enumValue : enumClass.getEnumConstants()){
                if (enumValue.toString().equals(value)){
                    return enumValue;
                }
            }
        }
        return null;
    }
    
    public static void setSysOption(SystemPreferences option, String value) {
        Properties.setProperty(option.getOptionName(), value); // save value of option into the database
    }

    /**
     * Applies custom option to the current configuration of system.
     *
     * @see <code>SystemPreferences</code>
     *
     * @param option One of the system options
     */
    public static void applySysOption(SystemPreferences option) {
        switch (option) {
            case NUMBER_FORMAT_PATTERN: {
                applyNumberFormatOptions();
                break;
            }
            // add other preferences here
        }
    }

    /**
     * Applies all custom options to the current configuration of system.
     *
     * @see <code>SystemPreferences</code>
     */
    public static void applySysOptions() {
        for (SystemPreferences option : SystemPreferences.values()) {
            applySysOption(option);
        }
    }

    /* Concrete realizations of applying each option
    ------------------------------------------------------------------------------------------------------------------*/
    public static void applyNumberFormatOptions() {
        String pattern = getSysOption(SystemPreferences.NUMBER_FORMAT_PATTERN);
        String decimalS = getSysOption(SystemPreferences.NUMBER_FORMAT_DECIMAL_SEPARATOR);
        String groupS = getSysOption(SystemPreferences.NUMBER_FORMAT_GROUP_SEPARATOR);

        if (pattern != null && decimalS != null && groupS != null) {
            StringUtils.updateNumberFormatter(pattern, decimalS.charAt(0), groupS.charAt(0));
        }
    }
}
