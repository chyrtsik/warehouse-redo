package com.artigile.warehouse.utils.dto.marketProposals;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Vadim.Zverugo
 */

/**
 * Market proposal status type (archival or active).
 */
public enum MarketProposalsStatusTypeTO {

    /**
     * Status for data which were updated already.
     */
    ARCHIVAL(1, I18nSupport.message("market.proposal.status.archival")),

    /**
     * Status for newest data about market proposals.
     */
    ACTIVE(2, I18nSupport.message("market.proposal.status.active"));

    // ==============================================

    private int statusNumber;
    private String statusName;

    MarketProposalsStatusTypeTO(int statusNumber, String statusName) {
        this.statusNumber = statusNumber;
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}
