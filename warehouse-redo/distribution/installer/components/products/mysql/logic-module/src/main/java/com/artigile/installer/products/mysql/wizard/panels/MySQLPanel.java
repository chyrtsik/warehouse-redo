package com.artigile.installer.products.mysql.wizard.panels;

import com.artigile.installer.utils.config.Configurator;
import org.netbeans.installer.utils.ResourceUtils;
import org.netbeans.installer.utils.StringUtils;
import org.netbeans.installer.utils.SystemUtils;
import org.netbeans.installer.utils.helper.swing.*;
import org.netbeans.installer.wizard.components.panels.DestinationPanel;
import org.netbeans.installer.wizard.containers.SwingContainer;
import org.netbeans.installer.wizard.ui.SwingUi;
import org.netbeans.installer.wizard.ui.WizardUi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Integer.parseInt;

/**
 *
 * @author Valery Barysok
 */
public class MySQLPanel extends DestinationPanel {

    private static boolean allPortsOccupied;

    public MySQLPanel() {
        setProperty(TITLE_PROPERTY,
                DEFAULT_TITLE);
        setProperty(DESCRIPTION_PROPERTY,
                DEFAULT_DESCRIPTION);

        setProperty(DESTINATION_LABEL_TEXT_PROPERTY,
                DEFAULT_DESTINATION_LABEL_TEXT);
        setProperty(DESTINATION_BUTTON_TEXT_PROPERTY,
                DEFAULT_DESTINATION_BUTTON_TEXT);

        setProperty(PASSWORD_LABEL_TEXT_PROPERTY,
                DEFAULT_PASSWORD_LABEL_TEXT);
        setProperty(REPEAT_PASSWORD_LABEL_TEXT_PROPERTY,
                DEFAULT_REPEAT_PASSWORD_LABEL_TEXT);
        setProperty(ANONYMOUS_ACCOUNT_TEXT_PROPERTY,
                DEFAULT_ANONYMOUS_ACCOUNT_LABEL_TEXT);
        setProperty(ANONYMOUS_ACCOUNT_DISABLED_TEXT_PROPERTY,
                DEFAULT_ANONYMOUS_ACCOUNT_LABEL_TEXT_DISABLED);
        setProperty(NETWORK_TEXT_PROPERTY,
                DEFAULT_NETWORK_LABEL_TEXT);
        setProperty(PORT_TEXT_PROPERTY,
                DEFAULT_PORT_LABEL_TEXT);
        setProperty(DEFAULT_PORT_PROPERTY,
                DEFAULT_PORT);
        setProperty(DEFAULT_PASSWORD_PROPERTY,
                DEFAULT_PASSWORD_PROPERTY);

        setProperty(ERROR_PASSWORD_TOO_SHORT_PROPERTY,
                DEFAULT_ERROR_PASSWORD_TOO_SHORT);
        setProperty(ERROR_PASSWORD_SPACES_PROPERTY,
                DEFAULT_ERROR_PASSWORD_SPACES);
        setProperty(ERROR_PASSWORDS_DO_NOT_MATCH_PROPERTY,
                DEFAULT_ERROR_PASSWORDS_DO_NOT_MATCH);
        setProperty(ERROR_ALL_PORTS_OCCUPIED_PROPERTY,
                DEFAULT_ERROR_ALL_PORTS_OCCUPIED);
        setProperty(ERROR_PORT_NULL_PROPERTY,
                DEFAULT_ERROR_PORT_NULL);
        setProperty(ERROR_PORT_NOT_IN_RANGE_PROPERTY,
                DEFAULT_ERROR_PORT_NOT_IN_RANGE);
        setProperty(ERROR_PORT_NOT_INTEGER_PROPERTY,
                DEFAULT_ERROR_PORT_NOT_INTEGER);
        setProperty(ERROR_PORT_OCCUPIED_PROPERTY,
                DEFAULT_ERROR_PORT_OCCUPIED);
        setProperty(MODIFY_SECURITY_TEXT_PROPERTY,
                DEFAULT_MODIFY_SECURITY_TEXT);
        setProperty(DEFAULT_PASSWORD_LABEL_TEXT_PROPERTY,
                DEFAULT_DEFAULT_PASSWORD_LABEL_TEXT);
    }

    @Override
    public WizardUi getWizardUi() {
        if (wizardUi == null) {
            wizardUi = new MySQLPanelUi(this);
        }

        return wizardUi;
    }

