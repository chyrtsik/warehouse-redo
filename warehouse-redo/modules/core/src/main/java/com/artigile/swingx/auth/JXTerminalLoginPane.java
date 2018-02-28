package com.artigile.swingx.auth;

import com.artigile.warehouse.utils.authentification.MySqlAuthenticator;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.auth.LoginService;
import org.jdesktop.swingx.auth.PasswordStore;
import org.jdesktop.swingx.auth.UserNameStore;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.plaf.UIManagerExt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * @author Valery Barysok, 2013-07-25
 */
public class JXTerminalLoginPane extends JXLoginPane {

    public JXTerminalLoginPane() {
        this(true);
    }

    public JXTerminalLoginPane(boolean terminal) {
        this(null, terminal);
    }

    public JXTerminalLoginPane(LoginService service, boolean terminal) {
        this(service, null, null, null, terminal);
    }

    public JXTerminalLoginPane(LoginService service, PasswordStore passwordStore, UserNameStore userStore, boolean terminal) {
        this(service, passwordStore, userStore, null, terminal);
    }

    public JXTerminalLoginPane(LoginService service, PasswordStore passwordStore, UserNameStore userStore, List<String> servers, boolean terminal) {
        super(service, passwordStore, terminal ? new TerminalUserNameStore() : userStore, servers);
    }

