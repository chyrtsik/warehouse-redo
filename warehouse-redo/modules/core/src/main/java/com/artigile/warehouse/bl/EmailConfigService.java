/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl;

import com.artigile.warehouse.dao.EmailConfigDAO;
import com.artigile.warehouse.domain.EmailConfig;
import com.artigile.warehouse.utils.dto.EmailConfigTO;
import com.artigile.warehouse.utils.transofmers.EmailConfigTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
@Transactional
public class EmailConfigService {

    private EmailConfigDAO emailConfigDAO;


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    public EmailConfig getEmailConfigByID(long id) {
        return emailConfigDAO.get(id);
    }

    public EmailConfigTO getAppEmailConfig() {
        List<EmailConfig> emailConfigs = emailConfigDAO.getAll();
        return emailConfigs.isEmpty() ? null : EmailConfigTransformer.transformDomain(emailConfigs.get(0));
    }

    public void mergeEmailConfig(EmailConfigTO emailConfigTO) {
        EmailConfig emailConfig = EmailConfigTransformer.transformTO(emailConfigTO);
        if (emailConfig != null) {
            if (getEmailConfigByID(emailConfig.getId()) == null) {
                emailConfigDAO.save(emailConfig);
            } else {
                emailConfigDAO.update(emailConfig);
            }
        }
    }


    /* Setters
    ------------------------------------------------------------------------------------------------------------------*/
    public void setEmailConfigDAO(EmailConfigDAO emailConfigDAO) {
        this.emailConfigDAO = emailConfigDAO;
    }
}
