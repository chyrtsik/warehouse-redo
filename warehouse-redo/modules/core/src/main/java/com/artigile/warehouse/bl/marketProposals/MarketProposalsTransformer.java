package com.artigile.warehouse.bl.marketProposals;

import com.artigile.warehouse.bl.contractors.ContractorService;
import com.artigile.warehouse.bl.detail.DetailBatchService;
import com.artigile.warehouse.bl.directory.MeasureUnitService;
import com.artigile.warehouse.bl.finance.CurrencyService;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.marketProposals.MarketProposalsAmountTypeTO;
import com.artigile.warehouse.utils.dto.marketProposals.MarketProposalsStatusTypeTO;
import com.artigile.warehouse.utils.dto.marketProposals.MarketProposalsTO;
import com.artigile.warehouse.utils.transofmers.ContractorTransformer;
import com.artigile.warehouse.utils.transofmers.CurrencyTransformer;
import com.artigile.warehouse.utils.transofmers.DetailBatchTransformer;
import com.artigile.warehouse.utils.transofmers.MeasureUnitTransformer;
import com.artigile.warehouse.webservices.MarketProposal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vadim.Zverugo
 */

/**
 * Market proposals transformer convert web-service objects to domain model objects
 */
public class MarketProposalsTransformer {

    /**
     * Convert market proposals web-service object, received from remote module, to domain
     * model market proposals object.
     */
    public static MarketProposalsTO transform(MarketProposal marketProposalWS) {
        MarketProposalsTO marketProposalsTO = new MarketProposalsTO();

        marketProposalsTO.setId(marketProposalWS.getId());
        
        if (marketProposalWS.getStatus().value().equals(MarketProposalsStatusTypeTO.ACTIVE.name())) {
            marketProposalsTO.setStatus(MarketProposalsStatusTypeTO.ACTIVE);
        } else if (marketProposalWS.getStatus().value().equals(MarketProposalsStatusTypeTO.ARCHIVAL.name())) {
            marketProposalsTO.setStatus(MarketProposalsStatusTypeTO.ARCHIVAL);
        }

        marketProposalsTO.setGiveDate(marketProposalWS.getGiveDate().toGregorianCalendar().getTime());

        DetailBatchService detailBatchService = SpringServiceContext.getInstance().getDetailBatchesService();
        marketProposalsTO.setDetailBatch(DetailBatchTransformer.batchTO(detailBatchService.getDetailBatchByUid(marketProposalWS.getUidGoods())));

        ContractorService contractorService = SpringServiceContext.getInstance().getContractorService();
        marketProposalsTO.setContractor(ContractorTransformer.transformContractor(contractorService.getContractorByUid(marketProposalWS.getUidContractor())));

        CurrencyService currencyService = SpringServiceContext.getInstance().getCurrencyService();
        marketProposalsTO.setCurrency(CurrencyTransformer.transformCurrency(currencyService.getCurrencyByUid(marketProposalWS.getUidCurrency())));

        MeasureUnitService measureUnitService = SpringServiceContext.getInstance().getMeasureUnitService();
        marketProposalsTO.setMeasureUnit(MeasureUnitTransformer.transform(measureUnitService.getMeasureUnitByUid(marketProposalWS.getUidMeasureUnit())));

        marketProposalsTO.setAmount(marketProposalWS.getAmount());
        if (marketProposalWS.getAmountType().value().equals(MarketProposalsAmountTypeTO.APPROXIMATE.name())) {
            marketProposalsTO.setAmountType(MarketProposalsAmountTypeTO.APPROXIMATE);
        } else if (marketProposalWS.getAmountType().value().equals(MarketProposalsAmountTypeTO.EXACT.name())) {
            marketProposalsTO.setAmountType(MarketProposalsAmountTypeTO.EXACT);
        } else if (marketProposalWS.getAmountType().value().equals(MarketProposalsAmountTypeTO.GREATER.name())) {
            marketProposalsTO.setAmountType(MarketProposalsAmountTypeTO.GREATER);
        } else if (marketProposalWS.getAmountType().value().equals(MarketProposalsAmountTypeTO.LESS.name())) {
            marketProposalsTO.setAmountType(MarketProposalsAmountTypeTO.LESS);
        } else if (marketProposalWS.getAmountType().value().equals(MarketProposalsAmountTypeTO.UNKNOWN.name())) {
            marketProposalsTO.setAmountType(MarketProposalsAmountTypeTO.UNKNOWN);
        }

        marketProposalsTO.setRetailPrice(marketProposalWS.getRetailPrice());
        marketProposalsTO.setWholeSalePrice(marketProposalWS.getWholeSalePrice());
        marketProposalsTO.setSmallWholeSalePrice(marketProposalWS.getSmallWholeSalePrice());
        marketProposalsTO.setOriginalName(marketProposalWS.getOriginalName());
        marketProposalsTO.setOriginalMisc(marketProposalWS.getOriginalMisc());

        return marketProposalsTO;
    }

    /**
     * Convert list of web-service market proposals objects to list with market proposals domian model objects.
     */
    public static List<MarketProposalsTO> transform(List<MarketProposal> marketProposals) {
        List<MarketProposalsTO> marketProposalsTO = new ArrayList<MarketProposalsTO>();
        for (MarketProposal marketProposal : marketProposals) {
            MarketProposalsTO marketProposalTO = MarketProposalsTransformer.transform(marketProposal);
            marketProposalsTO.add(marketProposalTO);
        }
        return marketProposalsTO;
    }
}
