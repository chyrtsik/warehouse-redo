package com.artigile.warehouse.utils.custom.types;

import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.Utils;

/**
 *
 * @author Valery.Barysok
 */
public class VariantPrice implements Comparable<VariantPrice> {
    
    private String value;
    
    private Double price;


    /* Constructors
    ------------------------------------------------------------------------------------------------------------------*/
    public VariantPrice(String value) {
        this.value = value;
        this.price = Utils.getDouble(value.replace(',', '.'));
    }

    public VariantPrice(Double price) {
        this.price = price;
        this.value = (price == null) ? StringUtils.EMPTY_STRING : String.valueOf(price);
    }


    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(VariantPrice price) {
        if (isNumber() && price.isNumber()) {
            return getPrice().compareTo(price.getPrice());
        }

        return getValue().compareTo(price.getValue());
    }


    /* Getters and setters
    ------------------------------------------------------------------------------------------------------------------*/
    public Double getPrice() {
        return price;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.price = Utils.getDouble(value);
    }

    public void setPrice(Double price) {
        this.price = price;
        this.value = String.valueOf(price);
    }

    public boolean isNumber() {
        return price != null;
    }
}
