/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.installer.utils;

import org.netbeans.installer.utils.LogManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public abstract class CommonUtils {

    /* String utils
    ------------------------------------------------------------------------------------------------------------------*/
    public static boolean isEmptyString(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static boolean isIntegerNumberInRange(String string, int min, int max) {
        Integer number;
        try {
            number = Integer.parseInt(string.trim());
        } catch (NumberFormatException e) {
            return false;
        }
        return number != null && number >= min && number <= max;
    }

    /* Character utils
    ------------------------------------------------------------------------------------------------------------------*/
    public static boolean isEmptyCharArray(char[] array) {
        if (array.length == 0) {
            return true;
        }
        String string = toString(array);
        return string.trim().isEmpty();
    }

    public static String toString(char[] characters) {
        return String.valueOf(characters);
    }
    
    /* Properties utils
    ------------------------------------------------------------------------------------------------------------------*/
    public static Properties loadPropertiesFromFile(String pathToFile) {
        Properties properties = new Properties();
        if (!isEmptyString(pathToFile)) {
            try {
                FileInputStream fileInputStream = new FileInputStream(pathToFile);
                properties.load(fileInputStream);
                fileInputStream.close();
            } catch (IOException e) {
                LogManager.log(e);
            }
        }
        return properties;
    }
    
    public static Properties loadPropertiesFromStream(InputStream inputStream) {
        Properties properties = new Properties();
        if (inputStream != null) {
            try {
                properties.load(inputStream);
                inputStream.close();
            } catch (IOException e) {
                LogManager.log(e);
            }
        }
        return properties;
    }
}