    @Override
    public void initialize() {
        super.initialize();

        String password = getWizard().getProperty(PASSWORD_PROPERTY);
        if (password == null) {
            getWizard().setProperty(PASSWORD_PROPERTY,
                    getProperty(DEFAULT_PASSWORD_PROPERTY));
        }

        String port = getWizard().getProperty(PORT_PROPERTY);
        if (port == null) {
            final int defaultPort = SystemUtils.getAvailablePort(
                parseInt(getProperty(DEFAULT_PORT_PROPERTY)));
            if (defaultPort != -1) {
                port = Integer.toString(defaultPort);
                allPortsOccupied = false;
            } else {
                port = StringUtils.EMPTY_STRING;
                allPortsOccupied = true;
            }
        }
        getWizard().setProperty(PORT_PROPERTY, port);

        String anonymous = getWizard().getProperty(ANONYMOUS_ACCOUNT_PROPERTY);
        if (anonymous == null) {
            getWizard().setProperty(ANONYMOUS_ACCOUNT_PROPERTY,
                    StringUtils.EMPTY_STRING + false);
        }
        String network = getWizard().getProperty(NETWORK_PROPERTY);
        if (network == null) {
            getWizard().setProperty(NETWORK_PROPERTY,
                    StringUtils.EMPTY_STRING + true);
        }

        String modifySecurity = getWizard().getProperty(MODIFY_SECURITY_PROPERTY);
        if (modifySecurity == null) {
            getWizard().setProperty(MODIFY_SECURITY_PROPERTY,
                    StringUtils.EMPTY_STRING + true);                        
        }
    }

    public static class MySQLPanelUi extends DestinationPanelUi {

        protected MySQLPanel component;

        public MySQLPanelUi(MySQLPanel component) {
            super(component);

            this.component = component;
        }

        @Override
        public SwingUi getSwingUi(SwingContainer container) {
            if (swingUi == null) {
                swingUi = new MySQLPanelSwingUi(component, container);
            }

            return super.getSwingUi(container);
        }
    }

    public static class MySQLPanelSwingUi extends DestinationPanelSwingUi {

        protected MySQLPanel panel;
        private NbiPanel containerPanel;
        private NbiPanel containerPanel2;

        private NbiPasswordField passwordField;
        private NbiPasswordField repeatPasswordField;
        private NbiLabel passwordLabel;
        private NbiLabel repeatPasswordLabel;
        private NbiCheckBox anonymousCheckBox;
        private NbiCheckBox networkCheckBox;
        private NbiTextField portField;
        private NbiLabel portLabel;
        private NbiCheckBox securitySettingsCheckbox;
        private NbiLabel defaultsLabel;

        public MySQLPanelSwingUi(
                final MySQLPanel panel,
                final SwingContainer container) {
            super(panel, container);

            this.panel = panel;

            initComponents();
        }

        @Override
        protected void initialize() {
            passwordLabel.setText(
                    panel.getProperty(PASSWORD_LABEL_TEXT_PROPERTY));
            repeatPasswordLabel.setText(
                    panel.getProperty(REPEAT_PASSWORD_LABEL_TEXT_PROPERTY));
            passwordField.setText(panel.getWizard().getProperty(PASSWORD_PROPERTY));
            repeatPasswordField.setText(panel.getWizard().getProperty(PASSWORD_PROPERTY));

            anonymousCheckBox.setText(
                    panel.getProperty(ANONYMOUS_ACCOUNT_DISABLED_TEXT_PROPERTY));

            anonymousCheckBox.setSelected(false);
            if (Boolean.parseBoolean(
                    panel.getWizard().getProperty(ANONYMOUS_ACCOUNT_PROPERTY))) {
                anonymousCheckBox.doClick();
            }

            networkCheckBox.setText(
                    panel.getProperty(NETWORK_TEXT_PROPERTY));

            networkCheckBox.setSelected(false);
            if (Boolean.parseBoolean(
                    panel.getWizard().getProperty(NETWORK_PROPERTY))) {
                networkCheckBox.doClick();
            }

            securitySettingsCheckbox.setText(
                    panel.getProperty(MODIFY_SECURITY_TEXT_PROPERTY));
            securitySettingsCheckbox.setSelected(false);

            if (Boolean.parseBoolean(
                    panel.getWizard().getProperty(MODIFY_SECURITY_PROPERTY))) {
                securitySettingsCheckbox.doClick();
            }

            portLabel.setText(panel.getProperty(PORT_TEXT_PROPERTY));
            portField.setText(panel.getWizard().getProperty(PORT_PROPERTY));
            defaultsLabel.setText(panel.getProperty(DEFAULT_PASSWORD_LABEL_TEXT_PROPERTY));
            
            super.initialize();
        }

