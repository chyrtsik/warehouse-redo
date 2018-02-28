package com.artigile.warehouse.bl.marketProposals;

import com.artigile.warehouse.bl.contractors.ContractorService;
import com.artigile.warehouse.bl.detail.DetailBatchService;
import com.artigile.warehouse.bl.directory.MeasureUnitService;
import com.artigile.warehouse.bl.finance.CurrencyService;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.webservices.MarketProposalFilter;

import java.util.List;

/**
 * @author Vadim.Zverugo
 */

/**
 * Market proposals filtration model.
 */
public class MarketProposalsFiltration {

    /**
     * Create filter with required criteria.
     * @param detailBatchIds Ids of goods, which need to select.
     * @param contractorIds Ids of contractors, which need to select.
     * @param currencyIds Ids of currencies, which need to select.
     * @param measureUnitIds Ids of measure units, which need to select.
     * @return New market proposals filter.
     */
    public static MarketProposalFilter createFilter(List<Long> detailBatchIds,
                                                    List<Long> contractorIds,
                                                    List<Long> currencyIds,
                                                    List<Long> measureUnitIds) {

        MarketProposalFilter filter = new MarketProposalFilter();

        if (!detailBatchIds.isEmpty()) {
            DetailBatchService detailBatchService = SpringServiceContext.getInstance().getDetailBatchesService();
            filter.getUidGoodsList().addAll(detailBatchService.getDetailBatchUidsByIds(detailBatchIds));
        }
        if (!contractorIds.isEmpty()) {
            ContractorService contractorService = SpringServiceContext.getInstance().getContractorService();
            filter.getUidContractorsList().addAll(contractorService.getContractorUidsByIds(contractorIds));
        }
        if (!currencyIds.isEmpty()) {
            CurrencyService currencyService = SpringServiceContext.getInstance().getCurrencyService();
            filter.getUidCurrencyList().addAll(currencyService.getCurrencyUidsByIds(currencyIds));
        }
        if (!measureUnitIds.isEmpty()) {
            MeasureUnitService measureUnitService = SpringServiceContext.getInstance().getMeasureUnitService();
            filter.getUidMeasureUnitList().addAll(measureUnitService.getMeasureUnitUidsByIds(measureUnitIds));
        }
            
        return filter;
    }
}
