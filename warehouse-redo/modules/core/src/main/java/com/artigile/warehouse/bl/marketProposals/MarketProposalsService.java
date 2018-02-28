package com.artigile.warehouse.bl.marketProposals;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.utils.dto.marketProposals.MarketProposalsTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.artigile.warehouse.webservices.MarketProposal;
import com.artigile.warehouse.webservices.MarketProposalFilter;
import com.artigile.warehouse.webservices.MarketProposalsWebService;
import com.artigile.warehouse.webservices.MarketProposalsWebServiceService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Vadim.Zverugo
 */

/**
 * Market proposals business methods.
 */
@Transactional(rollbackFor = BusinessException.class)
public class MarketProposalsService {

    /**
     * Get all market proposals from remote web-service by some criteria.
     * @param detailBatchIds Ids of goods, which need to select.
     * @param contractorIds Ids of contractors, which need to select.
     * @param currencyIds Ids of currencies, which need to select.
     * @param measureUnitIds Ids of measure units, which need to select.
     * @return Market proposals data list.
     */
    public List<MarketProposalsTO> findMarketProposals(List<Long> detailBatchIds, List<Long> contractorIds,
                                                       List<Long> currencyIds, List<Long> measureUnitIds) {
        MarketProposalsWebService webService = null;
        try {
            MarketProposalsWebServiceService service = new MarketProposalsWebServiceService();
            webService = service.getMarketProposalsWebServicePort();
        } catch (Exception e) {
            LoggingFacade.logError(e);
            MessageDialogs.showError(I18nSupport.message("market.prioposals.webservice.error.access"));
        }
        MarketProposalFilter filter = MarketProposalsFiltration.createFilter(detailBatchIds, contractorIds, currencyIds, measureUnitIds);
        List<MarketProposal> marketProposals = webService.getMarketProposalsByFilter(filter, null);
        return MarketProposalsTransformer.transform(marketProposals);
    }

}
