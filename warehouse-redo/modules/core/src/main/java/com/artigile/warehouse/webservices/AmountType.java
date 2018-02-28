
package com.artigile.warehouse.webservices;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for amountType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="amountType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="EXACT"/>
 *     &lt;enumeration value="APPROXIMATE"/>
 *     &lt;enumeration value="GREATER"/>
 *     &lt;enumeration value="LESS"/>
 *     &lt;enumeration value="UNKNOWN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "amountType")
@XmlEnum
public enum AmountType {

    EXACT,
    APPROXIMATE,
    GREATER,
    LESS,
    UNKNOWN;

    public String value() {
        return name();
    }

    public static AmountType fromValue(String v) {
        return valueOf(v);
    }

}
