package com.artigile.warehouse.utils.dto.marketProposals;

import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Vadim.Zverugo
 */
public class MarketProposalsTO {

    private long id;

    private MarketProposalsStatusTypeTO status;

    private Date giveDate;

    private DetailBatchTO detailBatch;

    private ContractorTO contractor;

    private MeasureUnitTO measureUnit;

    private CurrencyTO currency;

    private Long amount;

    private MarketProposalsAmountTypeTO amountType;

    private BigDecimal retailPrice;

    private BigDecimal wholeSalePrice;

    private BigDecimal smallWholeSalePrice;

    private String originalName;

    private String originalMisc;

    // ===================== Getters & Setters ========================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MarketProposalsStatusTypeTO getStatus() {
        return status;
    }

    public void setStatus(MarketProposalsStatusTypeTO status) {
        this.status = status;
    }

    public Date getGiveDate() {
        return giveDate;
    }

    public void setGiveDate(Date giveDate) {
        this.giveDate = giveDate;
    }

    public DetailBatchTO getDetailBatch() {
        return detailBatch;
    }

    public void setDetailBatch(DetailBatchTO detailBatch) {
        this.detailBatch = detailBatch;
    }

    public ContractorTO getContractor() {
        return contractor;
    }

    public void setContractor(ContractorTO contractor) {
        this.contractor = contractor;
    }

    public MeasureUnitTO getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(MeasureUnitTO measureUnit) {
        this.measureUnit = measureUnit;
    }

    public CurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTO currency) {
        this.currency = currency;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public MarketProposalsAmountTypeTO getAmountType() {
        return amountType;
    }

    public void setAmountType(MarketProposalsAmountTypeTO amountType) {
        this.amountType = amountType;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public BigDecimal getWholeSalePrice() {
        return wholeSalePrice;
    }

    public void setWholeSalePrice(BigDecimal wholeSalePrice) {
        this.wholeSalePrice = wholeSalePrice;
    }

    public BigDecimal getSmallWholeSalePrice() {
        return smallWholeSalePrice;
    }

    public void setSmallWholeSalePrice(BigDecimal smallWholeSalePrice) {
        this.smallWholeSalePrice = smallWholeSalePrice;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getOriginalMisc() {
        return originalMisc;
    }

    public void setOriginalMisc(String originalMisc) {
        this.originalMisc = originalMisc;
    }
}