    //TODO: Find out what was in custom lib verison (probably accessibility of components was changes)
//    @Override
//    protected JXPanel createLoginPanel() {
//        if (!(userNameStore instanceof TerminalUserNameStore)) {
//            return super.createLoginPanel();
//        }
//
//        JXPanel loginPanel = new JXPanel();
//
//        JPasswordField oldPwd = passwordField;
//        //create the password component
//        passwordField = new JPasswordField("", 15);
//        JLabel passwordLabel = new JLabel(UIManagerExt.getString(CLASS_NAME + ".passwordString", getLocale()));
//        passwordLabel.setLabelFor(passwordField);
//        if (oldPwd != null) {
//            passwordField.setText(new String(oldPwd.getPassword()));
//        }
//
//        NameComponent oldPanel = namePanel;
//        //create the NameComponent
//        if (saveMode == SaveMode.NONE) {
//            namePanel = new SimpleNamePanel();
//        } else {
//            namePanel = new TerminalComboNamePanel();
//        }
//        if (oldPanel != null) {
//            // need to reset here otherwise value will get lost during LAF change as panel gets recreated.
//            namePanel.setUserName(oldPanel.getUserName());
//            namePanel.setEnabled(oldPanel.isEnabled());
//            namePanel.setEditable(oldPanel.isEditable());
//        } else {
//            namePanel.setEnabled(namePanelEnabled);
//            namePanel.setEditable(false);
//        }
//        JLabel nameLabel = new JLabel(UIManagerExt.getString(CLASS_NAME + ".nameString", getLocale()));
//        nameLabel.setLabelFor(namePanel.getComponent());
//
//
//        //create the server combo box if necessary
//        JLabel serverLabel = new JLabel(UIManagerExt.getString(CLASS_NAME + ".serverString", getLocale()));
//        if (servers.size() > 1) {
//            serverCombo = new JComboBox(servers.toArray());
//            serverCombo.addItemListener(new ServerItemListener());
//            serverLabel.setLabelFor(serverCombo);
//        } else {
//            serverCombo = null;
//        }
//
//        //create the save check box. By default, it is not selected
//        saveCB = new JCheckBox(UIManagerExt.getString(CLASS_NAME + ".rememberPasswordString", getLocale()));
//        saveCB.setIconTextGap(10);
//        //TODO should get this from preferences!!! And, it should be based on the user
//        saveCB.setSelected(false);
//        //determine whether to show/hide the save check box based on the SaveMode
//        saveCB.setVisible(saveMode == SaveMode.PASSWORD || saveMode == SaveMode.BOTH);
//        saveCB.setOpaque(false);
//
//        capsOn = new JLabel(" ");
//        // don't show by default. We perform test when login panel gets focus.
//
//        int lShift = 3;// lShift is used to align all other components with the checkbox
//        GridLayout grid = new GridLayout(2, 1);
//        grid.setVgap(5);
//        JPanel fields = new JPanel(grid);
//        fields.setOpaque(false);
//        fields.add(namePanel.getComponent());
//        fields.add(passwordField);
//
//        GridLayout buttonGrid = new GridLayout(4, 3);
//        JPanel buttons = new JPanel(buttonGrid);
//        for (int i = 0; i <= 9; ++i) {
//            if (i == 9) {
//                JXButton del = new JXButton(I18nSupport.message("keyboard.delete"));
//                del.addActionListener(new DelPadActionListener());
//                buttons.add(del);
//            }
//            int num = (i + 1) % 10;
//            JXButton button = new JXButton("" + num);
//            button.addActionListener(new NumberPadActionListener(num));
//            buttons.add(button);
//            if (i == 9) {
//                JXButton clr = new JXButton(I18nSupport.message("keyboard.clear"));
//                clr.addActionListener(new ClearPadActionListener());
//                buttons.add(clr);
//            }
//        }
//
//        loginPanel.setLayout(new GridBagLayout());
//        GridBagConstraints gridBagConstraints = new GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = 0;
//        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
//        gridBagConstraints.insets = new Insets(4, lShift, 5, 11);
//        loginPanel.add(nameLabel, gridBagConstraints);
//
//        gridBagConstraints = new GridBagConstraints();
//        gridBagConstraints.gridx = 1;
//        gridBagConstraints.gridy = 0;
//        gridBagConstraints.gridwidth = 1;
//        gridBagConstraints.gridheight = 2;
//        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
//        gridBagConstraints.fill = GridBagConstraints.BOTH;
//        gridBagConstraints.weightx = 1.0;
//        gridBagConstraints.insets = new Insets(0, 0, 5, 0);
//        loginPanel.add(fields, gridBagConstraints);
//
//        gridBagConstraints = new GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = 1;
//        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
//        gridBagConstraints.insets = new Insets(5, lShift, 5, 11);
//        loginPanel.add(passwordLabel, gridBagConstraints);
//
//        gridBagConstraints = new GridBagConstraints();
//        gridBagConstraints.gridx = 1;
//        gridBagConstraints.gridy = 2;
//        gridBagConstraints.gridwidth = 1;
//        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
//        gridBagConstraints.fill = GridBagConstraints.BOTH;
//        gridBagConstraints.weightx = 1.0;
//        gridBagConstraints.insets = new Insets(0, 0, 5, 0);
//        loginPanel.add(buttons, gridBagConstraints);
//
//        if (serverCombo != null) {
//            gridBagConstraints = new GridBagConstraints();
//            gridBagConstraints.gridx = 0;
//            gridBagConstraints.gridy = 3;
//            gridBagConstraints.anchor = GridBagConstraints.LINE_START;
//            gridBagConstraints.insets = new Insets(0, lShift, 5, 11);
//            loginPanel.add(serverLabel, gridBagConstraints);
//
//            gridBagConstraints = new GridBagConstraints();
//            gridBagConstraints.gridx = 1;
//            gridBagConstraints.gridy = 3;
//            gridBagConstraints.gridwidth = 1;
//            gridBagConstraints.anchor = GridBagConstraints.LINE_START;
//            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
//            gridBagConstraints.weightx = 1.0;
//            gridBagConstraints.insets = new Insets(0, 0, 5, 0);
//            loginPanel.add(serverCombo, gridBagConstraints);
//
//            gridBagConstraints = new GridBagConstraints();
//            gridBagConstraints.gridx = 0;
//            gridBagConstraints.gridy = 4;
//            gridBagConstraints.gridwidth = 2;
//            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
//            gridBagConstraints.anchor = GridBagConstraints.LINE_START;
//            gridBagConstraints.weightx = 1.0;
//            gridBagConstraints.insets = new Insets(0, 0, 4, 0);
//            loginPanel.add(saveCB, gridBagConstraints);
//
//            gridBagConstraints = new GridBagConstraints();
//            gridBagConstraints.gridx = 0;
//            gridBagConstraints.gridy = 5;
//            gridBagConstraints.gridwidth = 2;
//            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
//            gridBagConstraints.anchor = GridBagConstraints.LINE_START;
//            gridBagConstraints.weightx = 1.0;
//            gridBagConstraints.insets = new Insets(0, lShift, 0, 11);
//            loginPanel.add(capsOn, gridBagConstraints);
//        } else {
//            gridBagConstraints = new GridBagConstraints();
//            gridBagConstraints.gridx = 0;
//            gridBagConstraints.gridy = 3;
//            gridBagConstraints.gridwidth = 2;
//            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
//            gridBagConstraints.anchor = GridBagConstraints.LINE_START;
//            gridBagConstraints.weightx = 1.0;
//            gridBagConstraints.insets = new Insets(0, 0, 4, 0);
//            loginPanel.add(saveCB, gridBagConstraints);
//
//            gridBagConstraints = new GridBagConstraints();
//            gridBagConstraints.gridx = 0;
//            gridBagConstraints.gridy = 4;
//            gridBagConstraints.gridwidth = 2;
//            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
//            gridBagConstraints.anchor = GridBagConstraints.LINE_START;
//            gridBagConstraints.weightx = 1.0;
//            gridBagConstraints.insets = new Insets(0, lShift, 0, 11);
//            loginPanel.add(capsOn, gridBagConstraints);
//        }
//        loginPanel.setOpaque(false);
//        updateUserList();
//        return loginPanel;
//    }
//
//    private void updateUserList() {
//        if (userNameStore != null) {
//            String server = servers.size() == 1 ? servers.get(0) : serverCombo == null ? null : (String)serverCombo.getSelectedItem();
//            if (MySqlAuthenticator.updateConnectionProperties(server)) {
//                userNameStore.loadUserNames();
//                namePanel.setUserName(null);
//            }
//        }
//    }
//
//    /**
//     * If a UserNameStore is used, then this combo box is presented allowing the user
//     * to select a previous login name, or type in a new login name
//     */
//    private final class TerminalComboNamePanel extends JComboBox implements NameComponent {
//        private static final long serialVersionUID = 2511649075486103959L;
//
//        public TerminalComboNamePanel() {
//            super();
//            setModel(new TerminalNameComboBoxModel());
//            setEditable(false);
//
//            // auto-complete based on the users input
//            AutoCompleteDecorator.decorate(this);
//
//            // listen to selection or text input, and offer password suggestion based on current
//            // text
//            if (passwordStore != null && passwordField != null) {
//                final JTextField textfield = (JTextField) getEditor().getEditorComponent();
//                textfield.addKeyListener(new KeyAdapter() {
//
//                    @Override
//                    public void keyReleased(KeyEvent e) {
//                        updatePassword(textfield.getText());
//                    }
//                });
//
//                super.addItemListener(new ItemListener() {
//
//                    public void itemStateChanged(ItemEvent e) {
//                        updatePassword((String) getSelectedItem());
//                    }
//                });
//            }
//        }
//
//        public String getUserName() {
//            Object item = getModel().getSelectedItem();
//            return item == null ? null : item.toString();
//        }
//
//        public void setUserName(String userName) {
//            getModel().setSelectedItem(userName);
//        }
//
//        public void setUserNames(String[] names) {
//            setModel(new DefaultComboBoxModel(names));
//        }
//
//        public JComponent getComponent() {
//            return this;
//        }
//
//        private final class TerminalNameComboBoxModel extends AbstractListModel implements ComboBoxModel {
//
//            private static final long serialVersionUID = 7097674687536018633L;
//
//            private Object selectedItem;
//
//            public void setSelectedItem(Object anItem) {
//                selectedItem = anItem;
//                fireContentsChanged(this, -1, -1);
//            }
//
//            public Object getSelectedItem() {
//                return selectedItem;
//            }
//
//            public Object getElementAt(int index) {
//                return userNameStore.getUserNames()[index];
//            }
//
//            public int getSize() {
//                return userNameStore.getUserNames().length;
//            }
//        }
//    }
//
//    private class NumberPadActionListener implements ActionListener {
//
//        private int num;
//
//        public NumberPadActionListener(int num) {
//            this.num = num;
//        }
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            passwordField.setText(String.valueOf(passwordField.getPassword()) + num);
//        }
//    }
//
//    private class DelPadActionListener implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            char[] password = passwordField.getPassword();
//            if (password.length > 0) {
//                passwordField.setText(String.valueOf(password, 0, password.length - 1));
//            }
//        }
//    }
//
//    private class ClearPadActionListener implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            passwordField.setText("");
//        }
//    }
//
//    private class ServerItemListener implements ItemListener {
//
//        @Override
//        public void itemStateChanged(ItemEvent e) {
//            if (e.getStateChange() == ItemEvent.SELECTED) {
//                updateUserList();
//            }
//        }
//    }
}
