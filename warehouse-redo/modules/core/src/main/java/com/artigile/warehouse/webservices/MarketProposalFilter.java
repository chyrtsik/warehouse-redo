
package com.artigile.warehouse.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for marketProposalFilter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="marketProposalFilter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="statusType" type="{http://webservices.marketproposals.artigile.com/}statusType" minOccurs="0"/>
 *         &lt;element name="uidContractorsList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="uidCurrencyList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="uidGoodsList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="uidMeasureUnitList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "marketProposalFilter", propOrder = {
    "statusType",
    "uidContractorsList",
    "uidCurrencyList",
    "uidGoodsList",
    "uidMeasureUnitList"
})
public class MarketProposalFilter {

    protected StatusType statusType;
    @XmlElement(nillable = true)
    protected List<String> uidContractorsList;
    @XmlElement(nillable = true)
    protected List<String> uidCurrencyList;
    @XmlElement(nillable = true)
    protected List<String> uidGoodsList;
    @XmlElement(nillable = true)
    protected List<String> uidMeasureUnitList;

    /**
     * Gets the value of the statusType property.
     * 
     * @return
     *     possible object is
     *     {@link StatusType }
     *     
     */
    public StatusType getStatusType() {
        return statusType;
    }

    /**
     * Sets the value of the statusType property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusType }
     *     
     */
    public void setStatusType(StatusType value) {
        this.statusType = value;
    }

    /**
     * Gets the value of the uidContractorsList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the uidContractorsList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUidContractorsList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getUidContractorsList() {
        if (uidContractorsList == null) {
            uidContractorsList = new ArrayList<String>();
        }
        return this.uidContractorsList;
    }

    /**
     * Gets the value of the uidCurrencyList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the uidCurrencyList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUidCurrencyList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getUidCurrencyList() {
        if (uidCurrencyList == null) {
            uidCurrencyList = new ArrayList<String>();
        }
        return this.uidCurrencyList;
    }

    /**
     * Gets the value of the uidGoodsList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the uidGoodsList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUidGoodsList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getUidGoodsList() {
        if (uidGoodsList == null) {
            uidGoodsList = new ArrayList<String>();
        }
        return this.uidGoodsList;
    }

    /**
     * Gets the value of the uidMeasureUnitList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the uidMeasureUnitList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUidMeasureUnitList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getUidMeasureUnitList() {
        if (uidMeasureUnitList == null) {
            uidMeasureUnitList = new ArrayList<String>();
        }
        return this.uidMeasureUnitList;
    }

}
