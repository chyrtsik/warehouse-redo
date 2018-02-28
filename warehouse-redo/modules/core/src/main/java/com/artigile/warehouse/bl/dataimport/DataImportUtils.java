/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.dataimport;

import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.ManufacturerTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Utilities for data import parsing and data row processing.
 */
public final class DataImportUtils {
    private DataImportUtils() {
    }

    /**
     * Dump string representation pf data row (for logging purposes).
     * @param dataRow data row to be dumped.
     * @return string perresentation of data row.
     */
    public static String formatDataRow(Map<String, String> dataRow) {
        StringBuilder builder = new StringBuilder();
        for (String key : dataRow.keySet()) {
            builder.append(key).append("=").append(dataRow.get(key)).append(";");
        }
        return builder.toString();
    }


    public static ManufacturerTO parseManufacturerValue(String value, String fieldName) throws DataImportException {
        if (value == null){
            return null;
        }
        String trimValue =  value.trim();
        if (trimValue.isEmpty()){
            return null;
        }
        ManufacturerTO manufacturer = SpringServiceContext.getInstance().getManufacturerService().getManufacturerByName(trimValue);
        if (manufacturer != null){
            return manufacturer;
        }
        else {
            throw new DataImportException(I18nSupport.message("data.import.error.cannot.parse.manufacturer", fieldName, trimValue));
        }
    }

    public static BigDecimal parseBigDecimalValue(String value, String fieldName) throws DataImportException {
        if (value == null){
            return null;
        }
        String trimValue = value.trim();
        if (trimValue.isEmpty()){
            return null;
        }
        if (StringUtils.isNumber(value)){
            return StringUtils.parseStringToBigDecimal(trimValue);
        }
        else{
            throw new DataImportException(I18nSupport.message("data.import.error.cannot.parse.number", fieldName, trimValue));
        }
    }

    public static String parseStringValue(String value, String fieldName) throws DataImportException {
        if (value == null){
            return null;
        }
        String trimValue = value.trim();
        if (trimValue.length() > ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH){
            throw new DataImportException(I18nSupport.message("data.import.error.too.long.string.value", fieldName, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH));
        }
        return trimValue;
    }

    public static Integer parseIntegerValue(String value, String fieldName) throws DataImportException {
        if (value == null){
            return null;
        }
        String trimValue = value.trim();
        if (trimValue.isEmpty()){
            return null;
        }
        else if (StringUtils.isNumberInteger(trimValue)){
            return Integer.valueOf(trimValue);
        }
        else{
            throw new DataImportException(I18nSupport.message("data.import.error.cannot.parse.integer", fieldName, trimValue));
        }
    }

    public static Long parseLongValue(String value, String fieldName) throws DataImportException {
        if (value == null){
            return null;
        }
        String trimValue = value.trim();
        if (trimValue.isEmpty()){
            return null;
        }
        else if (StringUtils.isNumberLong(trimValue)){
            return Long.valueOf(trimValue);
        }
        else{
            throw new DataImportException(I18nSupport.message("data.import.error.cannot.parse.integer", fieldName, trimValue));
        }
    }
}