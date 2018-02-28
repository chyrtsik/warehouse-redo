package com.artigile.swingx.filter.comparator.impl;

import com.artigile.swingx.filter.comparator.NumberComparator;
import com.artigile.swingx.filter.parser.FilterParser;
import com.artigile.swingx.filter.parser.SearchItem;
import com.artigile.swingx.filter.parser.SearchTypeConst;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.Utils;
import com.artigile.warehouse.utils.custom.types.VariantQuantity;

import java.util.regex.Pattern;

/**
 *
 * @author valery.barysok
 */
public class VariantQuantityComparator extends NumberComparator<Double> {

    /**
     * Possible string representation of the quantity (with special symbols, such as '<', '>', etc.)
     */
    private static final Pattern VARIANT_QUANTITY_STRING_PATTERN = Pattern.compile("^([<=>]{1,2})?[0-9]+([,|.][0-9]+)?$");
    private static final double VARIANT_QUANTITY_TRICK_STEP = 0.000000001;


    @Override
    protected Double parse(String text) {
        return Utils.getDouble(text);
    }

    @Override
    public int compareTo(Object o) {
        VariantQuantity variantQuantity = (VariantQuantity) o;
        double comparableValue;
        if (variantQuantity.isNumber()) {
            comparableValue = variantQuantity.getQuantity();
        } else {
            SearchItem searchItem = FilterParser.parse(StringUtils.removeSpaces(variantQuantity.getValue()));
            comparableValue = Utils.getDouble(searchItem.getText().replace(',', '.'));

            // Small trick for correct filtering.
            // Used for resolving troubles when input and table values are equals.
            if (searchItem.getSearchType() == SearchTypeConst.GREATER_SEARCH) {
                comparableValue += VARIANT_QUANTITY_TRICK_STEP;
            } else if (searchItem.getSearchType() == SearchTypeConst.LESS_SEARCH) {
                comparableValue -= VARIANT_QUANTITY_TRICK_STEP;
            }
        }
        return value.compareTo(comparableValue);
    }

    @Override
    public boolean validate(Object value) {
        boolean result = super.validate(value);
        if (result) {
            VariantQuantity variantQuantity = (VariantQuantity) value;
            if (!variantQuantity.isNumber()) {
                result = StringUtils.containsSymbols(variantQuantity.getValue())
                        && VARIANT_QUANTITY_STRING_PATTERN.matcher(StringUtils.removeSpaces(variantQuantity.getValue())).matches();
            }
        }
        return result;
    }
}
