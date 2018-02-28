
package com.artigile.warehouse.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getMarketProposalsByFilter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getMarketProposalsByFilter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://webservices.marketproposals.artigile.com/}marketProposalFilter" minOccurs="0"/>
 *         &lt;element name="arg1" type="{http://webservices.marketproposals.artigile.com/}marketProposalSorting" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getMarketProposalsByFilter", propOrder = {
    "arg0",
    "arg1"
})
public class GetMarketProposalsByFilter {

    protected MarketProposalFilter arg0;
    protected MarketProposalSorting arg1;

    /**
     * Gets the value of the arg0 property.
     * 
     * @return
     *     possible object is
     *     {@link MarketProposalFilter }
     *     
     */
    public MarketProposalFilter getArg0() {
        return arg0;
    }

    /**
     * Sets the value of the arg0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link MarketProposalFilter }
     *     
     */
    public void setArg0(MarketProposalFilter value) {
        this.arg0 = value;
    }

    /**
     * Gets the value of the arg1 property.
     * 
     * @return
     *     possible object is
     *     {@link MarketProposalSorting }
     *     
     */
    public MarketProposalSorting getArg1() {
        return arg1;
    }

    /**
     * Sets the value of the arg1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link MarketProposalSorting }
     *     
     */
    public void setArg1(MarketProposalSorting value) {
        this.arg1 = value;
    }

}
