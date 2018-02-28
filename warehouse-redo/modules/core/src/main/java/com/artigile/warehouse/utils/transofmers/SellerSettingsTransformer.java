/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.priceimport.SellerSettings;
import com.artigile.warehouse.utils.dto.priceimport.SellerSettingsTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Valery Barysok, 9/19/11
 */

public class SellerSettingsTransformer {

    private SellerSettingsTransformer() {
    }

    public static SellerSettingsTO transform(SellerSettings sellerSettings) {
        SellerSettingsTO sellerSettingsTO = new SellerSettingsTO(sellerSettings.getId());
        update(sellerSettingsTO, sellerSettings);
        return sellerSettingsTO;
    }

    public static List<SellerSettingsTO> transformList(List<SellerSettings> sellerSettingsList) {
        List<SellerSettingsTO> sellerSettingsTO = new ArrayList<SellerSettingsTO>();
        for (SellerSettings sellerSettings : sellerSettingsList){
            sellerSettingsTO.add(transform(sellerSettings));
        }
        return sellerSettingsTO;
    }

    public static void update(SellerSettingsTO sellerSettingsTO, SellerSettings sellerSettings) {
        sellerSettingsTO.setUser(UserTransformer.transformUser(sellerSettings.getUser()));
        sellerSettingsTO.setContractorId(sellerSettings.getContractorId());
        sellerSettingsTO.setCurrencyId(sellerSettings.getCurrencyId());
        sellerSettingsTO.setMeasureUnitId(sellerSettings.getMeasureUnitId());
        sellerSettingsTO.setImportAdapterUid(sellerSettings.getImportAdapterUid());
        sellerSettingsTO.setAdapterConfig(sellerSettings.getAdapterConfig());
    }

    public static SellerSettings transform(SellerSettingsTO sellerSettingsTO) {
        SellerSettings sellerSettings = new SellerSettings(sellerSettingsTO.getId());
        update(sellerSettings, sellerSettingsTO);
        return sellerSettings;
    }

    public static void update(SellerSettings sellerSettings, SellerSettingsTO sellerSettingsTO) {
        sellerSettings.setUser(UserTransformer.transformUser(sellerSettingsTO.getUser()));
        sellerSettings.setContractorId(sellerSettingsTO.getContractorId());
        sellerSettings.setCurrencyId(sellerSettingsTO.getCurrencyId());
        sellerSettings.setMeasureUnitId(sellerSettingsTO.getMeasureUnitId());
        sellerSettings.setImportAdapterUid(sellerSettingsTO.getImportAdapterUid());
        sellerSettings.setAdapterConfig(sellerSettingsTO.getAdapterConfig());
    }
}
