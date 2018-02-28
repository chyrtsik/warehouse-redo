/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.EmailConfig;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.EmailConfigTO;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public final class EmailConfigTransformer {

    private EmailConfigTransformer() { /* Silence is gold */ }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    public static EmailConfig transformTO(EmailConfigTO emailConfigTO) {
        if (emailConfigTO == null) {
            return null;
        }
        EmailConfig emailConfig = SpringServiceContext.getInstance().getEmailConfigService()
                .getEmailConfigByID(emailConfigTO.getId());
        if (emailConfig == null) {
            emailConfig = new EmailConfig();
            emailConfig.setId(emailConfigTO.getId());
        }
        emailConfig.setServerHost(emailConfigTO.getServerHost());
        emailConfig.setServerPort(emailConfigTO.getServerPort());
        emailConfig.setAccountUsername(emailConfigTO.getAccountUsername());
        emailConfig.setAccountPassword(emailConfigTO.getAccountPassword());
        emailConfig.setPriceListRequestMessageSubject(emailConfigTO.getPriceListRequestMessageSubject());
        emailConfig.setSelectedPositionsPurchaseMessageSubject(emailConfigTO.getSelectedPositionsPurchaseMessageSubject());

        return emailConfig;
    }

    public static EmailConfigTO transformDomain(EmailConfig emailConfig) {
        if (emailConfig == null) {
            return null;
        }
        EmailConfigTO emailConfigTO = new EmailConfigTO();
        emailConfigTO.setId(emailConfig.getId());
        emailConfigTO.setServerHost(emailConfig.getServerHost());
        emailConfigTO.setServerPort(emailConfig.getServerPort());
        emailConfigTO.setAccountUsername(emailConfig.getAccountUsername());
        emailConfigTO.setAccountPassword(emailConfig.getAccountPassword());
        emailConfigTO.setPriceListRequestMessageSubject(emailConfig.getPriceListRequestMessageSubject());
        emailConfigTO.setSelectedPositionsPurchaseMessageSubject(emailConfig.getSelectedPositionsPurchaseMessageSubject());

        return emailConfigTO;
    }
}
