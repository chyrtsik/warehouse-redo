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

/**
 * @author IoaN, Feb 21, 2009
 */

import com.artigile.warehouse.bl.common.exceptions.BugReportProperties;
import com.artigile.warehouse.bl.common.exceptions.CannotPerformOperationException;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.Assert;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.artigile.warehouse.bl.mail.Util.getDetailMessage;

/**
 * This service used to send emails.
 * This mail service work only with secured connection with enabled TSL(SSL).<br>
 * The configuration data for email can be found at <b>[classpath:/config/email.properties]</b>.
 */
public class EmailService {

    private static final String MESSAGE_ENCODING = "utf8";

    /**
     * Java threads executor
     */
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Predefined mail sender @see [classpath:/config/applicationContext-mail.xml]
     */
    private JavaMailSenderImpl mailSender;

    /**
     * Predefined mail message @see [classpath:/config/applicationContext-mail.xml]
     */
    private SimpleMailMessage mailMessage;

    /**
     * Injected application version
     */
    private String appVersion;

    /**
     * Velocity engine for accepting templates
     */
    private VelocityEngine velocityEngine;


    /**
     * Sends error email report
     *
     * @param properties description of bug
     */
    @SuppressWarnings({"HardCodedStringLiteral"})
    public void sendErrorReport(BugReportProperties properties) throws CannotPerformOperationException {
        try {
            mailSender.send(getMailMessage(properties));
        } catch (MailException ex) {
            LoggingFacade.logError(this, "Internet connection is not available. Error tableReport sending was skipped.", ex);
            throw new CannotPerformOperationException(I18nSupport.message("bugreport.email.send.error"), ex);
        }
    }

    public SimpleMailMessage getMailMessage(BugReportProperties properties) {
        SimpleMailMessage message = new SimpleMailMessage(mailMessage);
        message.setSentDate(new Date());
        String subj = properties.getTitle();
        if(subj == null || subj.isEmpty()) {
            subj = "Error: " + properties.getException().getMessage();
        }
        message.setSubject(subj);
        String m = getMessage(properties) + "\n";
        m += getDetailMessage(properties.getException(), appVersion);
        message.setText(m);
        return message;
    }

    private String getMessage(BugReportProperties properties) {
        StringBuilder detailMessage = new StringBuilder();
        detailMessage.append("Contributor name: ").append(properties.getName()).append("\n");
        detailMessage.append("Company: ").append(properties.getCompany()).append("\n");
        detailMessage.append("Phone: ").append(properties.getPhone()).append("\n");
        detailMessage.append("email: ").append(properties.getEmail()).append("\n");
        detailMessage.append("summary: ").append(properties.getSummary()).append("\n");
        detailMessage.append("reproducibility: ").append(properties.getReproducibility()).append("\n");
        return detailMessage.toString();
    }

    /**
     * Creates and returns new instance of the JAVA mail sender.
     *
     * @param host Mail server host
     * @param port Mail server port
     * @param username Username for the account at the mail host
     * @param password Password for the account at the mail host
     * @return New instance of mail sender
     */
    public static JavaMailSender getMailSenderInstance(String host, String port, String username, String password) {
        Assert.hasText(host);
        Assert.hasText(username);
        Assert.hasText(password);
        if (!StringUtils.isNumberInteger(port)) {
            throw new IllegalArgumentException();
        }

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Set base parameters
        mailSender.setHost(host);
        int portInt = Integer.valueOf(port);
        mailSender.setPort(portInt);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        // Set predefined optional parameters
        Properties mailSenderProperties = new Properties();
        mailSenderProperties.put("mail.smtp.auth", true);
        mailSenderProperties.put("mail.smtp.starttls.enable", true);
        mailSenderProperties.put("mail.smtp.socketFactory.port", portInt);
        mailSenderProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailSenderProperties.put("mail.smtp.socketFactory.fallback", false);
        mailSender.setJavaMailProperties(mailSenderProperties);

        return mailSender;
    }

    /**
     * Creates and returns instance of mime message.
     *
     * @param mailSender Sender service
     * @param from Who sends message
     * @param to Who receives message
     * @param subject Subject of message
     * @param content Message content
     * @param html True - message contains HTML content, false - message contains plain text
     * @return New instance of mime message
     */
    public static MimeMessage getMimeMessageInstance(JavaMailSender mailSender, String from, String to, String subject, String content, boolean html) {
        Assert.notNull(mailSender);
        Assert.hasText(from);
        Assert.hasText(to);
        Assert.hasText(subject);
        Assert.hasText(content);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, MESSAGE_ENCODING);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content, html);
        } catch (MessagingException e) {
            LoggingFacade.logError(e);
            return null;
        }
        return mimeMessage;
    }

    /**
     * Applies template to parameters.
     *
     * @param parameters Parameters of the message
     * @param emailTemplate Template of the message
     * @return String representation of the message
     */
    public String applyTemplate(Map<String, Object> parameters, EmailTemplate emailTemplate) {
        Assert.notNull(parameters);
        Assert.notNull(emailTemplate);
        try {
            return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, emailTemplate.getTemplateFilename(), MESSAGE_ENCODING, parameters);
        } catch (VelocityException e) {
            LoggingFacade.logError(e);
            return StringUtils.EMPTY_STRING;
        }
    }

    /**
     * Sends email.
     *
     * @param message Message for sending
     * @param mailSender Sender service
     * @throws MailException Thrown when failure sending the email
     */
    public static void sendMessage(MimeMessage message, JavaMailSender mailSender) throws MailException {
        Assert.notNull(message);
        Assert.notNull(mailSender);
        mailSender.send(message);
    }

    /**
     * Sends email asynchronously (in a separated thread).
     *
     * @param message Message for sending
     * @param mailSender Sender service
     */
    public static void sendMessageAsynchronously(MimeMessage message, JavaMailSender mailSender) {
        Assert.notNull(message);
        Assert.notNull(mailSender);
        executorService.execute(new AsynchronousMailSender(message, mailSender));
    }


    /* Setters
    ------------------------------------------------------------------------------------------------------------------*/
    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    public void setMailMessage(SimpleMailMessage mailMessage) {
        this.mailMessage = mailMessage;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
}
