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
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.EmailConfigTO;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public abstract class PriceListRequestEmailService {

    /**
     * Predefined attributes of the message content
     */
    private static final String USER_NAME = "userName";


    /**
     * Sends requests (e-mail messages) at getting new price lists.
     *
     * @param contractors Contractors recipients
     * @param messageText Message content
     * @return True - at least one message successfully sent, false - no one message wasn't sent
     * @throws MailException Failure of sending message
     */
    public static boolean requestPriceLists(List<ContractorTO> contractors, String messageText) {
        boolean messagesSent = false;
        if (!contractors.isEmpty()) {
            // Get e-mail configuration of the current warehouse
            EmailConfigTO appEmailConfig = SpringServiceContext.getInstance().getEmailConfigService().getAppEmailConfig();
            if (appEmailConfig != null) {
                // Create mail sender
                JavaMailSender mailSender = EmailService.getMailSenderInstance(appEmailConfig.getServerHost(),
                        String.valueOf(appEmailConfig.getServerPort()),
                        appEmailConfig.getAccountUsername(),
                        appEmailConfig.getAccountPassword());
                // First email send in the current thread (for checking mail exceptions)...
                // ...all other emails are send in a separated thread (asynchronously)
                boolean firstEmailSent = false;
                MimeMessage message;
                for (ContractorTO contractor : contractors) {
                    if (!firstEmailSent) {
                        // Create and send first email (test email service, configuration)
                        message = getMimeMessageInstance(mailSender, appEmailConfig, contractor.getEmail(), messageText);
                        if (message != null) {
                            EmailService.sendMessage(message, mailSender);
                            firstEmailSent = true;
                            messagesSent = true;
                        }
                    } else {
                        message = getMimeMessageInstance(mailSender, appEmailConfig, contractor.getEmail(), messageText);
                        if (message != null) {
                            EmailService.sendMessageAsynchronously(message, mailSender);
                        }
                    }
                }
            }
        }
        return messagesSent;
    }

    private static MimeMessage getMimeMessageInstance(JavaMailSender mailSender, EmailConfigTO appEmailConfig, String recipientEmail, String messageText) {
        String currentUserEmail = WareHouse.getUserSession().getUser().getEmail();
        String from = !StringUtils.isStringNullOrEmpty(currentUserEmail)
                ? currentUserEmail
                : appEmailConfig.getAccountUsername();
        String subject = appEmailConfig.getPriceListRequestMessageSubject();

        if (!StringUtils.isStringNullOrEmpty(from) && !StringUtils.isStringNullOrEmpty(subject)) {
            return EmailService.getMimeMessageInstance(mailSender, from, recipientEmail, subject, messageText, false);
        }
        return null;
    }

    /**
     * Builds message content using user attributes.
     *
     * @return String representation of the message content
     */
    public static String buildMessageContent() {
        Map<String, Object> contentAttributesMap = new HashMap<String, Object>();
        // User name
        contentAttributesMap.put(USER_NAME, WareHouse.getUserSession().getUser().getDisplayName());

        return SpringServiceContext.getInstance().getEmailService().applyTemplate(contentAttributesMap,
                EmailTemplate.PRICE_LIST_REQUEST_RU);
    }
}