        @Override
        protected void saveInput() {
            super.saveInput();
            Configurator.getInstance().setMysqlDestinationPath(getDestinationField().getText());
            panel.getWizard().setProperty(
                    PASSWORD_PROPERTY,
                    new String(passwordField.getPassword()));
            panel.getWizard().setProperty(
                    ANONYMOUS_ACCOUNT_PROPERTY,
                    StringUtils.EMPTY_STRING + anonymousCheckBox.isSelected());
            panel.getWizard().setProperty(
                    PORT_PROPERTY,
                    portField.getText().trim());
            panel.getWizard().setProperty(
                    NETWORK_PROPERTY,
                    StringUtils.EMPTY_STRING + networkCheckBox.isSelected());
            panel.getWizard().setProperty(
                    MODIFY_SECURITY_PROPERTY,
                    StringUtils.EMPTY_STRING + securitySettingsCheckbox.isSelected());
        }

        @Override
        protected String validateInput() {
            String errorMessage = super.validateInput();

            if (errorMessage != null) {
                return errorMessage;
            }

            if (securitySettingsCheckbox.isSelected()) {
                final String password = new String(passwordField.getPassword());
                final String password2 = new String(repeatPasswordField.getPassword());

                if (!password.trim().equals(StringUtils.EMPTY_STRING) ||
                        !password2.trim().equals(StringUtils.EMPTY_STRING)) {
                    if (!password.equals(password2)) {
                        return StringUtils.format(
                                panel.getProperty(ERROR_PASSWORDS_DO_NOT_MATCH_PROPERTY),
                                password,
                                password2);
                    }
                    
                    // We don't need to check the length of the password
                    //if (password.length() < 8) {
                    //    return StringUtils.format(
                    //            panel.getProperty(ERROR_PASSWORD_TOO_SHORT_PROPERTY),
                    //            password,
                    //            password2);
                    //}

                    if (!password.trim().equals(password)) {
                        return StringUtils.format(
                                panel.getProperty(ERROR_PASSWORD_SPACES_PROPERTY),
                                password,
                                password2);
                    }
                }
            }
            
            final String port = portField.getText().trim();
            if (port == null || port.equals("")) {
                return panel.getProperty(allPortsOccupied ? ERROR_ALL_PORTS_OCCUPIED_PROPERTY : ERROR_PORT_NULL_PROPERTY);
            }

            if (!port.matches("(0|[1-9][0-9]*)")) {
                return StringUtils.format(
                        panel.getProperty(ERROR_PORT_NOT_INTEGER_PROPERTY), port);
            }
            int portNumber = new Integer(port);
            if ((portNumber < 0) || (portNumber > 65535)) {
                return StringUtils.format(
                        panel.getProperty(ERROR_PORT_NOT_IN_RANGE_PROPERTY), port);
            }
            if (!SystemUtils.isPortAvailable(portNumber)) {
                return StringUtils.format(
                        panel.getProperty(ERROR_PORT_OCCUPIED_PROPERTY), port);
            }

            return null;
        }

        @Override
        protected String getWarningMessage() {
            return null;
        }

