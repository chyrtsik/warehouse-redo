/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.priceimport;

import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.CurrencyTO;

import java.util.List;

/**
 * Utility class for values parsing.
 *
 * @author Valery Barysok, 6/27/11
 */

public class PriceImportParsingUtils {
    private String currencyRegEx = buildCurrencyRegEx();

    public String removeMeasureUnit(String value) {
        value = value.replaceAll("(шт\\.)|(шт)", "").trim();
        return value;
    }

    public String removeCurrency(String value) {
        value = value.replaceAll(currencyRegEx, "").trim();
        return value;
    }

    //========================   Helpers   ===============================
    private String buildCurrencyRegEx() {
        //Building regex for elimination of currencies from the values.
        StringBuilder currencyRexExBuilder = new StringBuilder().append("(\\$)|(руб\\.)|(руб)");
        List<CurrencyTO> currenciesList = SpringServiceContext.getInstance().getCurrencyService().getAll();
        for (CurrencyTO currency : currenciesList){
            currencyRexExBuilder.append("|(").append(getCaseInsensitiveRegEx(currency.getSign())).append(")");
        }
        return currencyRexExBuilder.toString();
    }

    private String getCaseInsensitiveRegEx(String string) {
        String uCaseString = string.toUpperCase();
        String lCaseString = string.toLowerCase();
        StringBuilder regExBuilder = new StringBuilder();

        for (int i=0; i<string.length(); i++){
            regExBuilder.append("(").append(uCaseString.charAt(i)).append("|").append(lCaseString.charAt(i)).append(")");
        }

        return regExBuilder.toString();
    }
}
