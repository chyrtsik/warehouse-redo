/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.marketProposals;

import com.artigile.warehouse.bl.marketProposals.MarketProposalsService;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.marketProposals.MarketProposalsTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Vadim.Zverugo
 */

/**
 * Model of table with contractors proposals list.
 */
public class MarketProposalsList extends ReportDataSourceBase {

    /**
     * List of market proposals which received from web-service.
     */
    private List<MarketProposalsTO> receivedMarketProposals;

    /**
     * List of market proposals which display in data table.
     */
    private List<MarketProposalsTO> displayMarketProposals;

    public MarketProposalsList(List<Long> detailBatchIds, List<Long> contractorIds, List<Long> currencyIds, List<Long> measureUnitIds) {
        //Load required market proposals data.
        MarketProposalsService marketProposalsService = SpringServiceContext.getInstance().getMarketProposalsService();
        this.receivedMarketProposals = marketProposalsService.findMarketProposals(detailBatchIds, contractorIds, currencyIds, measureUnitIds);
        this.displayMarketProposals = new ArrayList<MarketProposalsTO>();
    }

    @Override
    public String getReportTitle() {
        return "<not used>";
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return null;
    }

    @Override
    public List getReportData() {
        return displayMarketProposals;
    }

    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(MarketProposalsTO.class);

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("market.proposals.list.proposal.number"), "id"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("market.proposals.list.proposal.status"), "status.statusName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("market.proposals.list.proposal.giveDate"), "giveDate"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("market.proposals.list.proposal.goods.name"), "detailBatch.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("market.proposals.list.proposal.goods.misc"), "detailBatch.misc"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("market.proposals.list.proposal.contractor.name"), "contractor.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("market.proposals.list.proposal.contractor.rating"), "contractor.rating"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("market.proposals.list.proposal.measureUnit.name"), "measureUnit.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("market.proposals.list.proposal.currency.name"), "currency.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("market.proposals.list.proposal.amount"), "amount",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("market.proposals.list.proposal.amountType"), "amountType.amountTypeName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("market.proposals.list.proposal.price.retail"), "retailPrice",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("market.proposals.list.proposal.price.wholeSale"), "wholeSalePrice",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("market.proposals.list.proposal.price.smallWholeSale"), "smallWholeSalePrice",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("market.proposals.list.proposal.original.name"), "originalName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("market.proposals.list.proposal.original.misc"), "originalMisc"));

        return reportInfo;
    }

    //========================== Contractor's rating filter ===============================

    /**
     * Add items of market proposals by some contractor rating on display.
     * @param rating - new value of filter.
     */
    public void addMarketProposalsByContractorRating(Integer rating) {
        if (tableReport != null) {
            displayMarketProposals = tableReport.getDisplayedReportItems();
        }

        for (MarketProposalsTO marketProposal : receivedMarketProposals) {
            if (marketProposal.getContractor().getRating() == null) {
                if (rating == null && !displayMarketProposals.contains(marketProposal)) {
                    displayMarketProposals.add(marketProposal);
                }
            } else if (marketProposal.getContractor().getRating().equals(rating)) {
                if (!displayMarketProposals.contains(marketProposal)) {
                    displayMarketProposals.add(marketProposal);
                }
            }
        }
    }

    /**
     * Del items of market proposals by some contractor rating from display.
     * @param rating - old value of filter.
     */
    public void delMarketProposalByContractorRating(Integer rating) {
        if (tableReport != null) {
            displayMarketProposals = tableReport.getDisplayedReportItems();
        }

        for (MarketProposalsTO marketProposal : receivedMarketProposals) {
            if (marketProposal.getContractor().getRating() == null) {
                if (rating == null && displayMarketProposals.contains(marketProposal)) {
                    displayMarketProposals.remove(marketProposal);
                }
            } else if (marketProposal.getContractor().getRating().equals(rating)) {
                if (displayMarketProposals.contains(marketProposal)) {
                    displayMarketProposals.remove(marketProposal);
                }
            }
        }
    }
}

