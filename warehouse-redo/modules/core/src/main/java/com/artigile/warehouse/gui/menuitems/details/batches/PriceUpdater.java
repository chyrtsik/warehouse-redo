package com.artigile.warehouse.gui.menuitems.details.batches;

import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Valery Barysok, 11/7/11
 */
public final class PriceUpdater {

    private PriceUpdater() { }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    public static List<DetailBatchTO> updatePrices(List<DetailBatchTO> result) {
        List<CurrencyTO> currencies = getAllCurrencies();
        Map<Long, Map<Long, Double>> exchangeRateMap = getCurrenciesExchangeRateMap();

        for (DetailBatchTO detailBatchTO : result) {
            updatePrices(detailBatchTO, currencies, exchangeRateMap);
        }
        return result;
    }

    public static DetailBatchTO updatePrices(DetailBatchTO detailBatchTO) {
        return updatePrices(detailBatchTO, getAllCurrencies(), getCurrenciesExchangeRateMap());
    }

    private static DetailBatchTO updatePrices(DetailBatchTO detailBatchTO, 
                                              List<CurrencyTO> currencies,
                                              Map<Long, Map<Long, Double>> exchangeRateMap) {
        if (detailBatchTO.getSellPrice() != null && detailBatchTO.getCurrency() != null) {
            int pos = 2;
            for (CurrencyTO currencyTO : currencies) {
                if (!detailBatchTO.getCurrency().equals(currencyTO)) {
                    // Update currency
                    setCurrency(detailBatchTO, currencyTO, ++pos);

                    // Update price
                    long fromCurrencyID = detailBatchTO.getCurrency().getId();
                    long toCurrencyID = currencyTO.getId();
                    double exchangeRate = exchangeRateMap.containsKey(fromCurrencyID) && exchangeRateMap.get(fromCurrencyID).containsKey(toCurrencyID)
                            ? exchangeRateMap.get(fromCurrencyID).get(toCurrencyID)
                            : 1.0;
                    setSellPrice(detailBatchTO, detailBatchTO.getSellPrice().multiply(BigDecimal.valueOf(exchangeRate)), pos);
                }
            }
        }
        return detailBatchTO;
    }

    private static void setSellPrice(DetailBatchTO detailBatchTO, BigDecimal sellPrice, int pos) {
        switch (pos) {
            case 3: detailBatchTO.setSellPrice3(sellPrice); break;
            case 4: detailBatchTO.setSellPrice4(sellPrice); break;
            case 5: detailBatchTO.setSellPrice5(sellPrice); break;
        }
    }

    private static void setCurrency(DetailBatchTO detailBatchTO, CurrencyTO currencyTO, int pos) {
        switch (pos) {
            case 3: detailBatchTO.setCurrency3(currencyTO); break;
            case 4: detailBatchTO.setCurrency4(currencyTO); break;
            case 5: detailBatchTO.setCurrency5(currencyTO); break;
        }
    }


    /* Util methods
    ------------------------------------------------------------------------------------------------------------------*/
    private static List<CurrencyTO> getAllCurrencies()  {
        return SpringServiceContext.getInstance().getCurrencyService().getAll();
    }
    
    private static Map<Long, Map<Long, Double>> getCurrenciesExchangeRateMap() {
        return SpringServiceContext.getInstance().getExchangeService().getExchangeRateMap();
    }
}
