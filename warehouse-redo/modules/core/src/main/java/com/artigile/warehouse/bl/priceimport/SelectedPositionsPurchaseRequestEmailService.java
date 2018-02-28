/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.priceimport;

import com.artigile.warehouse.bl.mail.EmailService;
import com.artigile.warehouse.bl.mail.EmailTemplate;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.EmailConfigTO;
import com.artigile.warehouse.utils.dto.priceimport.ContractorProductTOForReport;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public abstract class SelectedPositionsPurchaseRequestEmailService {

    /**
     * Predefined attributes of the message content
     */
    private static final String PRODUCT_LIST = "productList";
    private static final String USER_NAME = "userName";


    /**
     * Sends request at purchase.
     *
     * @param recipientEmail Email address of the recipient
     * @param messageText Message content
     * @return True - if message successfully sent, false - if message not sent
     */
    public static boolean requestPurchase(String recipientEmail, String messageText) throws MailException {
        boolean messageSent = false;
        if (!StringUtils.isStringNullOrEmpty(recipientEmail) && !StringUtils.isStringNullOrEmpty(messageText)) {
            // Get e-mail configuration of the current warehouse
            EmailConfigTO appEmailConfig = SpringServiceContext.getInstance().getEmailConfigService().getAppEmailConfig();
            if (appEmailConfig != null) {
                // Create mail sender
                JavaMailSender mailSender = EmailService.getMailSenderInstance(appEmailConfig.getServerHost(),
                        String.valueOf(appEmailConfig.getServerPort()),
                        appEmailConfig.getAccountUsername(),
                        appEmailConfig.getAccountPassword());
                MimeMessage message = getMimeMessageInstance(mailSender, appEmailConfig, recipientEmail, messageText);
                if (message != null) {
                    EmailService.sendMessage(message, mailSender);
                    messageSent = true;
                }
            }
        }
        return messageSent;
    }
    
    private static MimeMessage getMimeMessageInstance(JavaMailSender mailSender, EmailConfigTO appEmailConfig, String recipientEmail, String messageText) {
        String currentUserEmail = WareHouse.getUserSession().getUser().getEmail();
        String from = !StringUtils.isStringNullOrEmpty(currentUserEmail)
                ? currentUserEmail
                : appEmailConfig.getAccountUsername();
        String subject = appEmailConfig.getSelectedPositionsPurchaseMessageSubject();

        if (!StringUtils.isStringNullOrEmpty(from) && !StringUtils.isStringNullOrEmpty(subject)) {
            return EmailService.getMimeMessageInstance(mailSender, from, recipientEmail, subject, messageText, false);
        }
        return null;
    }

    /**
     * Builds message content, using information about products for purchase.
     * 
     * @param products Product for purchase
     * @return String representation of the message content
     */
    public static String buildMessageContent(List<ContractorProductTOForReport> products) {
        Map<String, Object> contentAttributesMap = new HashMap<String, Object>();
        // User name
        contentAttributesMap.put(USER_NAME, WareHouse.getUserSession().getUser().getDisplayName());

        // Product list
        StringBuilder productList = new StringBuilder();
        int productsCount = products.size();
        for (int i = 0; i < productsCount; i++) {
            productList.append(products.get(i).getName());
            if (i != (productsCount - 1)) {
                productList.append("\n");
            }
        }
        contentAttributesMap.put(PRODUCT_LIST, productList.toString());

        return SpringServiceContext.getInstance().getEmailService().applyTemplate(contentAttributesMap,
                EmailTemplate.SELECTED_POSITIONS_PURCHASE_RU);
    }
}
