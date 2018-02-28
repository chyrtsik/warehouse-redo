/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.priceimport;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.dao.SellerSettingsDAO;
import com.artigile.warehouse.domain.priceimport.SellerSettings;
import com.artigile.warehouse.utils.dto.priceimport.SellerSettingsTO;
import com.artigile.warehouse.utils.transofmers.SellerSettingsTransformer;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Valery Barysok, 9/19/11
 */

@Transactional(rollbackFor = BusinessException.class)
public class SellerSettingsService {

    private SellerSettingsDAO sellerSettingsDAO;

    public void setSellerSettingsDAO(SellerSettingsDAO sellerSettingsDAO) {
        this.sellerSettingsDAO = sellerSettingsDAO;
    }

    public SellerSettingsTO findSellerSettingsBy(long userId, long contractorId) {
        SellerSettings sellerSettings = sellerSettingsDAO.findSellerSettingsBy(userId, contractorId);
        return sellerSettings == null ? null : SellerSettingsTransformer.transform(sellerSettings);
    }

    public void saveSellerSettings(SellerSettingsTO sellerSettingsTO) {
        SellerSettings sellerSettings = sellerSettingsDAO.findSellerSettingsBy(sellerSettingsTO.getUser().getId(), sellerSettingsTO.getContractorId());
        if (sellerSettings != null) {
            SellerSettingsTransformer.update(sellerSettings, sellerSettingsTO);
            sellerSettingsDAO.saveOrUpdate(sellerSettings);
        } else {
            sellerSettingsDAO.save(SellerSettingsTransformer.transform(sellerSettingsTO));
        }
    }
}
