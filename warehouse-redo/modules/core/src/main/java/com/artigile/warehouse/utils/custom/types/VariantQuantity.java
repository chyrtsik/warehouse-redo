package com.artigile.warehouse.utils.custom.types;

import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.Utils;

/**
 *
 * @author Valery.Barysok
 */
public class VariantQuantity implements Comparable<VariantQuantity> {
    
    private String value;
    
    private Double quantity;


    /* Constructors
    ------------------------------------------------------------------------------------------------------------------*/
    public VariantQuantity(String value) {
        this.value = value;
        this.quantity = Utils.getDouble(value.replace(',', '.'));
    }

    public VariantQuantity(Double quantity) {
        this.quantity = quantity;
        this.value = (quantity == null) ? StringUtils.EMPTY_STRING : String.valueOf(quantity);
    }


    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(VariantQuantity qnt) {
        if (isNumber() && qnt.isNumber()) {
            return getQuantity().compareTo(qnt.getQuantity());
        }

        return getValue().compareTo(qnt.getValue());
    }


    /* Getters and setters
    ------------------------------------------------------------------------------------------------------------------*/
    public Double getQuantity() {
        return quantity;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.quantity = Utils.getDouble(value);
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
        this.value = String.valueOf(quantity);
    }

    public boolean isNumber() {
        return quantity != null;
    }
}
