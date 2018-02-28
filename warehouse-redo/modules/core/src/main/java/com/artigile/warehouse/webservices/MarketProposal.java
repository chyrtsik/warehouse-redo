
package com.artigile.warehouse.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;


/**
 * <p>Java class for marketProposal complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="marketProposal">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="amountType" type="{http://webservices.marketproposals.artigile.com/}amountType" minOccurs="0"/>
 *         &lt;element name="giveDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="originalMisc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="originalName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="retailPrice" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="smallWholeSalePrice" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="status" type="{http://webservices.marketproposals.artigile.com/}statusType" minOccurs="0"/>
 *         &lt;element name="uidContractor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uidCurrency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uidGoods" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uidMeasureUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="wholeSalePrice" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "marketProposal", propOrder = {
    "amount",
    "amountType",
    "giveDate",
    "id",
    "originalMisc",
    "originalName",
    "retailPrice",
    "smallWholeSalePrice",
    "status",
    "uidContractor",
    "uidCurrency",
    "uidGoods",
    "uidMeasureUnit",
    "wholeSalePrice"
})
public class MarketProposal {

    protected Long amount;
    protected AmountType amountType;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar giveDate;
    protected long id;
    protected String originalMisc;
    protected String originalName;
    protected BigDecimal retailPrice;
    protected BigDecimal smallWholeSalePrice;
    protected StatusType status;
    protected String uidContractor;
    protected String uidCurrency;
    protected String uidGoods;
    protected String uidMeasureUnit;
    protected BigDecimal wholeSalePrice;

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAmount(Long value) {
        this.amount = value;
    }

    /**
     * Gets the value of the amountType property.
     * 
     * @return
     *     possible object is
     *     {@link AmountType }
     *     
     */
    public AmountType getAmountType() {
        return amountType;
    }

    /**
     * Sets the value of the amountType property.
     * 
     * @param value
     *     allowed object is
     *     {@link AmountType }
     *     
     */
    public void setAmountType(AmountType value) {
        this.amountType = value;
    }

    /**
     * Gets the value of the giveDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getGiveDate() {
        return giveDate;
    }

    /**
     * Sets the value of the giveDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setGiveDate(XMLGregorianCalendar value) {
        this.giveDate = value;
    }

    /**
     * Gets the value of the id property.
     * 
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Gets the value of the originalMisc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalMisc() {
        return originalMisc;
    }

    /**
     * Sets the value of the originalMisc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalMisc(String value) {
        this.originalMisc = value;
    }

    /**
     * Gets the value of the originalName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalName() {
        return originalName;
    }

    /**
     * Sets the value of the originalName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalName(String value) {
        this.originalName = value;
    }

    /**
     * Gets the value of the retailPrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    /**
     * Sets the value of the retailPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRetailPrice(BigDecimal value) {
        this.retailPrice = value;
    }

    /**
     * Gets the value of the smallWholeSalePrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSmallWholeSalePrice() {
        return smallWholeSalePrice;
    }

    /**
     * Sets the value of the smallWholeSalePrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSmallWholeSalePrice(BigDecimal value) {
        this.smallWholeSalePrice = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link StatusType }
     *     
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusType }
     *     
     */
    public void setStatus(StatusType value) {
        this.status = value;
    }

    /**
     * Gets the value of the uidContractor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUidContractor() {
        return uidContractor;
    }

    /**
     * Sets the value of the uidContractor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUidContractor(String value) {
        this.uidContractor = value;
    }

    /**
     * Gets the value of the uidCurrency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUidCurrency() {
        return uidCurrency;
    }

    /**
     * Sets the value of the uidCurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUidCurrency(String value) {
        this.uidCurrency = value;
    }

    /**
     * Gets the value of the uidGoods property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUidGoods() {
        return uidGoods;
    }

    /**
     * Sets the value of the uidGoods property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUidGoods(String value) {
        this.uidGoods = value;
    }

    /**
     * Gets the value of the uidMeasureUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUidMeasureUnit() {
        return uidMeasureUnit;
    }

    /**
     * Sets the value of the uidMeasureUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUidMeasureUnit(String value) {
        this.uidMeasureUnit = value;
    }

    /**
     * Gets the value of the wholeSalePrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getWholeSalePrice() {
        return wholeSalePrice;
    }

    /**
     * Sets the value of the wholeSalePrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setWholeSalePrice(BigDecimal value) {
        this.wholeSalePrice = value;
    }

}
