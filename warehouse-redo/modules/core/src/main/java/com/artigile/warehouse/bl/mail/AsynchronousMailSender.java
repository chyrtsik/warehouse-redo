/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.mail;

import com.artigile.warehouse.utils.logging.LoggingFacade;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

/**
 * Mail sender which works in a separate thread and sends mime messages.
 * Use <code>ExecutorService</code> to launch sender.
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class AsynchronousMailSender implements Runnable {

    /**
     * Message for sending
     */
    private MimeMessage mailMessage;

    /**
     * Service for sending
     */
    private JavaMailSender mailSender;


    public AsynchronousMailSender(MimeMessage mailMessage, JavaMailSender mailSender) {
        this.mailMessage = mailMessage;
        this.mailSender = mailSender;
    }


    @Override
    public void run() {
        try {
            mailSender.send(mailMessage);
        } catch (MailException e) {
            LoggingFacade.logError(e);
        }
    }
}