        private void initComponents() {
            containerPanel = new NbiPanel();
            containerPanel2 = new NbiPanel();
            final Dimension longFieldSize = new Dimension(
                    200,
                    new NbiTextField().getPreferredSize().height);
            final Dimension shortFieldSize = new Dimension(
                    80,
                    longFieldSize.height);

            passwordField = new NbiPasswordField();
            passwordField.setPreferredSize(longFieldSize);
            passwordField.setMinimumSize(longFieldSize);
            passwordField.getDocument().addDocumentListener(
                    new ValidatingDocumentListener(this));

            passwordLabel = new NbiLabel();
            passwordLabel.setLabelFor(passwordField);

            repeatPasswordField = new NbiPasswordField();
            repeatPasswordField.setPreferredSize(longFieldSize);
            repeatPasswordField.setMinimumSize(longFieldSize);
            repeatPasswordField.getDocument().addDocumentListener(
                    new ValidatingDocumentListener(this));

            repeatPasswordLabel = new NbiLabel();
            repeatPasswordLabel.setLabelFor(repeatPasswordField);

            anonymousCheckBox = new NbiCheckBox();

            networkCheckBox = new NbiCheckBox();

            networkCheckBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    portField.setEnabled(networkCheckBox.isSelected());
                    portLabel.setEnabled(networkCheckBox.isSelected());

                    panel.getWizard().setProperty(NETWORK_PROPERTY,
                            StringUtils.EMPTY_STRING + networkCheckBox.isSelected());
                    updateErrorMessage();

                }
            });

            portField = new NbiTextField();
            portField.setPreferredSize(shortFieldSize);
            portField.setMinimumSize(shortFieldSize);
            portField.getDocument().addDocumentListener(
                    new ValidatingDocumentListener(this));

            portLabel = new NbiLabel();
            portLabel.setLabelFor(portField);

            defaultsLabel = new NbiLabel();
            
            securitySettingsCheckbox = new NbiCheckBox();
            securitySettingsCheckbox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JComponent[] securityComponents = new JComponent[]{
                        passwordLabel, passwordField,
                        repeatPasswordLabel, repeatPasswordField,
                        anonymousCheckBox
                    };
                    for (JComponent c : securityComponents) {
                        c.setEnabled(securitySettingsCheckbox.isSelected());
                    }
                    anonymousCheckBox.setText(
                            panel.getProperty(
                            securitySettingsCheckbox.isSelected() ? 
                                ANONYMOUS_ACCOUNT_TEXT_PROPERTY :
                                    ANONYMOUS_ACCOUNT_DISABLED_TEXT_PROPERTY));
                    panel.getWizard().setProperty(MODIFY_SECURITY_PROPERTY,
                            StringUtils.EMPTY_STRING + securitySettingsCheckbox.isSelected());
                    updateErrorMessage();

                }
            });
            int securityPadding = 44;
            containerPanel.add(securitySettingsCheckbox, new GridBagConstraints(
                    0, 0, // x, y
                    4, 1, // width, height
                    0.0, 0.0, // weight-x, weight-y
                    GridBagConstraints.LINE_START, // anchor
                    GridBagConstraints.HORIZONTAL, // fill
                    new Insets(4, 11, 4, 0), // padding
                    0, 0));                           // padx, pady - ???

            containerPanel.add(passwordLabel, new GridBagConstraints(
                    0, 1, // x, y
                    1, 1, // width, height
                    0.0, 0.0, // weight-x, weight-y
                    GridBagConstraints.LINE_START, // anchor
                    GridBagConstraints.HORIZONTAL, // fill
                    new Insets(4, securityPadding + 11, 0, 0), // padding
                    0, 0));                           // padx, pady - ???

            containerPanel.add(passwordField, new GridBagConstraints(
                    1, 1, // x, y
                    2, 1, // width, height
                    0.5, 0.0, // weight-x, weight-y
                    GridBagConstraints.LINE_START, // anchor
                    GridBagConstraints.HORIZONTAL, // fill
                    new Insets(4, 6, 0, 11), // padding
                    0, 0));                           // padx, pady - ???

            containerPanel.add(defaultsLabel, new GridBagConstraints(
                    3, 1, // x, y
                    1, 1, // width, height
                    1.0, 0.0, // weight-x, weight-y
                    GridBagConstraints.LINE_START, // anchor
                    GridBagConstraints.HORIZONTAL, // fill
                    new Insets(4, 0, 0, 11), // padding
                    0, 0));                           // padx, pady - ???

            containerPanel.add(repeatPasswordLabel, new GridBagConstraints(
                    0, 2, // x, y
                    1, 1, // width, height
                    0.0, 0.0, // weight-x, weight-y
                    GridBagConstraints.LINE_START, // anchor
                    GridBagConstraints.HORIZONTAL, // fill
                    new Insets(4, securityPadding + 11, 0, 0), // padding
                    0, 0));                           // padx, pady - ???

            containerPanel.add(repeatPasswordField, new GridBagConstraints(
                    1, 2, // x, y
                    2, 1, // width, height
                    0.5, 0.0, // weight-x, weight-y
                    GridBagConstraints.LINE_START, // anchor
                    GridBagConstraints.HORIZONTAL, // fill
                    new Insets(4, 6, 0, 11), // padding
                    0, 0));                           // padx, pady - ???

            containerPanel.add(new NbiPanel(), new GridBagConstraints(
                    3, 2, // x, y
                    1, 1, // width, height
                    1.0, 0.0, // weight-x, weight-y
                    GridBagConstraints.CENTER, // anchor
                    GridBagConstraints.BOTH, // fill
                    new Insets(0, 0, 0, 0), // padding
                    0, 0));                           // padx, pady - ???

            containerPanel.add(anonymousCheckBox, new GridBagConstraints(
                    0, 3, // x, y
                    4, 1, // width, height
                    0.0, 0.0, // weight-x, weight-y
                    GridBagConstraints.LINE_START, // anchor
                    GridBagConstraints.HORIZONTAL, // fill
                    new Insets(4, securityPadding + 8, 11, 0), // padding
                    0, 0));                           // padx, pady - ???

            containerPanel2.add(networkCheckBox, new GridBagConstraints(
                    0, 4, // x, y
                    4, 1, // width, height
                    0.0, 0.0, // weight-x, weight-y
                    GridBagConstraints.LINE_START, // anchor
                    GridBagConstraints.HORIZONTAL, // fill
                    new Insets(4, 11, 0, 0), // padding
                    0, 0));                           // padx, pady - ???

            containerPanel2.add(portLabel, new GridBagConstraints(
                    0, 5, // x, y
                    1, 1, // width, height
                    0.0, 0.0, // weight-x, weight-y
                    GridBagConstraints.LINE_START, // anchor
                    GridBagConstraints.HORIZONTAL, // fill
                    new Insets(4, securityPadding + 11, 0, 0), // padding
                    0, 0));                           // padx, pady - ???

            containerPanel2.add(portField, new GridBagConstraints(
                    1, 5, // x, y
                    1, 1, // width, height
                    0.1, 0.0, // weight-x, weight-y
                    GridBagConstraints.LINE_START, // anchor
                    GridBagConstraints.HORIZONTAL, // fill
                    new Insets(4, 6, 0, 11), // padding
                    0, 0));                           // padx, pady - ???

            containerPanel2.add(new NbiPanel(), new GridBagConstraints(
                    2, 5, // x, y
                    1, 1, // width, height
                    0.4, 0.0, // weight-x, weight-y
                    GridBagConstraints.CENTER, // anchor
                    GridBagConstraints.BOTH, // fill
                    new Insets(0, 0, 0, 0), // padding
                    0, 0));                           // padx, pady - ???

            containerPanel2.add(new NbiPanel(), new GridBagConstraints(
                    3, 5, // x, y
                    1, 1, // width, height
                    1.0, 0.0, // weight-x, weight-y
                    GridBagConstraints.CENTER, // anchor
                    GridBagConstraints.BOTH, // fill
                    new Insets(0, 0, 0, 0), // padding
                    0, 0));                           // padx, pady - ???


            add(containerPanel, new GridBagConstraints(
                    0, 3 , // x, y
                    2, 1, // width, height
                    1.0, 0.0, // weight-x, weight-y
                    GridBagConstraints.LINE_START, // anchor
                    GridBagConstraints.BOTH, // fill
                    new Insets(10, 0, 0, 0), // padding
                    0, 0));                           // padx, pady - ???
            add(containerPanel2, new GridBagConstraints(
                    0, 4 , // x, y
                    2, 1, // width, height
                    1.0, 0.0, // weight-x, weight-y
                    GridBagConstraints.LINE_START, // anchor
                    GridBagConstraints.BOTH, // fill
                    new Insets(0, 0, 0, 0), // padding
                    0, 0));                           // padx, pady - ???


        }
    }

    public static final String DEFAULT_TITLE =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.title"); // NOI18N

    public static final String DEFAULT_DESCRIPTION =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.description"); // NOI18N

    public static final String DEFAULT_DESTINATION_LABEL_TEXT =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.destination.label.text"); // NOI18N

    public static final String DEFAULT_DESTINATION_BUTTON_TEXT =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.destination.button.text"); // NOI18N

    public static final String ERROR_PASSWORD_TOO_SHORT_PROPERTY =
            "error.password.too.short"; // NOI18N

    public static final String ERROR_PASSWORD_SPACES_PROPERTY =
            "error.password.spaces"; // NOI18N

    public static final String ERROR_PASSWORDS_DO_NOT_MATCH_PROPERTY =
            "error.passwords.do.not.match"; // NOI18N

    public static final String ERROR_ALL_PORTS_OCCUPIED_PROPERTY =
            "error.all.ports.occupied"; // NOI18N

    public static final String ERROR_PORT_NULL_PROPERTY =
            "error.port.null"; // NOI18N

    public static final String ERROR_PORT_NOT_IN_RANGE_PROPERTY =
            "error.port.not.in.range";
    public static final String ERROR_PORT_NOT_INTEGER_PROPERTY =
            "error.port.not.integer";
    public static final String ERROR_PORT_OCCUPIED_PROPERTY =
            "error.port.occupied";
    public static final String PASSWORD_LABEL_TEXT_PROPERTY =
            "password.label.text"; // NOI18N

    public static final String REPEAT_PASSWORD_LABEL_TEXT_PROPERTY =
            "repeat.password.label.text"; // NOI18N

    public static final String ANONYMOUS_ACCOUNT_TEXT_PROPERTY =
            "anonymous.account.text";
    public static final String ANONYMOUS_ACCOUNT_DISABLED_TEXT_PROPERTY =
            "anonymous.account.disabled.text";
    public static final String NETWORK_TEXT_PROPERTY =
            "network.text";
    public static final String PORT_TEXT_PROPERTY =
            "port.text";
    public static final String PORT_PROPERTY =
            "port";
    public static final String DEFAULT_PORT_PROPERTY =
            "default.port";
    public static final String PASSWORD_PROPERTY =
            "password";
    public static final String ANONYMOUS_ACCOUNT_PROPERTY =
            "anonymous.account";
    public static final String NETWORK_PROPERTY =
            "network";
    public static final String MODIFY_SECURITY_PROPERTY =
            "modify.security";
    
    public static final String DEFAULT_PASSWORD_LABEL_TEXT_PROPERTY =
            "default.password.text";
            
    public static final String DEFAULT_PASSWORD_LABEL_TEXT =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.password.label.text"); // NOI18N

    public static final String DEFAULT_REPEAT_PASSWORD_LABEL_TEXT =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.repeat.password.label.text"); // NOI18N

    public static final String DEFAULT_ANONYMOUS_ACCOUNT_LABEL_TEXT =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.anonymous.account.label.text"); // NOI18N
    public static final String DEFAULT_ANONYMOUS_ACCOUNT_LABEL_TEXT_DISABLED =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.anonymous.account.label.text.disabled"); // NOI18N

    public static final String DEFAULT_NETWORK_LABEL_TEXT =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.network.label.text"); // NOI18N

    public static final String DEFAULT_PORT_LABEL_TEXT =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.port.label.text"); // NOI18N

    public static final String DEFAULT_PORT =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.default.port.number");
    public static final String DEFAULT_PASSWORD_PROPERTY =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.default.password");
    public static final String MODIFY_SECURITY_TEXT_PROPERTY =
            "modify.security.text";
    public static final String DEFAULT_MODIFY_SECURITY_TEXT =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.modify.security.text");
    
    public static final String DEFAULT_ERROR_PASSWORD_TOO_SHORT =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.error.password.too.short"); // NOI18N

    public static final String DEFAULT_ERROR_PASSWORD_SPACES =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.error.password.spaces"); // NOI18N

    public static final String DEFAULT_ERROR_PASSWORDS_DO_NOT_MATCH =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.error.passwords.do.not.match"); // NOI18N

    public static final String DEFAULT_ERROR_ALL_PORTS_OCCUPIED =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.error.all.ports.occupied"); // NOI18N

    public static final String DEFAULT_ERROR_PORT_NULL =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.error.http.null"); // NOI18N

    public static final String DEFAULT_ERROR_PORT_NOT_INTEGER =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.error.port.not.integer"); // NOI18N

    public static final String DEFAULT_ERROR_PORT_NOT_IN_RANGE =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.error.port.not.in.range"); // NOI18N

    public static final String DEFAULT_ERROR_PORT_OCCUPIED =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.error.port.occupied"); //NOI18N
    public static final String DEFAULT_DEFAULT_PASSWORD_LABEL_TEXT =
            ResourceUtils.getString(MySQLPanel.class,
            "MSP.default.password.text"); //NOI18N
}
