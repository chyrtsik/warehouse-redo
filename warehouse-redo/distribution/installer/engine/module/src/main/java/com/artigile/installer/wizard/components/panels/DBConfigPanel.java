/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.installer.wizard.components.panels;

import com.artigile.installer.utils.CommonUtils;
import com.artigile.installer.utils.config.Configurator;
import com.artigile.installer.utils.db.DatabaseInitializer;
import com.artigile.installer.utils.db.JDBCUtils;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import org.netbeans.installer.utils.ResourceUtils;
import org.netbeans.installer.utils.helper.swing.*;
import org.netbeans.installer.wizard.components.panels.ErrorMessagePanel;
import org.netbeans.installer.wizard.containers.SwingContainer;
import org.netbeans.installer.wizard.ui.SwingUi;
import org.netbeans.installer.wizard.ui.WizardUi;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.MessageFormat;

/**
 * Wizard's custom panel for creation system database.
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class DBConfigPanel extends ErrorMessagePanel {

    public DBConfigPanel() {
        // Silence is gold
    }

    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public WizardUi getWizardUi() {
        if (wizardUi == null) {
            wizardUi = new DBConfigPanelUI(this);
        }
        return wizardUi;
    }

    @Override
    public void initialize() {
        super.initialize();
    }


    /* Wrapper of this panel
    ------------------------------------------------------------------------------------------------------------------*/
    public static class DBConfigPanelUI extends ErrorMessagePanelUi {

        protected DBConfigPanel panel; // Main container


        public DBConfigPanelUI(DBConfigPanel panel) {
            super(panel);
            this.panel = panel;
        }

        @Override
        public SwingUi getSwingUi(SwingContainer swingContainer) {
            if (swingUi == null) {
                swingUi = new DBConfigPanelSwingUI(panel, swingContainer);
            }
            return super.getSwingUi(swingContainer);
        }
    }


    /* Content of this panel
    /* All custom components configured here
    ------------------------------------------------------------------------------------------------------------------*/
    public static class DBConfigPanelSwingUI extends ErrorMessagePanelSwingUi {

        // Default parameter that are offered to user
        private static final String DEFAULT_SERVER_HOST = "localhost";
        private static final String DEFAULT_SERVER_PORT = "3306";
        private static final String DEFAULT_SERVER_USER = "root";
        private static final String DEFAULT_SERVER_PASSWORD = "root";
        private static final String DEFAULT_DB_NAME = "whclient";
        private static final String DEFAULT_SYS_USERNAME_AND_PASSWORD = "admin";
        
        private static final String PATTERN_DB_NAME = "^[0-9a-zA-Z$_]{1,64}$";

        protected DBConfigPanel panel;

        private NbiPanel createDBPanel;
        private NbiLabel createDBDescLabel;
        private NbiCheckBox createDBCheckBox;

        private NbiPanel configServerPanel; // Contains fields for database server configuration
        private NbiLabel configServerHostLabel; // Database host
        private NbiTextField configServerHostTextField;
        private NbiLabel configServerPortLabel; // Database port
        private NbiTextField configServerPortTextField;
        private NbiLabel configServerUsernameLabel; // Database username
        private NbiTextField configServerUsernameTextField;
        private NbiLabel configServerPasswordLabel; // Database password
        private NbiPasswordField configServerPasswordPassField;

        private NbiPanel configDBPanel; // Contains fields for database configuration
        private NbiLabel configDBNameLabel; // Database name
        private NbiTextField configDBNameTextField;

        private NbiPanel configSysPanel; // Contains fields for system configuration
        private NbiLabel configSysUsernameLabel; // System username
        private NbiTextField configSysUsernameTextField;
        private NbiLabel configSysPasswordLabel; // System password
        private NbiTextField configSysPasswordTextField;
        private NbiLabel configSysRememberLabel1;
        private NbiLabel configSysRememberLabel2;

        private GridBagConstraints gridBagConstraints;

        /**
         * Connection to the system database.
         */
        private Connection dbConnection;

        /**
         * True if the entered database name isn't duplicated on the MySQL server.
         */
        private boolean validDatabaseName;

        /**
         * True if the specified username isn't duplicated on the MySQL server.
         */
        private boolean validUsername;


        public DBConfigPanelSwingUI(DBConfigPanel panel, SwingContainer swingContainer) {
            super(panel, swingContainer);
            this.panel = panel;
            this.gridBagConstraints = new GridBagConstraints();

            // Build UI and initialize listeners
            createComponents();
            initListeners();
        }


        /* Main content methods
        --------------------------------------------------------------------------------------------------------------*/
        @Override
        protected void initialize() {
            super.initialize();
            connectToDatabaseServer();
            checkDatabaseNameDuplication();
            checkUsernameDuplication();
        }

        @Override
        protected void saveInput() {
            super.saveInput();
            if (createDBCheckBox.isSelected()) {
                // Create SQL statement from the current connection
                Statement statement = JDBCUtils.createStatement(dbConnection);
                if (statement != null) {
                    String serverAddress = configServerHostTextField.getText();
                    int serverPort = Integer.valueOf(configServerPortTextField.getText());
                    String username = configSysUsernameTextField.getText();
                    String password = configSysPasswordTextField.getText();
                    String dbName = configDBNameTextField.getText();
                    
                    // Create MySQL user
                    DatabaseInitializer.createUser(statement, username, password);
                    // Create database
                    DatabaseInitializer.createDatabase(statement, dbName, username, password);
                    // Configure Warehouse
                    Configurator.getInstance().configWarehouse(serverAddress, serverPort, dbName);
                }
                JDBCUtils.disconnect(dbConnection);
            }
        }

        @Override
        protected String validateInput() {
            if (super.validateInput() != null) {
                return super.validateInput();
            }
            if (createDBCheckBox.isSelected()) {
                // Server host (is empty?)
                // Max length - 60 characters
                if (CommonUtils.isEmptyString(configServerHostTextField.getText())) {
                    return i18n("DBC.server.host.empty.error");
                }
                // Server port (is empty?)
                if (CommonUtils.isEmptyString(configServerPortTextField.getText())) {
                    return i18n("DBC.server.port.empty.error");
                }
                // Server port (is number?)
                if (!CommonUtils.isIntegerNumberInRange(configServerPortTextField.getText(), 1, 65535)) {
                    return i18n("DBC.server.port.number.error");
                }
                // Server user (is empty?)
                // Max length - 16 characters
                if (CommonUtils.isEmptyString(configServerUsernameTextField.getText())) {
                    return i18n("DBC.server.user.empty.error");
                }
                // Server password (is empty?)
                // Max length - 41 characters
                if (CommonUtils.isEmptyCharArray(configServerPasswordPassField.getPassword())) {
                    return i18n("DBC.server.pass.empty.error");
                }
                // Server connection (Has connection established?)
                if (!JDBCUtils.isOpenedConnection(dbConnection)) {
                    return i18n("DBC.server.connection.error");
                }

                String dbName = configDBNameTextField.getText();
                // Database name (is empty?)
                if (CommonUtils.isEmptyString(dbName)) {
                    return i18n("DBC.database.name.empty.error");
                }
                // Check database name using specific pattern
                if (!dbName.matches(PATTERN_DB_NAME)) {
                    return i18n("DBC.database.name.invalid.pattern");
                }
                // Is database name existing on the MySQL server?
                if (!validDatabaseName) {
                    return i18n("DBC.database.name.duplication.error");
                }
                
                if (!validUsername) {
                    return MessageFormat.format(i18n("DBC.system.user.duplication"), configSysUsernameTextField.getText());
                }
            }
            return null;
        }


        /* Util methods
        --------------------------------------------------------------------------------------------------------------*/
        private String i18n(String key) {
            return ResourceUtils.getString(DBConfigPanel.class, key);
        }

        /**
         * Builds UI.
         * For more information see http://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
         */
        private void createComponents() {
            createDBPanel = new NbiPanel();
            add(createDBPanel, constraints(0, 0, 1, 0, GridBagConstraints.BOTH, 0, 8, 0, 8));

            createDBDescLabel = new NbiLabel();
            createDBDescLabel.setText(i18n("DBC.create.database.description"));
            createDBPanel.add(createDBDescLabel, constraints(0, 0, 1, 0, GridBagConstraints.BOTH, 5, 4, 0, 4));

            createDBCheckBox = new NbiCheckBox();
            createDBCheckBox.setSelected(false); // default state
            createDBCheckBox.setText(i18n("DBC.create.database"));
            createDBPanel.add(createDBCheckBox, constraints(0, 1, 1, 0, GridBagConstraints.NONE, 3, 0, 0, 0));

            // ----- Databases server connection -----
            configServerPanel = new NbiPanel();
            configServerPanel.setVisible(false); // default
            configServerPanel.setBorder(new TitledBorder(i18n("DBC.server.title")));
            add(configServerPanel, constraints(0, 1, 1, 0, GridBagConstraints.BOTH, 6, 8, 0, 8));

            // DB server host
            configServerHostLabel = new NbiLabel();
            configServerHostLabel.setLabelFor(configServerHostTextField);
            prepareLabel(configServerHostLabel, i18n("DBC.server.host"));
            configServerPanel.add(configServerHostLabel, constraints(0, 0, 0, 0, GridBagConstraints.NONE, 2, 4, 0, 0));
            configServerHostTextField = new NbiTextField();
            prepareNormalTextField(configServerHostTextField, DEFAULT_SERVER_HOST);
            configServerPanel.add(configServerHostTextField, constraints(1, 0, 0, 0, GridBagConstraints.NONE, 0, 4, 4, 0));

            // DB server port
            configServerPortLabel = new NbiLabel();
            configServerPortLabel.setLabelFor(configServerPortTextField);
            configServerPortLabel.setText(i18n("DBC.server.port"));
            configServerPanel.add(configServerPortLabel, constraints(2, 0, 0, 0, GridBagConstraints.NONE, 2, 10, 0, 0));
            configServerPortTextField = new NbiTextField();
            prepareShortTextField(configServerPortTextField, DEFAULT_SERVER_PORT);
            configServerPortTextField.setAlignmentY(0);
            configServerPanel.add(configServerPortTextField, constraints(2, 0, 0, 0, GridBagConstraints.NONE, 0, configServerPortLabel.getPreferredSize().width + 14, 0, 0));
            // Horizontal spacer for the all cells at this panel
            configServerPanel.add(new NbiLabel(), constraints(4, 0, 1, 0, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0));

            // DB server user
            configServerUsernameLabel = new NbiLabel();
            configServerUsernameLabel.setLabelFor(configServerUsernameTextField);
            prepareLabel(configServerUsernameLabel, i18n("DBC.server.user"));
            configServerPanel.add(configServerUsernameLabel, constraints(0, 1, 0, 0, GridBagConstraints.NONE, 2, 4, 0, 0));
            configServerUsernameTextField = new NbiTextField();
            prepareNormalTextField(configServerUsernameTextField, DEFAULT_SERVER_USER);
            configServerPanel.add(configServerUsernameTextField, constraints(1, 1, 0, 0, GridBagConstraints.NONE, 0, 4, 4, 0));

            // DB server password
            configServerPasswordLabel = new NbiLabel();
            configServerPasswordLabel.setLabelFor(configServerPasswordPassField);
            prepareLabel(configServerPasswordLabel, i18n("DBC.server.pass"));
            configServerPanel.add(configServerPasswordLabel, constraints(0, 2, 0, 0, GridBagConstraints.NONE, 2, 4, 0, 0));
            configServerPasswordPassField = new NbiPasswordField();
            prepareNormalPasswordField(configServerPasswordPassField, DEFAULT_SERVER_PASSWORD);
            configServerPanel.add(configServerPasswordPassField, constraints(1, 2, 0, 0, GridBagConstraints.NONE, 0, 4, 4, 0));

            // ----- Databases configuration -----
            configDBPanel = new NbiPanel();
            configDBPanel.setVisible(false);
            configDBPanel.setBorder(new TitledBorder(i18n("DBC.database.title")));
            add(configDBPanel, constraints(0, 2, 1, 0, GridBagConstraints.BOTH, 2, 8, 0, 8));

            // Database name
            configDBNameLabel = new NbiLabel();
            configDBNameLabel.setLabelFor(configDBNameTextField);
            prepareLabel(configDBNameLabel, i18n("DBC.database.name"));
            configDBPanel.add(configDBNameLabel, constraints(0, 0, 0, 0, GridBagConstraints.NONE, 2, 4, 0, 0));
            configDBNameTextField = new NbiTextField();
            prepareNormalTextField(configDBNameTextField, DEFAULT_DB_NAME);
            configDBPanel.add(configDBNameTextField, constraints(1, 0, 0, 0, GridBagConstraints.NONE, 0, 4, 4, 0));

            // Horizontal spacer
            configDBPanel.add(new NbiLabel(), constraints(2, 0, 1, 0, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0));

            // ----- System administrator configuration -----
            configSysPanel = new NbiPanel();
            configSysPanel.setVisible(false); // default
            configSysPanel.setBorder(new TitledBorder(i18n("DBC.system.title")));
            add(configSysPanel, constraints(0, 3, 1, 0, GridBagConstraints.BOTH, 2, 8, 0, 8));

            // System username (login)
            configSysUsernameLabel = new NbiLabel();
            configSysUsernameLabel.setLabelFor(configSysUsernameTextField);
            prepareLabel(configSysUsernameLabel, i18n("DBC.system.login"));
            configSysPanel.add(configSysUsernameLabel, constraints(0, 0, 0, 0, GridBagConstraints.NONE, 2, 4, 0, 0));
            configSysUsernameTextField = new NbiTextField();
            prepareNormalTextField(configSysUsernameTextField, DEFAULT_SYS_USERNAME_AND_PASSWORD);
            configSysUsernameTextField.setEditable(false); // Non-editable
            configSysPanel.add(configSysUsernameTextField, constraints(1, 0, 0, 0, GridBagConstraints.NONE, 0, 4, 4, 0));

            configSysRememberLabel1 = new NbiLabel();
            configSysRememberLabel1.setForeground(Color.GRAY);
            configSysRememberLabel1.setText(i18n("DBC.system.remember.text.part1"));
            configSysPanel.add(configSysRememberLabel1, constraints(2, 0, 1, 0, GridBagConstraints.HORIZONTAL, 8, 10, 0, 4));

            // System user's password
            configSysPasswordLabel = new NbiLabel();
            configSysPasswordLabel.setLabelFor(configSysPasswordTextField);
            prepareLabel(configSysPasswordLabel, i18n("DBC.system.password"));
            configSysPanel.add(configSysPasswordLabel, constraints(0, 1, 0, 0, GridBagConstraints.NONE, 2, 4, 0, 0));
            configSysPasswordTextField = new NbiTextField();
            prepareNormalTextField(configSysPasswordTextField, DEFAULT_SYS_USERNAME_AND_PASSWORD);
            configSysPasswordTextField.setEditable(false); // Non-editable
            configSysPanel.add(configSysPasswordTextField, constraints(1, 1, 0, 0, GridBagConstraints.NONE, 0, 4, 4, 0));

            configSysRememberLabel2 = new NbiLabel();
            configSysRememberLabel2.setForeground(Color.GRAY);
            configSysRememberLabel2.setText(i18n("DBC.system.remember.text.part2"));
            configSysPanel.add(configSysRememberLabel2, constraints(2, 1, 1, 0, GridBagConstraints.HORIZONTAL, 0, 10, 0, 4));

            // Vertical spacer (empty panel)
            add(new NbiPanel(), constraints(0, 4, 1, 1, GridBagConstraints.BOTH, 0, 0, 0, 0));
        }

        private void prepareLabel(NbiLabel label, String text) {
            int height = label.getPreferredSize().height;
            label.setPreferredSize(new Dimension(100, height));
            label.setHorizontalAlignment(JLabel.RIGHT);
            label.setText(text);
        }

        private void prepareNormalTextField(NbiTextField textField, String text) {
            int height = textField.getPreferredSize().height;
            textField.setPreferredSize(new Dimension(150, height));
            textField.setText(text);
        }

        private void prepareShortTextField(NbiTextField textField, String text) {
            int height = textField.getPreferredSize().height;
            textField.setPreferredSize(new Dimension(50, height));
            textField.setText(text);
        }

        private void prepareNormalPasswordField(NbiPasswordField passwordField, String password) {
            passwordField.setColumns(18);
            passwordField.setText(password);
        }

        private GridBagConstraints constraints(int posX, int posY,
                                               double weightX, double weightY,
                                               int fill,
                                               int insetTop, int insetLeft, int insetBottom, int insetRight) {
            // Predefined parameters
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.gridheight = 1;
            gridBagConstraints.ipadx = 0;
            gridBagConstraints.ipady = 0;
            gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;

            // Custom parameters
            gridBagConstraints.gridx = posX;
            gridBagConstraints.gridy = posY;
            gridBagConstraints.weightx = weightX;
            gridBagConstraints.weighty = weightY;
            gridBagConstraints.fill = fill;
            gridBagConstraints.insets.top = insetTop;
            gridBagConstraints.insets.left = insetLeft;
            gridBagConstraints.insets.bottom = insetBottom;
            gridBagConstraints.insets.right = insetRight;

            return gridBagConstraints;
        }

        /**
         * Initializes required listeners.
         */
        private void initListeners() {
            createDBCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    onSelectDatabaseCreation();
                }
            });

            ValidatingDocumentListener validatingDocumentListener = new ValidatingDocumentListener(this);
            ConfigServerFocusListener configServerFocusListener = new ConfigServerFocusListener();

            configServerHostTextField.getDocument().addDocumentListener(validatingDocumentListener);
            configServerHostTextField.addFocusListener(configServerFocusListener);
            configServerPortTextField.getDocument().addDocumentListener(validatingDocumentListener);
            configServerPortTextField.addFocusListener(configServerFocusListener);
            configServerUsernameTextField.getDocument().addDocumentListener(validatingDocumentListener);
            configServerUsernameTextField.addFocusListener(configServerFocusListener);
            configServerPasswordPassField.getDocument().addDocumentListener(validatingDocumentListener);
            configServerPasswordPassField.addFocusListener(configServerFocusListener);
            configDBNameTextField.getDocument().addDocumentListener(validatingDocumentListener);
            configDBNameTextField.addFocusListener(new DatabaseNameFocusListener());
            configSysUsernameTextField.getDocument().addDocumentListener(validatingDocumentListener);
            configSysPasswordTextField.getDocument().addDocumentListener(validatingDocumentListener);
        }

        private void onSelectDatabaseCreation() {
            configServerPanel.setVisible(createDBCheckBox.isSelected());
            configSysPanel.setVisible(createDBCheckBox.isSelected());
            configDBPanel.setVisible(createDBCheckBox.isSelected());
        }

        private void connectToDatabaseServer() {
            String serverHost = configServerHostTextField.getText();
            String serverPort = configServerPortTextField.getText();
            String serverUser = configServerUsernameTextField.getText();
            String serverPass = CommonUtils.toString(configServerPasswordPassField.getPassword());

            if (!CommonUtils.isEmptyString(serverHost)
                    && CommonUtils.isIntegerNumberInRange(serverPort, 1, 65535)
                    && !CommonUtils.isEmptyString(serverUser)
                    && !CommonUtils.isEmptyString(serverPass)) {
                dbConnection = JDBCUtils.connect(serverHost, Integer.valueOf(serverPort), serverUser, serverPass, "mysql");
            } else {
                dbConnection = null;
            }
        }

        private void checkDatabaseNameDuplication() {
            String enteredDBName = configDBNameTextField.getText();
            if (!CommonUtils.isEmptyString(enteredDBName) && JDBCUtils.isOpenedConnection(dbConnection)) {
                Statement statement = JDBCUtils.createStatement(dbConnection);
                validDatabaseName = !DatabaseInitializer.databaseDuplicated(statement, enteredDBName);
            } else {
                validDatabaseName = false;
            }
        }
        
        private void checkUsernameDuplication() {
            if (JDBCUtils.isOpenedConnection(dbConnection)) {
                Statement statement = JDBCUtils.createStatement(dbConnection);
                validUsername = !DatabaseInitializer.userDuplicated(statement, configSysUsernameTextField.getText());
            } else {
                validUsername = false;
            }
        }


        /* Custom action listener
        --------------------------------------------------------------------------------------------------------------*/
        private class ConfigServerFocusListener implements FocusListener {

            @Override
            public void focusGained(FocusEvent e) {
                // Nothing to do
            }

            @Override
            public void focusLost(FocusEvent e) {
                connectToDatabaseServer();
                checkDatabaseNameDuplication();
                checkUsernameDuplication();
            }
        }
        
        private class DatabaseNameFocusListener implements FocusListener {

            @Override
            public void focusGained(FocusEvent e) {
                // Nothing to do
            }

            @Override
            public void focusLost(FocusEvent e) {
                checkDatabaseNameDuplication();
            }
        }
    }
}
