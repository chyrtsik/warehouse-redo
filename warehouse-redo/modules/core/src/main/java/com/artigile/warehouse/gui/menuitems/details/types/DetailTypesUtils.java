/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.types;

import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * Utility class for detail types.
 * @author Shyrik, 16.12.2008
 */
public final class DetailTypesUtils {

    private DetailTypesUtils(){ }

    /**
     * Gets string representation for the boolean value.
     * @param value
     * @return
     */
    public static String getBoooleanAsString(Boolean value) {
        if (value == null){
            return null;
        }
        return value ? getYesValue() : getNoValue();
    }

    /**
     * Converts string to the boolean value.
     * @param value - string represenation of the boolean. May be "" or null.
     * @return
     */
    public static Boolean getStringAsBoolean(String value){
        if (value == null || value.isEmpty()) {
            return null;
        }
        if (value.equals(getYesValue())){
            return true;
        }
        else if (value.equals(getNoValue())){
            return false;
        }
        else{
            throw new IllegalArgumentException("Invalid boolean value '" + value + "'");
        }
    }

    public static String getNullAsString() {
        return I18nSupport.message("detail.field.value.null");
    }

    private static String getYesValue() {
        return I18nSupport.message("detail.field.value.yes");
    }

    private static String getNoValue() {
        return I18nSupport.message("detail.field.value.no");
    }

    public static String getSelectValueWarning() {
        return I18nSupport.message("detail.field.selectValueWarning");
    }

    public static String getUserDisplayName(String userId) {
        if (!StringUtils.hasValue(userId)){
            return getNullAsString();
        }
        else if (!StringUtils.isNumberLong(userId)){
            return I18nSupport.message("detail.type.error");
        }

        User user = SpringServiceContext.getInstance().getUserService().getUserById(Long.valueOf(userId));
        if (user == null){
            return I18nSupport.message("serial.number.error.user.not.found", userId);
        }

        if (StringUtils.hasValue(user.getNameOnProduct())){
            return user.getNameOnProduct();
        }
        else{
            return I18nSupport.message("serial.number.error.user.name.on.product.not.set", user.getId(), user.getLogin());
        }
    }
}
