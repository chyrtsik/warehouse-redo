package com.artigile.warehouse.utils.dto.marketProposals;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Vadim.Zverugo
 */

/**
 * Knowledge about amount of goods which proposes some contractor.
 * This knowledge must be exact or not (approximate, greater, less, unknown).
 */
public enum MarketProposalsAmountTypeTO {

    /**
     * Value of amount declared exactly.
     */
    EXACT(1, I18nSupport.message("market.proposal.amount.type.exact")),

    /**
     * Value of amount may not coincide with real contractor amount.
     */
    APPROXIMATE(2, I18nSupport.message("market.proposal.amount.type.approximate")),

    /**
     * When value of amount greater then real contractor amount.
     */
    GREATER(3, I18nSupport.message("market.proposal.amount.type.greater")),

    LESS(4, I18nSupport.message("market.proposal.amount.type.less")),

    /**
     * When value of amount is unknown.
     */
    UNKNOWN(5, I18nSupport.message("market.proposal.amount.type.unknown"));

    //======================================================================

    private int amountTypeNumber;
    private String amountTypeName;

    MarketProposalsAmountTypeTO(int amountTypeNumber, String amountTypeName) {
        this.amountTypeNumber = amountTypeNumber;
        this.amountTypeName = amountTypeName;
    }

    public String getAmountTypeName() {
        return amountTypeName;
    }
}
